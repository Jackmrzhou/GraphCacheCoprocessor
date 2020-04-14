package org.gilmour.coprocessor;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessor;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.coprocessor.RegionObserver;
import org.apache.hadoop.hbase.regionserver.InternalScanner;
import org.apache.hadoop.hbase.regionserver.MiniBatchOperationInProgress;
import org.apache.hadoop.hbase.wal.WALEdit;
import org.gilmour.coprocessor.logs.Logger;
import org.gilmour.coprocessor.statistic.Aggregation;
import org.gilmour.coprocessor.web.WebConsole;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GraphCacheObserver implements RegionObserver, RegionCoprocessor {
    private static ExecutorService service;
    private final static Logger logger = new Logger();
    static  {
        service = Executors.newSingleThreadExecutor();
        service.execute(new WebConsole());
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
    public void prePut(ObserverContext<RegionCoprocessorEnvironment> c, Put put, WALEdit edit, Durability durability) throws IOException {
        Aggregation.increPutCalls();
        logger.info("putOp");
        logger.info(put.toJSON());
    }

    @Override
    public void preGetOp(ObserverContext<RegionCoprocessorEnvironment> c, Get get, List<Cell> result) throws IOException {
        Aggregation.increGetCalls();
        logger.info("getOp");
        logger.info(get.toJSON());
        for (Cell cell : result){
            logger.info(cell.toString());
        }
    }

    @Override
    public boolean preScannerNext(ObserverContext<RegionCoprocessorEnvironment> c, InternalScanner s, List<Result> result, int limit, boolean hasNext) throws IOException {
        Aggregation.increScanCalls();
        logger.info("scanOp");
        if (service == null)
            logger.info("service null");
        logger.info(service.toString());
        return hasNext;
    }
}
