package com.microsoft.azure.storage.pipeline;


//import static org.apache.log4j.Level.*;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class PipelineLogger {

    private static Level logLevel;

    //private static Logger

    public static void initialize(Level logLevel) {
        PipelineLogger.logLevel = logLevel;
        if (System.getProperty("os.name").contains("windows")) {
            DefaultWindowsEventLogWriter.Initialize();
        }
        else {
            DefaultSysLogWriter.Initialize();
        }
    }

    public static boolean shouldLog(Level logLevel) {
        //return logLevel.isGreaterOrEqual(PipelineLogger.logLevel);
        return true;
    }

//    public static void fatal(String format, Object... args) {
//        Logger logger = Logger.getGlobal();
//        if (shouldLog(Level.FATAL)) {
//            Logger logger = Logger.getRootLogger();
//            logger.fatal(formatLogEntry(format, args));
//        }
//    }

//    public static void error(String format, Object... args) {
//        if (shouldLog(Level.SEVERE)) {
//            Logger logger = Logger.getGlobal().getRootLogger();
//            logger.error(formatLogEntry(format, args));
//        }
//    }
//
//    public static void warn(String format, Object... args) {
//        if (shouldLog(Level.WARNING)) {
//            Logger logger = Logger.getRootLogger();
//            logger.warn(formatLogEntry(format, args));
//        }
//    }
//
//    public static void info(String format, Object... args) {
//        if (shouldLog(Level.INFO)) {
//            Logger logger = Logger.getRootLogger();
//            if (logger.isInfoEnabled()) {
//                logger.info(formatLogEntry(format, args));
//            }
//        }
//    }
//
//    public static void debug(String format, Object... args) {
//        if (shouldLog(Level.DEBUG)) {
//            Logger logger = Logger.getRootLogger();
//            if (logger.isDebugEnabled()) {
//                logger.debug(formatLogEntry(format, args));
//            }
//        }
//    }
//
//    public static void trace(String format, Object... args) {
//        if (shouldLog(Level.TRACE)) {
//            Logger logger = Logger.getRootLogger();
//            if (logger.isTraceEnabled()) {
//                logger.trace(formatLogEntry(format, args));
//            }
//        }
//    }

    private static String formatLogEntry(String format, Object... args) {
        return String.format(format, args);//.replace('\n', '.');
    }
}
