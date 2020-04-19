package org.gilmour.coprocessor.CacheService;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;

public class ClientCreator {
    private final static Logger logger = LoggerFactory.getLogger(ClientCreator.class);
    public ClientCreator() {

    }

    public CacheServiceClient CreateClient(Resolver resolver) {
        try {
            InetSocketAddress address = resolver.resolve("CacheService");
            if (address == null) {
                logger.error("can't resolve the service address");
            }
            ManagedChannel channel = ManagedChannelBuilder.forAddress(address.getHostName(), address.getPort()).usePlaintext().build();
            return new CacheServiceClient(channel);
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }
}
