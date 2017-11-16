package com.microsoft.azure.storage.pipeline;


//import static org.apache.log4j.Level.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import static com.microsoft.azure.storage.pipeline.LogLevel.ERROR;
import static com.microsoft.azure.storage.pipeline.LogLevel.FATAL;
import static com.microsoft.azure.storage.pipeline.LogLevel.INFO;

public final class PipelineLogger implements ILogRequest {

    private LogLevel minLogLevel;

    private Logger logger;

    public PipelineLogger(LogLevel minLogLevel) {
        this.minLogLevel = minLogLevel;
        this.logger = Logger.getGlobal();
    }

    @Override
    public LogLevel minimumLevelToLog() {
        return this.minLogLevel;
    }

    @Override
    public void logRequest(LogLevel logLevel, String message) {
        switch (logLevel) {
            case ERROR:
            case FATAL:
                this.logger.severe(message);
                break;
            case WARNING:
                this.logger.warning(message);
                break;
            case INFO:
                this.logger.info(message);
                break;
            default:
                break;
        }
    }

    private static String formatLogEntry(String format, Object... args) {
        return String.format(format, args);//.replace('\n', '.');
    }
}
