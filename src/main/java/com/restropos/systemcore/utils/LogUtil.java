package com.restropos.systemcore.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtil {

    public static void printLog(String message,Class className){
        Logger logger = LoggerFactory.getLogger(className);
        logger.error(message);
    }
}
