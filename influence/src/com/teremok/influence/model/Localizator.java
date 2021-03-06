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
        dictionary = new HashMap<>();
    }

    public static String getString(String key) {
        if (language == null)
            setDefaultLanguage();
        return dictionary.get(language).get(key);
    }

    public static void setDefaultLanguage() {
        setEnglishLanguage();
    }

    public static void setEnglishLanguage() {
        if (! dictionary.containsKey(LANGUAGE_ENGLISH))
            fillEnglishStrings();
        language = LANGUAGE_ENGLISH;
    }

    public static void setRussianLanguage() {
        if (! dictionary.containsKey(LANGUAGE_RUSSIAN))
            fillRussianStrings();
        language = LANGUAGE_RUSSIAN;
    }

    public static void setLanguage(String language) {
        if (language == null) {
            setDefaultLanguage();
            return;
        }

        if (language.equals(LANGUAGE_RUSSIAN)) {
            setRussianLanguage();
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

    private static void fillRussianStrings() {
        Map<String, String> stringsRussian = new HashMap<>();
        stringsRussian.put("selectYourCell", "Коснитесь клетки ");
        stringsRussian.put("ofYourColor", "вашего цвета");
        stringsRussian.put("yourCells", "своим клеткам");
        stringsRussian.put("selectMoreThanOne", "Выберите клетку с 2+ силы");
        stringsRussian.put("touchNearby", "Коснитесь соседней клетки для атаки");
        stringsRussian.put("touchToPower", "Раздайте силу ");
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
        stringsRussian.put("settings", "Настройки");
        stringsRussian.put("quickGame", "Быстрая игра");
        stringsRussian.put("chooseLanguage", "Выберите язык: ");
        stringsRussian.put("sounds", "Звук");
        stringsRussian.put("vibrate", "Вибрация");
        stringsRussian.put("computerPlayerSpeed", "Скорость ходов");
        stringsRussian.put("touchToEndAttack", "Коснитесь, чтобы завершить атаку");

        dictionary.put(LANGUAGE_RUSSIAN, stringsRussian);
    }


    private static void fillEnglishStrings() {
        Map<String, String> stringsEnglish = new HashMap<>();
        stringsEnglish.put("selectYourCell", "Touch a cell ");
        stringsEnglish.put("ofYourColor", "of your color");
        stringsEnglish.put("yourCells", "your cells");
        stringsEnglish.put("selectMoreThanOne", "Select a cell with 2+ power");
        stringsEnglish.put("touchNearby", "Touch a nearby cell to attack");
        stringsEnglish.put("touchToPower", "Power up ");
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
        stringsEnglish.put("settings", "Settings");
        stringsEnglish.put("quickGame", "Quick play");
        stringsEnglish.put("chooseLanguage", "Choose language: ");
        stringsEnglish.put("sounds", "Sound");
        stringsEnglish.put("vibrate", "Vibrate");
        stringsEnglish.put("computerPlayerSpeed", "Computer speed");
        stringsEnglish.put("touchToEndAttack", "Touch here to end attack");

        dictionary.put(LANGUAGE_ENGLISH, stringsEnglish);
    }

    // Auto-generated

    public static String getLanguage() {
        return language;
    }

}
