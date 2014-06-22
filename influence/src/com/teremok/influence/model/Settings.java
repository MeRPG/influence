package com.teremok.influence.model;

/**
 * Created by Alexx on 07.02.14
 */
public class Settings {

    public final float FAST = 0.25f;
    public final float NORMAL = 0.4f;
    public final float SLOW = 0.6f;

    public boolean sound = true;
    public boolean vibrate = true;
    public float speed = NORMAL;

    public int lastAboutScreen = 1;

    public boolean debug = true;

    public GameSettings gameSettings = GameSettings.getDefault();

    public Settings reset() {
        sound = true;
        vibrate = true;
        speed = NORMAL;
        debug = true;
        gameSettings = GameSettings.getDefault();
        return this;
    }
}
