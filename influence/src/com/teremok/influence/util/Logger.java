package com.teremok.influence.util;

import com.teremok.influence.model.Settings;

/**
 * Created by Alexx on 10.02.14
 */
public class Logger {

    private static final String code = "INF_LOG -- ";

    public static void log(String message) {
        if (Settings.debug)
            System.out.println(code + message);
    }

    public static void append(String message) {
        if (Settings.debug)
            System.out.print(message);
    }
}
