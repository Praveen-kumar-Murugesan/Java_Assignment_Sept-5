package com.zeetaminds.assgn5sept.fileio.logger;

public class LoggerDemo {
    public static void main(String[] args) {
        Logger fileLogger = new FileLogger("/home/pk/IdeaProjects/Java-Assignment/src/main/java/com/zeetaminds/assgn5sept/fileio/logger/fileLogger.txt");
        LoggerManager fileLoggerManager = new LoggerManager(fileLogger);
        fileLoggerManager.logMessage("FileLogger: This is a simple file log message.");

        Logger timestampedFileLogger = new TimestampedFileLogger("/home/pk/IdeaProjects/Java-Assignment/src/main/java/com/zeetaminds/assgn5sept/fileio/logger/timestampedFileLogger.txt");
        LoggerManager timestampedFileLoggerManager = new LoggerManager(timestampedFileLogger);
        timestampedFileLoggerManager.logMessage("TimestampedFileLogger: This is a file log message with a timestamp.");

        Logger consoleLogger = new ConsoleLogger();
        LoggerManager consoleLoggerManager = new LoggerManager(consoleLogger);
        consoleLoggerManager.logMessage("ConsoleLogger: This is a log message displayed on the console.");
    }
}
