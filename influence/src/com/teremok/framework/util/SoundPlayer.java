package com.teremok.framework.util;

import com.badlogic.gdx.audio.Sound;
import com.teremok.framework.TeremokGame;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Алексей on 08.07.2014
 */
public class SoundPlayer {

    TeremokGame game;
    Map<String, Sound> sounds;


    public SoundPlayer(TeremokGame game) {
        this.game = game;
        sounds = new HashMap<>(10);
    }

    public void play(String soundName) {
        Sound sound = sounds.get(soundName);
        if (sound == null) {
            sound = game.getResourceManager().getSound(soundName);
            sounds.put(soundName, sound);
        }
        sound.play();
    }
}
