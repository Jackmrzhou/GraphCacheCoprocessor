package org.gilmour.coprocessor;

import com.google.common.util.concurrent.ListenableFuture;
import io.grpc.Status;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessor;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.coprocessor.RegionObserver;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.regionserver.InternalScanner;
import org.apache.hadoop.hbase.regionserver.MiniBatchOperationInProgress;
import org.apache.hadoop.hbase.wal.WALEdit;
import org.gilmour.coprocessor.CacheService.generated.CacheServer;
import org.gilmour.coprocessor.CacheService.CacheServiceClient;
import org.gilmour.coprocessor.CacheService.ClientCreator;
import org.gilmour.coprocessor.CacheService.HttpResolver;
import org.gilmour.coprocessor.logs.Logger;
import org.gilmour.coprocessor.statistic.Aggregation;
import org.gilmour.coprocessor.utils.Utils;
import org.gilmour.coprocessor.utils.WrapFuture;
import org.gilmour.coprocessor.web.WebAPI;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class GraphCacheObserver implements RegionObserver, RegionCoprocessor {
    private ExecutorService executorService;
    private Logger logger = new Logger();
    private CacheServiceClient client;

    private List<WrapFuture> futures = Collections.synchronizedList(new LinkedList<>());
    private AtomicBoolean cleaning = new AtomicBoolean(false);
    private final int cleaningThreshold = 2000;

    // used for strong consistency
    // todo: change to invalid cache rather than make a local map
    private ConcurrentMap<byte[], Boolean> staleRowKeys;
    private ConcurrentMap<byte[], Boolean> pendingKeys;

    @Override
    public void start(CoprocessorEnvironment env) throws IOException {
        executorService = Executors.newFixedThreadPool(4);
        executorService.execute(new WebAPI());
        client = new ClientCreator().CreateClient(new HttpResolver());
        staleRowKeys = new ConcurrentHashMap<>();
        pendingKeys = new ConcurrentHashMap<>();
        logger.info("coprocessor setup done!");
    }

    @Override
    public void stop(CoprocessorEnvironment env) throws IOException {
        executorService.shutdown();
    }

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
    public void postBatchMutate(ObserverContext<RegionCoprocessorEnvironment> c, MiniBatchOperationInProgress<Mutation> miniBatchOp) throws IOException {
        logger.info("postBatchMutate");
        for (int i = 0; i < miniBatchOp.size(); i++) {
            Mutation mutation = miniBatchOp.getOperation(i);
            if (staleRowKeys.get(mutation.getRow()) != null) {
                // don't update stale row
                continue;
            }
            logger.info(mutation.toJSON());
            NavigableMap<byte[], List<Cell>> map = mutation.getFamilyCellMap();
            List<Cell> cells = new LinkedList<>();
            for (List<Cell> v : map.values()) {
                cells.addAll(v);
            }
            // update the row already cached
            logger.info("updating row:" + new String(mutation.getRow()));
            cacheCells(cells);
        }
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
        cacheCells(cells);
    }

    // warning: all cells must belong to the same row
    private void cacheCells(List<Cell> cells){
        if (cells.size() == 0)
            return;
        WrapFuture<CacheServer.SetValuesResponse> wrapFuture = new WrapFuture<>();
        wrapFuture.future = client.SetValues(cells);
        // putting row key to pending keys, so loading this row from cache is forbidden
        pendingKeys.put(CellUtil.cloneRow(cells.get(0)), true);

        wrapFuture.future.addListener(()->{
            try {
                CacheServer.SetValuesResponse response = wrapFuture.future.get();
                if (response.getCode() != 0) {
                    staleRowKeys.put(CellUtil.cloneRow(cells.get(0)), true);
                    logger.error("set cache failed, row:"
                            + new String(CellUtil.cloneRow(cells.get(0)))
                            + "; message:" + response.getMessage());
                    Aggregation.increCacheFailures();
                } else {
                    logger.info("set cache succeeded, cells:" + cells.size());
                    // remove the key from the pending keys
                    pendingKeys.remove(CellUtil.cloneRow(cells.get(0)));
                }
            } catch (ExecutionException | InterruptedException e) {
                Status status = Status.fromThrowable(e);
                e.printStackTrace();
                staleRowKeys.put(CellUtil.cloneRow(cells.get(0)), true);
                Aggregation.increCacheFailures();
                if (!status.equals(Status.UNKNOWN))
                    logger.error("set cache failed, rpc error. error code:");
                else
                    logger.error("set cache failed, exception:" + e.getMessage());
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
        logger.info(get.toJSON());
        // check whether the cached row is stale or the writing process is undone
        if (staleRowKeys.get(get.getRow()) != null || pendingKeys.get(get.getRow()) != null)
            return;
        ListenableFuture<CacheServer.GetRowResponse> future = client.GetRow(get.getRow());
        try {
            CacheServer.GetRowResponse response = future.get();
            if (response.getCode() == 1) {
                logger.info("cache missed");
                Aggregation.increCacheMissed();
                // cache missed
                Get basicGet = new Get(get.getRow());
                List<Cell> res = c.getEnvironment().getRegion().get(basicGet, false);
                if (res.size() == 0) {
                    logger.error("get row with empty result!");c.bypass();
                    return;
                }
                cacheCells(res);
                doFilter(get, res, result);
                c.bypass();
            }
            else if (response.getCode() == 0) {
                List<CacheServer.HCell> cells = response.getResultList();
                List<Cell> res = new LinkedList<>();
                cellConverter(cells, res);
                doFilter(get, res, result);
                // result is loaded from cache, stop loading from hbase
                c.bypass();
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            logger.info("get row from cache exception");
            logger.info(e.getMessage());
        }
        /*logger.info("cells filter manually");
        for (Cell cell : result) {
            logger.info(cell.toString());
        }
        result.clear();
        if (get.getFilter() != null)
            get.getFilter().reset();*/
    }

    private void doFilter(Get get, List<Cell> in, List<Cell> out) throws IOException{
        if (in.size() == 0)
            return;

        Filter filter = get.getFilter();
        if (filter != null && filter.filterRowKey(in.get(0))){
            // this row is not included
            return;
        }
        Map<byte[], NavigableSet<byte[]>> map = get.getFamilyMap();
        boolean check = !map.isEmpty();
        for (Cell cc : in) {
            Filter.ReturnCode code = Filter.ReturnCode.SKIP;
            if (filter != null) {
                code = filter.filterCell(cc);
            }
            if (filter == null || code == Filter.ReturnCode.INCLUDE || code == Filter.ReturnCode.INCLUDE_AND_NEXT_COL || code == Filter.ReturnCode.INCLUDE_AND_SEEK_NEXT_ROW) {
                if (filter != null) {
                    filter.transformCell(cc);
                }
                if (check) {
                    byte[] cf = CellUtil.cloneFamily(cc);
                    NavigableSet<byte[]> set = map.get(cf);
                    if (map.containsKey(cf)
                            && (set == null || set.contains(CellUtil.cloneQualifier(cc)))
                            && get.getTimeRange().withinTimeRange(cc.getTimestamp())) {
                        out.add(cc);
                    }
                } else {
                    out.add(cc);
                }
            }
        }
        if (filter != null) {
            filter.filterRowCells(out);
        }
        if (filter != null && filter.filterRow()) {
            out.clear();
        }
    }

    @Override
    public void postGetOp(ObserverContext<RegionCoprocessorEnvironment> c, Get get, List<Cell> result) throws IOException {
        logger.info("postGetOp");
        //cacheCells(result);
        /*logger.info("cells filtered by the hbase");
        for (Cell cell : result) {
            logger.info(cell.toString());
        }*/
        if (staleRowKeys.get(get.getRow()) != null) {
            // try to update stale keys
            ListenableFuture<CacheServer.GetRowResponse> future = client.GetRow(get.getRow());
            future.addListener(()->{
                try {
                    CacheServer.GetRowResponse response = future.get();
                    if (response.getCode() == 1) {
                        // stale keys are removed
                        // set cache
                        cacheCells(result);
                        staleRowKeys.remove(get.getRow());
                    }
                    else if (response.getCode() == 0) {
                        // stale keys are still in the cache, ignoring
                    }
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                    logger.info("get row from cache exception");
                    logger.info(e.getMessage());
                }
            }, executorService);
        }
    }

    @Override
    public boolean preScannerNext(ObserverContext<RegionCoprocessorEnvironment> c, InternalScanner s, List<Result> result, int limit, boolean hasNext) throws IOException {
        Aggregation.increScanCalls();
        logger.info("scanOp");
        return hasNext;
    }


    private void triggerClean(){
        logger.info("start to clean finished futures");
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
        Aggregation.setFuturesSize(futures.size());
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
