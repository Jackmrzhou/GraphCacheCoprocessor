package org.gilmour.coprocessor.CacheService;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;

public class HttpResolver implements Resolver{
    public InetSocketAddress resolve(String serviceName) throws IOException{
        String target = "http://localhost:8500/v1/catalog/service/" + serviceName;
        URL url = new URL(target);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        if (connection.getResponseCode() == 200) {
            Gson gson = new Gson();
            InputStream is = connection.getInputStream();
            // 封装输入流is，并指定字符集
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            // 存放数据
            StringBuffer sbf = new StringBuffer();
            String temp = null;
            while ((temp = br.readLine()) != null) {
                sbf.append(temp);
                sbf.append("\r\n");
            }
            JsonObject json = gson.fromJson(sbf.toString(), JsonArray.class).get(0).getAsJsonObject();
            String addr = json.get("Address").getAsString();
            int port = json.get("ServicePort").getAsInt();
            return new InetSocketAddress(addr, port);
        }else {
            //todo
            return null;
        }
    }
}
