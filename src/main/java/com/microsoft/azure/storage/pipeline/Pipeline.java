package com.microsoft.azure.storage.pipeline;

import com.microsoft.rest.v2.LogLevel;
import com.microsoft.rest.v2.RestClient;
import com.microsoft.rest.v2.policy.RequestPolicy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;

public final class Pipeline {

    private ShouldLogCallable shouldLogFunc;
    private LogRequestCallable logFunc;

    private List<RequestPolicy.Factory> customRequestPolicyFactories = new ArrayList<>();

    public Pipeline(ShouldLogCallable shouldLogFunc, LogRequestCallable logFunc) {
        this.shouldLogFunc = shouldLogFunc;
        this.logFunc = logFunc;
    }
    public boolean shouldLog(Level loggingLevel) {
        try {
            return this.shouldLogFunc.shouldLog(loggingLevel);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    public void Log(Level loggingLevel, String message) {
        try {
            this.logFunc.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a custom RequestPolicy.Factory to the request pipeline in addition to the standard policies.
     *
     * @param factory The Factory producing a custom user-defined RequestPolicy.
     * @return the builder itself for chaining
     */
    public Pipeline addRequestPolicy(RequestPolicy.Factory factory) {
        customRequestPolicyFactories.add(factory);
        return this;
    }

    public RestClient build() {
        return null;
    }

    public void Invoke() {

    }
}
