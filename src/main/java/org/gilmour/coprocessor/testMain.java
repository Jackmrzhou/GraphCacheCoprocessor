package org.gilmour.coprocessor;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.gilmour.coprocessor.CacheService.CacheServer;
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
        ClientCreator creator = new ClientCreator();
        CacheServiceClient client = creator.CreateClient(new HttpResolver());
        List<Cell> Hbasecells = new LinkedList<>();
        Hbasecells.add(CellUtil.createCell("666".getBytes(), "i".getBytes(), "col".getBytes(), System.currentTimeMillis(), (byte)4, "val".getBytes()));
        ListenableFuture<CacheServer.SetValuesResponse> af = client.SetValues(Hbasecells);
        CacheServer.SetValuesResponse r = af.get();
        System.out.println(r.getCode());
        ListenableFuture future = client.GetRow("666".getBytes());
        future.addListener(()->{
            try {
                CacheServer.GetRowResponse res = (CacheServer.GetRowResponse) future.get();
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
