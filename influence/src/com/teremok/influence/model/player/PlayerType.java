package com.teremok.influence.model.player;

import com.badlogic.gdx.Gdx;

/**
 * Created by Alexx on 07.02.14
 */
public enum PlayerType {
        Human ("Human"),
        Random ("Random"),
        Dummy ("Dummy"),
        Lazy ("Lazy"),
        Beefy ("Beefy"),
        Smarty ("Smarty"),
        Hunter ("Hunter");


    PlayerType(String s) {
        Gdx.app.debug(getClass().getSimpleName(), "player type " + s + " created.");
    }
}
