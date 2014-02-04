package com.teremok.influence.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alexx on 10.01.14
 */
public class Localizator {

    public static final String LANGUAGE_RUSSIAN = "ru";
    public static final String LANGUAGE_ENGLISH = "en";

    private static String language;

    private static Map<String, Map<String, String>> dictionary;

    static  {
        Map<String, String> stringsEnglish;
        stringsEnglish = new HashMap<String, String>();
        stringsEnglish.put("selectYourCell", "Touch a cell ");
        stringsEnglish.put("ofYourColor", "of your color");
        stringsEnglish.put("yourCells", "your cells");
        stringsEnglish.put("selectMoreThanOne", "Select a cell with 2+ power");
        stringsEnglish.put("touchNearby", "Touch a nearby cell to attack");
        stringsEnglish.put("touchToDistribute", "Distribute power to ");
        stringsEnglish.put("touchToEndTurn", "Touch to end turn");
        stringsEnglish.put("orTouchToEndAttack", "(or touch here to end attack)");
        stringsEnglish.put("orTouchToEndTurn", "(or touch here to end turn)");
        stringsEnglish.put("waitYourMove", "Wait your move");
        stringsEnglish.put("youWon", "You won!");
        stringsEnglish.put("youLost", "You lost!");
        stringsEnglish.put("touchForNewGame", "Touch here to start new game.");
        stringsEnglish.put("singleplayer", "Singleplayer");
        stringsEnglish.put("multiplayer", "Play with friend");
        stringsEnglish.put("resume", "Resume");
        stringsEnglish.put("pause", "Pause");

        Map<String, String> stringsRussian;
        stringsRussian = new HashMap<String, String>();
        stringsRussian.put("selectYourCell", "Коснитесь клетки ");
        stringsRussian.put("ofYourColor", "вашего цвета");
        stringsRussian.put("yourCells", "своим клеткам");
        stringsRussian.put("selectMoreThanOne", "Выберите клетку с 2+ силы");
        stringsRussian.put("touchNearby", "Коснитесь соседней клетки для атаки");
        stringsRussian.put("touchToDistribute", "Раздайте силу ");
        stringsRussian.put("touchToEndTurn", "Коснитесь, чтобы завершить ход");
        stringsRussian.put("orTouchToEndAttack", "(коснитесь здесь, чтобы завершить ход)");
        stringsRussian.put("orTouchToEndTurn", "(коснитесь здесь, чтобы завершить ход)");
        stringsRussian.put("waitYourMove", "Ожидайте своего хода");
        stringsRussian.put("youWon", "Победа!");
        stringsRussian.put("youLost", "Поражение!");
        stringsRussian.put("touchForNewGame", "Коснитесь, чтобы начать новую игру.");
        stringsRussian.put("singleplayer", "Одиночная игра");
        stringsRussian.put("multiplayer", "Игра с другом");
        stringsRussian.put("resume", "Продолжить");
        stringsRussian.put("pause", "Пауза");

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
