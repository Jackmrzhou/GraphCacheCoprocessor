package org.gilmour.coprocessor.statistic;

import com.google.gson.annotations.Expose;

public class Aggregation {
    @Expose
    static private long batchCalls;
    @Expose
    static private long putCalls;
    @Expose
    static private long getCalls;
    @Expose
    static private long scanCalls;

    public static long getBatchCalls() {
        return batchCalls;
    }

    public static void increBatchCalls() {
        batchCalls++;
    }


    public static long getPutCalls() {
        return putCalls;
    }

    public static void increPutCalls() {
        putCalls++;
    }

    public static long getGetCalls() {
        return getCalls;
    }

    public static void increGetCalls(){
        getCalls++;
    }

    public static long getScanCalls() {
        return scanCalls;
    }

    public static void increScanCalls() {
        scanCalls++;
    }
}
