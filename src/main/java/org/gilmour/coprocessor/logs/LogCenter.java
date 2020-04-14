package org.gilmour.coprocessor.logs;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class LogCenter {
    static private List<String> logs = Collections.synchronizedList(new LinkedList<>());

    public static List<String> getLogs() {
        return logs;
    }

    public static void appendLog(String log) {
        logs.add(log);
    }
}
