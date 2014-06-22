package com.teremok.influence.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.teremok.influence.Influence;
import com.teremok.influence.model.Settings;

import java.util.HashMap;

/**
 * Created by Alexx on 14.01.14
 */
public class FXPlayer {

    static final String CLICK = "click";
    static final String WIN = "win";
    static final String LOSE = "lose";
    static final String WIN_MATCH = "winMatch";
    static final String LOSE_MATCH = "loseMatch";

    static HashMap<String, Sound> sounds;

    static Settings settings = ((Influence)Gdx.app.getApplicationListener()).getSettings();

    public static void playClick() {
        if (settings.sound && sounds != null) {
            sounds.get(CLICK).play();
        }
    }

    public static void playWin() {
        if (settings.sound && sounds != null) {
            sounds.get(WIN).play();
        }
    }

    public static void playLose() {
        if (settings.sound && sounds != null) {
            sounds.get(LOSE).play();
        }
    }

    public static void playWinMatch() {
        if (settings.sound && sounds != null) {
            sounds.get(WIN_MATCH).play();
        }
    }

    public static void playLoseMatch() {
        if (settings.sound && sounds != null) {
            sounds.get(LOSE_MATCH).play();
        }
    }

    public static void dispose() {
        for (Sound sound : sounds.values()) {
            sound.dispose();
        }
        sounds.clear();
    }

    public static void load() {
        if (sounds == null) {
            sounds = new HashMap<>();
        }
        load(CLICK);
        load(WIN);
        load(LOSE);
        load(WIN_MATCH);
        load(LOSE_MATCH);
    }

    private static void load(String name) {
        Sound sound = sounds.get(name);
        if (sound != null)
            sound.dispose();
        sounds.put(name, Gdx.audio.newSound(Gdx.files.internal("sound/" + name+".mp3")));
    }


}
