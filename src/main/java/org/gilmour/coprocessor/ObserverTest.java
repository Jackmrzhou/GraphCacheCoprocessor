package org.gilmour.coprocessor;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Durability;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessor;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.coprocessor.RegionObserver;
import org.apache.hadoop.hbase.regionserver.InternalScanner;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.wal.WALEdit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Hello world!
 *
 */
public class ObserverTest implements RegionObserver, RegionCoprocessor {
    Logger logger = LoggerFactory.getLogger(ObserverTest.class);
    private static final byte[] ADMIN = Bytes.toBytes("admin");
    private static final byte[] COLUMN_FAMILY = Bytes.toBytes("details");
    private static final byte[] COLUMN = Bytes.toBytes("Admin_det");
    private static final byte[] VALUE = Bytes.toBytes("You can't see Admin details");
    @Override
    public Optional<RegionObserver> getRegionObserver() {
        return Optional.of(this);
    }

    @Override
    public void preGetOp(ObserverContext<RegionCoprocessorEnvironment> c, Get get, List<Cell> result) throws IOException {
        if (Bytes.equals(get.getRow(), ADMIN)) {
            Cell cell = CellUtil.createCell(get.getRow(), COLUMN_FAMILY, COLUMN, System.currentTimeMillis(), (byte) 4, VALUE);
            result.add(cell);
            c.bypass();
        }
    }


    @Override
    public boolean preScannerNext(ObserverContext<RegionCoprocessorEnvironment> c, InternalScanner s, List<Result> result, int limit, boolean hasNext) throws IOException {
        logger.info("entering preScannerNext");
        for (Result r : result) {
            logger.info(r.toString());
        }

        logger.info("exiting preScannerNext");
        return hasNext;
    }

    @Override
    public boolean postScannerNext(ObserverContext<RegionCoprocessorEnvironment> c, InternalScanner s, List<Result> result, int limit, boolean hasNext) throws IOException {
        logger.info("entering postScannerNext");
        for (Result r : result) {
            logger.info(r.toString());
        }
        logger.info("exiting postScannerNext");
        return hasNext;
    }
}