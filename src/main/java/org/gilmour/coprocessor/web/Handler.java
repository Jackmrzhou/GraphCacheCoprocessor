package org.gilmour.coprocessor.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.gilmour.coprocessor.logs.LogCenter;
import org.gilmour.coprocessor.statistic.Aggregation;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Modifier;

public class Handler implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        JsonObject json = new JsonObject();
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(Modifier.TRANSIENT);
        Gson gson = builder.create();
        json.add("statistics", gson.toJsonTree(new Aggregation()));
        json.add("logs", gson.toJsonTree(LogCenter.getLogs()));
        httpExchange.sendResponseHeaders(200, 0);
        httpExchange.getResponseHeaders().add("Content-Type", "application/json");
        OutputStream stream = httpExchange.getResponseBody();
        stream.write(json.toString().getBytes());
        stream.close();
    }
}
