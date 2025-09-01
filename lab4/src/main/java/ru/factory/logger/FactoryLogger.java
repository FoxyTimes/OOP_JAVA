package ru.factory.logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FactoryLogger {
    public static Logger logger;
    public static void init() {
        logger = LogManager.getLogger(FactoryLogger.class);
    }
}
