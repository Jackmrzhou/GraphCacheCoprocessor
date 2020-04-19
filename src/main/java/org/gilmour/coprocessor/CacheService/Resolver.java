package org.gilmour.coprocessor.CacheService;

import java.io.IOException;
import java.net.InetSocketAddress;

public interface Resolver {
    InetSocketAddress resolve(String serviceName) throws IOException;
}
