package com.teremok.influence.model;

import com.teremok.influence.util.Logger;

/**
 * Created by Alexx on 27.02.14
 */
public enum GameDifficulty {
    EASY ("EASY"),
    NORMAL ("NORMAL"),
    HARD ("HARD"),
    INSANE ("INSANE"),
    CUSTOM ("CUSTOM");

    GameDifficulty(String string) {
        Logger.log("game difficulty " + string + " created.");
    }
}
