package org.gilmour.coprocessor.logs;

public class Logger {
    private final static String errorPrefix = "[Error]:";
    public void info(String s) {
        LogCenter.appendLog(s);
    }
    public void error(String s) {
        LogCenter.appendLog(errorPrefix + s);
    }
}
