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

    private static Map<String, Map<String, String>> dictionary;

    static  {
        Map<String, String> stringsEnglish;
        stringsEnglish = new HashMap<String, String>();
        stringsEnglish.put("selectYourCell", "Touch a cell ");
        stringsEnglish.put("ofYourColor", "of your color");
        stringsEnglish.put("selectMoreThanOne", "Select a cell with 2+ power");
        stringsEnglish.put("touchNearby", "Touch a nearby cell to attack");
        stringsEnglish.put("waitYourMove", "Wait your move");
        stringsEnglish.put("youWon", "You won! Touch here to start new game");
        stringsEnglish.put("youLost", "You lost! Touch here to start new game");
        stringsEnglish.put("singleplayer", "Singleplayer");
        stringsEnglish.put("multiplayer", "Multiplayer");
        stringsEnglish.put("resume", "Resume");

        Map<String, String> stringsRussian;
        stringsRussian = new HashMap<String, String>();
        stringsRussian.put("selectYourCell", "Коснитесь клетки ");
        stringsRussian.put("ofYourColor", "вашего цвета");
        stringsRussian.put("selectMoreThanOne", "Выберите клетку с 2+ силы");
        stringsRussian.put("touchNearby", "Коснитесь соседней клетки, чтобы атаковать");
        stringsRussian.put("waitYourMove", "Ожидайте своего хода");
        stringsRussian.put("youWon", "Победа! Коснитесь, чтобы начать новую игру.");
        stringsRussian.put("youLost", "Поражение! Коснитесь, чтобы начать новую игру.");
        stringsRussian.put("singleplayer", "Одиночная игра");
        stringsRussian.put("multiplayer", "Игра с другом");
        stringsRussian.put("resume", "Продолжить");

        dictionary = new HashMap<String, Map<String, String>>();
        dictionary.put(LANGUAGE_ENGLISH, stringsEnglish);
        dictionary.put(LANGUAGE_RUSSIAN, stringsRussian);
    }

    public static String getString(String key) {
        if (language == null)
            setDefaultLanguage();
        return dictionary.get(language).get(key);
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

    public static void switchLanguage() {
        if (language.equals(LANGUAGE_ENGLISH)) {
            setRussianLanguage();
        } else {
            setEnglishLanguage();
        }
    }

    // Auto-generated

    public static String getLanguage() {
        return language;
    }

}
