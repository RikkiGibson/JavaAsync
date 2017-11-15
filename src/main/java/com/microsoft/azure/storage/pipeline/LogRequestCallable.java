package com.microsoft.azure.storage.pipeline;

import java.util.concurrent.Callable;
import java.util.logging.Level;

public class LogRequestCallable implements Callable<Void>{

    public void logRequest(Level loggingLevel, String message) {

    }

    @Override
    public Void call() throws Exception {
        return null;
    }
}
