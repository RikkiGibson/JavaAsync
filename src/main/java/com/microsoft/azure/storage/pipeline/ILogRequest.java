package com.microsoft.azure.storage.pipeline;

//import java.util.concurrent.Callable;

public interface ILogRequest {

    public LogLevel minimumLevelToLog();

    public void logRequest(LogLevel logLevel, String message);
}
