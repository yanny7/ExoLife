package com.yanny.utils;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Log {
    @NotNull private static final Map<Module, Logger> loggerMap = new HashMap<>();

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tF %1$tT %4$s %2$s %5$s%6$s%n");
    }

    private Log() {
    }

    @NotNull
    public static Logger log(@NotNull Module module) {
        Logger logger = loggerMap.get(module);

        if (logger == null) {
            logger = Logger.getLogger(module.name);
            loggerMap.put(module, logger);
        }

        return logger;
    }
}
