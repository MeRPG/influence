package com.teremok.influence.util;

import com.badlogic.gdx.audio.Sound;
import com.teremok.influence.Influence;

/**
 * Created by Alexx on 14.01.14
 */
public class FXPlayer {

    static final String CLICK = "click";
    static final String WIN = "win";
    static final String LOSE = "lose";
    static final String WIN_MATCH = "winMatch";
    static final String LOSE_MATCH = "loseMatch";

    Influence game;

    public FXPlayer(Influence game) {
        this.game = game;
    }

    public void playClick() {
        play(CLICK);
    }

    public void playWin() {
        play(WIN);
    }

    public void playLose() {
        play(LOSE);
    }

    public void playWinMatch() {
        play(WIN_MATCH);
    }

    public void playLoseMatch() {
        play(LOSE_MATCH);
    }

    private void play(String soundName) {
        if (game.getSettings().sound) {
            Sound sound = game.getResourceManager().getSound(soundName);
            sound.play();
        }
    }
}
