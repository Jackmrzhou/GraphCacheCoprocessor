package org.gilmour.coprocessor.CacheService;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.protobuf.ByteString;
import io.grpc.Channel;
import org.apache.hadoop.hbase.Cell;
import org.gilmour.coprocessor.CacheService.generated.CacheServer;
import org.gilmour.coprocessor.CacheService.generated.CacheServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CacheServiceClient {
    private final static Logger logger = LoggerFactory.getLogger(CacheServiceClient.class);
    private final CacheServiceGrpc.CacheServiceBlockingStub blockingStub;
    private final CacheServiceGrpc.CacheServiceFutureStub futureStub;
    public CacheServiceClient(Channel channel) {
        blockingStub = CacheServiceGrpc.newBlockingStub(channel);
        futureStub = CacheServiceGrpc.newFutureStub(channel);
    }

    private CacheServer.HCell cellConverter(Cell c) {
        CacheServer.HCell.Builder builder = CacheServer.HCell.newBuilder();
        builder.setRow(ByteString.copyFrom(c.getRowArray(), c.getRowOffset(), c.getRowLength()));
        builder.setColumn(ByteString.copyFrom(c.getQualifierArray(), c.getQualifierOffset(), c.getQualifierLength()));
        builder.setValue(ByteString.copyFrom(c.getValueArray(), c.getValueOffset(), c.getValueLength()));
        builder.setColumnFamily(ByteString.copyFrom(c.getFamilyArray(), c.getFamilyOffset(), c.getFamilyLength()));
        builder.setTimestamp(c.getTimestamp());
        builder.setType(c.getType().getCode());
        return builder.build();
    }

    public ListenableFuture<CacheServer.SetValuesResponse> SetValues(List<Cell> cells) {
        CacheServer.SetValuesRequest.Builder builder = CacheServer.SetValuesRequest.newBuilder();
        for (Cell cell : cells) {
            builder.addCells(cellConverter(cell));
        }
        ListenableFuture<CacheServer.SetValuesResponse> res = futureStub.setValues(builder.build());
        return res;
    }

    public ListenableFuture<CacheServer.GetRowResponse> GetRow(byte[] rowKey) {
        CacheServer.GetRowRequest.Builder builder = CacheServer.GetRowRequest.newBuilder();
        builder.setKey(ByteString.copyFrom(rowKey));
        return futureStub.getRow(builder.build());
    }
}
