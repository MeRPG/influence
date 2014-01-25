package com.teremok.influence.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/**
 * Created by Alexx on 14.01.14
 */
public class FXPlayer {

    static Sound click;
    static Sound win;
    static Sound lose;

    public static void playClick() {
        if (click == null)
            click = Gdx.audio.newSound(Gdx.files.internal("click.mp3"));
        click.play();
    }

    public static void playWin() {
        if (win == null)
            win = Gdx.audio.newSound(Gdx.files.internal("win.mp3"));
        win.play();
    }

    public static void playLose() {
        if (lose == null)
            lose = Gdx.audio.newSound(Gdx.files.internal("lose.mp3"));
        lose.play();
    }

    public static void dispose() {
        if (click != null)
            click.dispose();
        if (win != null)
            win.dispose();
        if (lose != null)
            lose.dispose();
    }

    public static void load() {
        click = Gdx.audio.newSound(Gdx.files.internal("click.mp3"));
        win = Gdx.audio.newSound(Gdx.files.internal("win.mp3"));
        lose = Gdx.audio.newSound(Gdx.files.internal("lose.mp3"));
    }


}
