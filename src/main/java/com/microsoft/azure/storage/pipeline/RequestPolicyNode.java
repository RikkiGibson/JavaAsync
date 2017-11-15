package com.microsoft.azure.storage.pipeline;

import com.microsoft.azure.storage.blob.Pipeline;
import com.microsoft.rest.v2.http.HttpRequest;
import com.microsoft.rest.v2.http.HttpResponse;
import com.microsoft.rest.v2.policy.RequestPolicy;
//import org.apache.log4j.Level;
//import org.apache.log4j.Logger;
import rx.Single;

import java.util.logging.Level;

public final class RequestPolicyNode {
    // A Node represents a node in a linked-list of Policy objects. A Node is passed
// to the Factory's New method which passes to the Policy object it creates. The Policy object
// uses the Node to forward the Context and HTTP request to the next Policy object in the pipeline.
        Pipeline pipeline;
        RequestPolicy next;

    /**
     * Sends an HTTP request as an asynchronous operation.
     *
     * @param request The HTTP request message to send.
     * @return The rx.Single instance representing the asynchronous operation.
     */
    Single<HttpResponse> sendAsync(HttpRequest request) {
        return next.sendAsync(request);
    }

    public boolean ShouldLog(Level loggingLevel) {
        //loggingLevel.
        return true;
//        if (loggingLevel.equals(Level.ERROR)) {
//            return true;
//        }
//
//        return Logger.getRootLogger().getLevel().isGreaterOrEqual(loggingLevel);
    }

    public void Log(Level loggingLevel, String message) {
//        Logger defaultLogger = null;
//        if (loggingLevel.equals(Level.ERROR)) {
//            defaultLogger.error(message);
//        }
//
//        //Logger logger
//        if (ShouldLog(loggingLevel)) {
//            logger.write();
//        }
    }
//
//    // WouldLog returns true if the specified severity level would be logged.
//    func (n *Node) WouldLog(severity LogSeverity) bool {
//        minimum := LogNone
//        if n.pipeline.options.Log.MinimumSeverityToLog != nil {
//            minimum = n.pipeline.options.Log.MinimumSeverityToLog()
//        }
//        return severity <= minimum
//    }
//
//    // Log logs a string to the Pipeline's Logger.
//    func (n *Node) Log(severity LogSeverity, msg string) {
//        if !n.WouldLog(severity) {
//            return // Short circuit message formatting if we're not logging it
//        }
//        if len(msg) == 0 || msg[len(msg)-1] != '\n' {
//            msg += "\n" // Ensure trailing newline
//        }
//        defaultLog(severity, msg)
//        n.pipeline.options.Log.Log(severity, msg)
//        // If logger doesn't handle fatal/panic, we'll do it here.
//        if severity == LogFatal {
//            os.Exit(1)
//        } else if severity == LogPanic {
//            panic(msg)
//        }
    //}
}
