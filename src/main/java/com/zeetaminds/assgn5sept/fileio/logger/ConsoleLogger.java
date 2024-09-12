package com.zeetaminds.assgn5sept.fileio.logger;

public class ConsoleLogger extends Logger {
    @Override
    public void log(String message) {
        System.out.println(message);
    }
}
