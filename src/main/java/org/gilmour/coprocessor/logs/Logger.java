package org.gilmour.coprocessor.logs;

public class Logger {
    public void info(String s) {
        LogCenter.appendLog(s);
    }
}
