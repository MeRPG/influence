package com.teremok.influence.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Calendar;

import static com.teremok.influence.util.IOConstants.LOG_PATH;

/**
 * Created by Alexx on 10.02.14
 */
//TODO заменить либгдх-логгером
public class Logger {

    private static final String CODE = "INF_LOG -- ";

    private static PrintStream printer;

    private static boolean enabled;

    public static void init(boolean enabled) {
        openFile();
        Logger.enabled = enabled;
    }

    public static void log(String message) {
        if (enabled) {
            System.out.println(CODE + message);
            if (printer!=null) {
                printer.println(message);
            }
        }
    }

    private static void openFile() {
        if (! enabled)
            return;
        FileHandle logFile = Gdx.files.external(LOG_PATH);
        try {
            printer = new PrintStream(new FileOutputStream(logFile.file(), true));
            printer.println(" - - - - - - - - - - ");
            printer.println("Reopened file at" + Calendar.getInstance().getTime());
            printer.println(" - - - - - - - - - - ");
            //System.setErr(printer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
