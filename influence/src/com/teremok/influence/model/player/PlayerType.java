package com.teremok.influence.model.player;

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
        //Logger.log("player type " + s + " created.");
    }
}
