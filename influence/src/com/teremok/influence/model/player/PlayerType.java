package com.teremok.influence.model.player;

import com.teremok.influence.util.Logger;

/**
 * Created by Alexx on 07.02.14
 */
public enum PlayerType {
    Undefined (""),
    Dummy ("Dummy"),
    Beefy ("Beefy"),
    Lazy ("Lazy"),
    Smarty ("Smarty"),
    Random ("Random"),
    Hunter ("Hunter"),
    Human ("Human");

    PlayerType(String s) {
        Logger.log("player type " + s + " created.");
    }
}
