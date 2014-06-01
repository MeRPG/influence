package com.teremok.influence.model;

import com.teremok.influence.controller.SettingsSaver;

/**
 * Created by Alexx on 07.02.14
 */
public class Settings {

    public static final float FAST = 0.25f;
    public static final float NORMAL = 0.4f;
    public static final float SLOW = 0.6f;

    public static boolean sound;
    public static boolean vibrate;
    public static float speed;

    public static int lastAboutScreen = 1;

    public static boolean debug = false;

    public static GameSettings gameSettings;

    public static void reset() {
        sound = true;
        vibrate = true;
        speed = NORMAL;
        debug = false;
        gameSettings = GameSettings.getDefault();
    }

    public static void init() {
        SettingsSaver.checkDirs();
        reset();
        if (! SettingsSaver.load())
            reset();
    }
}
