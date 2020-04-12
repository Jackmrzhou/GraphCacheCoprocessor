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

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class GraphCacheObserver implements RegionObserver, RegionCoprocessor {
    @Override
    public Optional<RegionObserver> getRegionObserver() {
        return Optional.of(this);
    }

    @Override
    public void preBatchMutate(ObserverContext<RegionCoprocessorEnvironment> c, MiniBatchOperationInProgress<Mutation> miniBatchOp) throws IOException {

    }

    @Override
    public void prePut(ObserverContext<RegionCoprocessorEnvironment> c, Put put, WALEdit edit, Durability durability) throws IOException {

    }

    @Override
    public void preGetOp(ObserverContext<RegionCoprocessorEnvironment> c, Get get, List<Cell> result) throws IOException {

    }

    @Override
    public boolean preScannerNext(ObserverContext<RegionCoprocessorEnvironment> c, InternalScanner s, List<Result> result, int limit, boolean hasNext) throws IOException {
        return hasNext;
    }
}
