package com.teremok.framework.util;

import com.badlogic.gdx.Gdx;
import com.teremok.influence.Influence;
import com.teremok.influence.model.Settings;

/**
 * Created by Alexx on 10.02.14
 */
public class Vibrator {

    private static final int NORMAL_DURATION = 200;

    public static void bzz() {
        Settings settings = ((Influence)Gdx.app.getApplicationListener()).getSettings();
        if (settings.vibrate)
            Gdx.input.vibrate(NORMAL_DURATION);
    }

    public static void bzz(int mills) {
        Settings settings = ((Influence)Gdx.app.getApplicationListener()).getSettings();
        if (settings.vibrate)
            Gdx.input.vibrate(mills);
    }
}
