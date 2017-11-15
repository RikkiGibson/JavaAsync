package com.microsoft.azure.storage.pipeline;

//import org.apache.log4j.Logger;
//import org.apache.log4j.helpers.SyslogWriter;
//import org.apache.log4j.net.SyslogAppender;

//import static org.apache.log4j.Level.ERROR;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class DefaultSysLogWriter {

    private Level level;//.DefaultWindowsEventLogWriter
//    private SyslogWriter syslogWriter = null;
    private static Logger logger = null;
//
     public static void Initialize() {
         if (logger == null) {
             logger = Logger.getAnonymousLogger();
         }
//        SyslogAppender syslogAppender = new SyslogAppender();
//        syslogAppender.setThreshold(ERROR);
//        Logger.getRootLogger().addAppender(syslogAppender);
//
    }
}
