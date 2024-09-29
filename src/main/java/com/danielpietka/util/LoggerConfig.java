package com.danielpietka.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerConfig {
    private static final Logger logger = Logger.getLogger(LoggerConfig.class.getName());
    private static boolean isConfigured = false;

    public static void setupLogger() {
        if (isConfigured) {
            return;
        }

        try {
            Logger rootLogger = Logger.getLogger("");
            rootLogger.setLevel(Level.ALL);

            if (needsHandler(rootLogger, ConsoleHandler.class)) {
                ConsoleHandler consoleHandler = new ConsoleHandler();
                consoleHandler.setLevel(Level.ALL);
                consoleHandler.setFormatter(new SimpleFormatter());
                rootLogger.addHandler(consoleHandler);
            }

            String logDir = "logs";
            File logDirectory = new File(logDir);
            if (!logDirectory.exists()) {
                if (logDirectory.mkdir()) {
                    logger.info("Log directory created: " + logDir);
                } else {
                    logger.warning("Failed to create log directory: " + logDir);
                }
            }

            if (needsHandler(rootLogger, FileHandler.class)) {
                String dateStr = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
                String logFileName = logDir + "/app_" + dateStr + ".log";
                FileHandler fileHandler = new FileHandler(logFileName, true);
                fileHandler.setLevel(Level.ALL);
                fileHandler.setFormatter(new SimpleFormatter());
                rootLogger.addHandler(fileHandler);
            }

            logger.info("Logger successfully configured.");
            isConfigured = true;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to set up logger: ", e);
        }
    }

    private static boolean needsHandler(Logger logger, Class<? extends Handler> handlerClass) {
        for (Handler handler : logger.getHandlers()) {
            if (handlerClass.isInstance(handler)) {
                return false;
            }
        }
        return true;
    }
}
