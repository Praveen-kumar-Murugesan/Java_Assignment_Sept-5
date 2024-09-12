package com.zeetaminds.assgn5sept.fileio.logger;

class LoggerManager {
    private Logger logger;

    public LoggerManager(Logger logger) {
        this.logger = logger;
    }

    public void logMessage(String message) {
        logger.log(message);
    }
}
