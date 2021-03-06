package com.teremok.influence.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.teremok.influence.model.Settings;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Calendar;

import static com.teremok.influence.util.IOConstants.LOG_PATH;

/**
 * Created by Alexx on 10.02.14
 */
public class Logger {

    private static final String CODE = "INF_LOG -- ";

    private static PrintStream printer;

    public static void init() {
        openFile();
    }

    public static void log(String message) {
        if (Settings.debug) {
            System.out.println(CODE + message);
            if (printer!=null) {
                printer.println(message);
            }
        }
    }

    public static void append(String message) {
        if (Settings.debug) {
            System.out.print(message);
            if (printer!=null) {
                printer.print(message);
            }
        }
    }

    private static void openFile() {
        if (! Settings.debug)
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
