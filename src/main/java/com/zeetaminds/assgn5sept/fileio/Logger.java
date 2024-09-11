package com.zeetaminds.assgn5sept.fileio;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Logger {
    public abstract void log(String message);
}

class FileLogger extends Logger {
    private String fileName;

    public FileLogger(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void log(String message) {
        try (FileWriter fileWriter = new FileWriter(fileName);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ConsoleLogger extends Logger {

    @Override
    public void log(String message) {
        System.out.println(message);
    }
}

class LoggerManager {
    private Logger logger;

    public LoggerManager(Logger logger) {
        this.logger = logger;
    }

    public void logMessage(String message) {
        logger.log(message);
    }
}

class TimestampedFileLogger extends FileLogger {
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

class LoggerDemo {
    public static void main(String[] args) {
        Logger fileLogger = new FileLogger("/home/pk/IdeaProjects/Java-Assignment/src/main/java/com/zeetaminds/assgn5sept/fileio/fileLogger.txt");
        LoggerManager fileLoggerManager = new LoggerManager(fileLogger);
        fileLoggerManager.logMessage("FileLogger: This is a simple file log message.");

        Logger timestampedFileLogger = new TimestampedFileLogger("/home/pk/IdeaProjects/Java-Assignment/src/main/java/com/zeetaminds/assgn5sept/fileio/timestampedFileLogger.txt");
        LoggerManager timestampedFileLoggerManager = new LoggerManager(timestampedFileLogger);
        timestampedFileLoggerManager.logMessage("TimestampedFileLogger: This is a file log message with a timestamp.");

        Logger consoleLogger = new ConsoleLogger();
        LoggerManager consoleLoggerManager = new LoggerManager(consoleLogger);
        consoleLoggerManager.logMessage("ConsoleLogger: This is a log message displayed on the console.");
    }
}