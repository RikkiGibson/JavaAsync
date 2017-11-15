package com.microsoft.azure.storage.pipeline;

import java.util.concurrent.Callable;
import java.util.logging.Level;

public class ShouldLogCallable implements Callable<Boolean>{

    public boolean shouldLog(Level loggingLevel) {
        return true;
    }

    @Override
    public Boolean call() throws Exception {
        return true;
    }
}
