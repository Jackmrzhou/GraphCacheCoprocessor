package org.gilmour.coprocessor.web;

import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;


public class WebConsole implements Runnable{
    private static Logger logger = LoggerFactory.getLogger(WebConsole.class);
    @Override
    public void run() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.createContext("/", new Handler());
            server.start();
        } catch (IOException e){
            logger.error(e.getMessage());
        }
    }
}
