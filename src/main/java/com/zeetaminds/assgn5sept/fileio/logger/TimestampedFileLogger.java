package com.zeetaminds.assgn5sept.fileio.logger;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimestampedFileLogger extends FileLogger {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public TimestampedFileLogger(String fileName) {
        super(fileName);
    }

    @Override
    public void log(String message) {
        String timestampedMessage = "[" + sdf.format(new Date()) + "] " + message;
        super.log(timestampedMessage);
    }
}
