package com.teremok.framework.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.I18NBundle;
import com.teremok.framework.TeremokGame;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Alexx on 10.01.14
 */
public class Localizator {

    public static final String LANGUAGE_RUSSIAN = "ru";
    public static final String LANGUAGE_ENGLISH = "en";

    private static TeremokGame game;
    private static String language;

    private static Map<String, I18NBundle> dictionary;

    public static void init(TeremokGame game) {
        Localizator.game = game;
        dictionary = new HashMap<>(2);
        fillEnglishStrings();
        fillRussianStrings();

        switch (game.getLanguage()) {
            case "ru":
            case "uk":
            case "lt":
            case "kk":
                setRussianLanguage();
                break;
            default:
                setEnglishLanguage();
                break;
        }
        Gdx.app.debug(Localizator.class.getSimpleName(), "Use language: " + language);
    }

    public static String getString(String key) {
        if (language == null) {
            setDefaultLanguage();
        }
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

        switch (language) {
            case LANGUAGE_ENGLISH:
                setEnglishLanguage();
                break;
            case LANGUAGE_RUSSIAN:
                setRussianLanguage();
                break;
            default:
                setDefaultLanguage();
        }
    }

    public static void switchLanguage() {
        switch (language) {
            case LANGUAGE_ENGLISH:
                setRussianLanguage();
                break;
            case LANGUAGE_RUSSIAN:
                setEnglishLanguage();
                break;
        }
    }

    private static void fillEnglishStrings() {
        I18NBundle bundleEnglish = I18NBundle.createBundle(game.getResourceManager().getBasicBundleFile(), new Locale(LANGUAGE_ENGLISH));
        dictionary.put(LANGUAGE_ENGLISH, bundleEnglish);
    }

    private static void fillRussianStrings() {
        I18NBundle bundleRussian = I18NBundle.createBundle(game.getResourceManager().getBasicBundleFile(), new Locale(LANGUAGE_RUSSIAN));
        dictionary.put(LANGUAGE_RUSSIAN, bundleRussian);
    }

    // Auto-generated

    public static String getLanguage() {
        return language;
    }

}
