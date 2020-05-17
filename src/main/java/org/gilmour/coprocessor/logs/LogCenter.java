package org.gilmour.coprocessor.logs;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class LogCenter {
    static private List<String> logs = Collections.synchronizedList(new LinkedList<>());
    // only lasted 1000 logs will be preserved
    static private int threshold = 1000;
    public static List<String> getLogs() {
        return logs;
    }

    public static void appendLog(String log) {
        logs.add(log);
        if (logs.size() > threshold){
            logs.remove(0);
        }
    }
}
