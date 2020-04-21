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
    @Expose
    static private long cacheMissed;

    public static long getBatchCalls() {
        return batchCalls;
    }

    synchronized public static void increBatchCalls() {
        batchCalls++;
    }


    public static long getPutCalls() {
        return putCalls;
    }

    synchronized public static void increPutCalls() {
        putCalls++;
    }

    public static long getGetCalls() {
        return getCalls;
    }

    synchronized public static void increGetCalls(){
        getCalls++;
    }

    public static long getScanCalls() {
        return scanCalls;
    }

    synchronized public static void increScanCalls() {
        scanCalls++;
    }

    public static long getCacheMissed() {
        return cacheMissed;
    }

    synchronized public static void increCacheMissed(){
        cacheMissed++;
    }
}
