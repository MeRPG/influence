package com.teremok.influence.util;

import com.teremok.framework.util.SoundPlayer;
import com.teremok.influence.Influence;

/**
 * Created by Alexx on 14.01.14
 */
// TODO: вынести во фреймворк
public class FXPlayer extends SoundPlayer {

    static final String CLICK = "click";
    static final String WIN = "win";
    static final String LOSE = "lose";
    static final String WIN_MATCH = "winMatch";
    static final String LOSE_MATCH = "loseMatch";

    public FXPlayer(Influence game) {
        super(game);
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
}
