package com.teremok.influence.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alexx on 10.01.14
 */
public class Localizator {

    public static final String LANGUAGE_RUSSIAN = "russian";
    public static final String LANGUAGE_ENGLISH = "english";

    private static String language;

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

    public static void setDefaultLanguage() {
        language = LANGUAGE_ENGLISH;
    }

    public static void setEnglishLanguage() {
        language = LANGUAGE_ENGLISH;
    }

    public static void setRussianLanguage() {
        language = LANGUAGE_RUSSIAN;
    }

    public static void setLanguage(String language) {
        if (language == null) {
            setDefaultLanguage();
            return;
        }

        if (language.equals(LANGUAGE_ENGLISH) || language.equals(LANGUAGE_RUSSIAN)) {
            Localizator.language = language;
        } else {
            setDefaultLanguage();
        }
    }

    // Auto-generated

    public static String getLanguage() {
        return language;
    }

}
