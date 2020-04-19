package org.gilmour.coprocessor;

import com.google.common.util.concurrent.ListenableFuture;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessor;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.coprocessor.RegionObserver;
import org.apache.hadoop.hbase.regionserver.InternalScanner;
import org.apache.hadoop.hbase.regionserver.MiniBatchOperationInProgress;
import org.apache.hadoop.hbase.wal.WALEdit;
import org.gilmour.coprocessor.CacheService.CacheServer;
import org.gilmour.coprocessor.CacheService.CacheServiceClient;
import org.gilmour.coprocessor.CacheService.ClientCreator;
import org.gilmour.coprocessor.CacheService.HttpResolver;
import org.gilmour.coprocessor.logs.Logger;
import org.gilmour.coprocessor.statistic.Aggregation;
import org.gilmour.coprocessor.utils.Utils;
import org.gilmour.coprocessor.utils.WrapFuture;
import org.gilmour.coprocessor.web.WebConsole;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class GraphCacheObserver implements RegionObserver, RegionCoprocessor {
    private final static ExecutorService executorService;
    private final static Logger logger = new Logger();
    private final static CacheServiceClient client;
    static  {
        executorService = Executors.newFixedThreadPool(4);
        executorService.execute(new WebConsole());
        client = new ClientCreator().CreateClient(new HttpResolver());
    }

    private List<WrapFuture> futures = Collections.synchronizedList(new LinkedList<>());
    private AtomicBoolean cleaning = new AtomicBoolean(false);
    private final int cleaningThreshold = 100;

    @Override
    public Optional<RegionObserver> getRegionObserver() {
        return Optional.of(this);
    }

    @Override
    public void preBatchMutate(ObserverContext<RegionCoprocessorEnvironment> c, MiniBatchOperationInProgress<Mutation> miniBatchOp) throws IOException {
        Aggregation.increBatchCalls();
        logger.info("BatchMutate");
    }

    @Override
    public void prePut(ObserverContext<RegionCoprocessorEnvironment> c, Put put, WALEdit edit, Durability durability) throws IOException {
        Aggregation.increPutCalls();
        logger.info("putOp");
        logger.info(put.toJSON());
        
    }

    @Override
    public void postPut(ObserverContext<RegionCoprocessorEnvironment> c, Put put, WALEdit edit, Durability durability) throws IOException {
        logger.info("postPut");
        NavigableMap<byte[], List<Cell>> map = put.getFamilyCellMap();
        List<Cell> cells = new LinkedList<>();
        for (List<Cell> v : map.values()) {
            cells.addAll(v);
        }
        WrapFuture wrapFuture = new WrapFuture();
        wrapFuture.future = client.SetValues(cells);

        wrapFuture.future.addListener(()->{
            try {
                CacheServer.SetValuesResponse response = (CacheServer.SetValuesResponse) wrapFuture.future.get();
                if (response.getCode() != 0) {
                    // todo: handle cached row consistency
                    logger.info("set cache failed, message:" + response.getMessage());
                }
                logger.info("set cache succeeded, cells:" + cells.size());
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
                logger.info("set cache exception:");
                logger.info(e.getMessage());
            }
            wrapFuture.setFinished(true);
            if (futures.size() >= cleaningThreshold){
                triggerClean();
            }
        }, executorService);

        futures.add(wrapFuture);
    }

    @Override
    public void preGetOp(ObserverContext<RegionCoprocessorEnvironment> c, Get get, List<Cell> result) throws IOException {
        Aggregation.increGetCalls();
        logger.info("getOp");
        ListenableFuture<CacheServer.GetRowResponse> future = client.GetRow(get.getRow());
        try {
            CacheServer.GetRowResponse response = future.get();
            if (response.getCode() == 1) {
                // cache missed
                // todo: add metrics here
                return;
            }
            else if (response.getCode() == 0) {
                // todo: should filter done here?
                List<CacheServer.HCell> cells = response.getResultList();
                if (result == null)
                    result = new LinkedList<>();
                cellConverter(cells, result);
                // result is loaded from cache, stop loading from hbase
                c.bypass();
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            logger.info("get row from cache exception");
            logger.info(e.getMessage());
        }
    }

    @Override
    public boolean preScannerNext(ObserverContext<RegionCoprocessorEnvironment> c, InternalScanner s, List<Result> result, int limit, boolean hasNext) throws IOException {
        Aggregation.increScanCalls();
        logger.info("scanOp");
        return hasNext;
    }


    private void triggerClean(){
        if (cleaning.get()) {
            return;
        }

        if (!cleaning.compareAndSet(false, true))
            return;
        if (futures.size() < cleaningThreshold) {
            cleaning.set(false);
            return;
        }
        // clean all the destroyable future
        futures.removeIf(WrapFuture::destroyable);

        cleaning.set(false);
    }

    private void cellConverter(List<CacheServer.HCell> cells, List<Cell> cellsOut){
        CellBuilder builder = CellBuilderFactory.create(CellBuilderType.DEEP_COPY);
        for (CacheServer.HCell cell : cells) {
            builder.clear();
            builder.setRow(cell.getRow().toByteArray());
            builder.setFamily(cell.getColumnFamily().toByteArray());
            builder.setQualifier(cell.getColumn().toByteArray());
            builder.setTimestamp(cell.getTimestamp());
            builder.setType(Utils.codeToType(cell.getType()));
            builder.setValue(cell.getValue().toByteArray());
            cellsOut.add(builder.build());
        }
    }
}
