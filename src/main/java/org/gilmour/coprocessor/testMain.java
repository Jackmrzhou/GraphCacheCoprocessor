package org.gilmour.coprocessor;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import org.gilmour.coprocessor.CacheService.generated.CacheServer;
import org.gilmour.coprocessor.CacheService.CacheServiceClient;
import org.gilmour.coprocessor.CacheService.ClientCreator;
import org.gilmour.coprocessor.CacheService.HttpResolver;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class testMain {
    public static void main(String[] args) throws Exception{
//        GraphCacheObserver observer = new GraphCacheObserver();
//        observer.preBatchMutate(null,null);
//        System.in.read();
//
//        ClientCreator creator = new ClientCreator();
//        CacheServiceClient client = creator.CreateClient(new HttpResolver());
        ManagedChannel channel = ManagedChannelBuilder.forAddress("192.168.123.66", 9988).usePlaintext().build();
        CacheServiceClient client = new CacheServiceClient(channel);
        List<Cell> Hbasecells = new LinkedList<>();
        for (int i = 0; i < 1000; i ++) {
            Hbasecells.add(CellUtil.createCell("999".getBytes(), "e".getBytes(), ("col"+i).getBytes(), System.currentTimeMillis(), (byte) 4, "val".getBytes()));
        }
        ListenableFuture<CacheServer.SetValuesResponse> af = client.SetValues(Hbasecells);

        Hbasecells.clear();
        for (int i = 0; i < 100; i++) {
            Hbasecells.add(CellUtil.createCell("888".getBytes(), "e".getBytes(), ("col"+i).getBytes(), System.currentTimeMillis(), (byte) 4, "val".getBytes()));
        }
        client.SetValues(Hbasecells);

        Hbasecells.clear();
        for (int i = 0; i < 100; i++) {
            Hbasecells.add(CellUtil.createCell("777".getBytes(), "e".getBytes(), ("col"+i).getBytes(), System.currentTimeMillis(), (byte) 4, "val".getBytes()));
        }
        client.SetValues(Hbasecells);

        Hbasecells.clear();
        for (int i = 0; i < 100; i++) {
            Hbasecells.add(CellUtil.createCell("555".getBytes(), "e".getBytes(), ("col"+i).getBytes(), System.currentTimeMillis(), (byte) 4, "val".getBytes()));
        }
        client.SetValues(Hbasecells);

        Hbasecells.clear();
        for (int i = 0; i < 100; i++) {
            Hbasecells.add(CellUtil.createCell("444".getBytes(), "e".getBytes(), ("col"+i).getBytes(), System.currentTimeMillis(), (byte) 4, "val".getBytes()));
        }
        client.SetValues(Hbasecells);

        Hbasecells.clear();
        for (int i = 0; i < 100; i++) {
            Hbasecells.add(CellUtil.createCell("333".getBytes(), "e".getBytes(), ("col"+i).getBytes(), System.currentTimeMillis(), (byte) 4, "val".getBytes()));
        }
        client.SetValues(Hbasecells);

        Hbasecells.clear();
        for (int i = 0; i < 100; i++) {
            Hbasecells.add(CellUtil.createCell("222".getBytes(), "e".getBytes(), ("col"+i).getBytes(), System.currentTimeMillis(), (byte) 4, "val".getBytes()));
        }
        client.SetValues(Hbasecells);

        Hbasecells.clear();
        for (int i = 0; i < 100; i++) {
            Hbasecells.add(CellUtil.createCell("111".getBytes(), "e".getBytes(), ("col"+i).getBytes(), System.currentTimeMillis(), (byte) 4, "val".getBytes()));
        }
        client.SetValues(Hbasecells);

        Hbasecells.clear();
        for (int i = 0; i < 2000; i++) {
            Hbasecells.add(CellUtil.createCell("100".getBytes(), "e".getBytes(), ("col"+i).getBytes(), System.currentTimeMillis(), (byte) 4, "val".getBytes()));
        }
        af = client.SetValues(Hbasecells);

        CacheServer.SetValuesResponse r = af.get();
        System.out.println(r.getCode());
        ListenableFuture<CacheServer.GetRowResponse> future = client.GetRow("666".getBytes());
        future.addListener(()->{
            try {
                CacheServer.GetRowResponse res =  future.get();
                System.out.println(res.getCode());
                System.out.println(res.getMessage());
                List<CacheServer.HCell> cells = res.getResultList();
                cells.forEach((c) -> {
                    System.out.println(c.getRow());
                    System.out.println(c.getColumnFamily());
                    System.out.println(c.getColumn());
                    System.out.println(c.getValue());
                });
            }catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, MoreExecutors.directExecutor());
        future.get();
        Thread.sleep(5000);
    }
}
