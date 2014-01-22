package com.teremok.influence.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alexx on 10.01.14
 */
public class Localizator {

    private static Map<String, String> strings;

    static  {
        strings = new HashMap<String, String>();
        strings.put("selectYourCell", "Touch a cell with your ");
        strings.put("selectYourCellGreen", "Touch a cell with your ");
        strings.put("selectMoreThanOne", "Select a cell with 2+ power.");
        strings.put("touchNearby", "Touch a nearby cell to attack.");
        strings.put("waitYourMove", "Wait your move.");
        strings.put("youWon", "You won! Touch here to start new game.");
        strings.put("youLost", "You lost! Touch here to start new game.");
        strings.put("multiplayer", "Multiplayer");
        strings.put("singleplayer", "Singleplayer");
        strings.put("resume", "Resume");
    }

    public static String getString(String key) {
        return strings.get(key);
    }
}
