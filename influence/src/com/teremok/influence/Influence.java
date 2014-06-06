package com.teremok.influence;

import com.badlogic.gdx.Game;
import com.teremok.influence.model.Localizator;
import com.teremok.influence.screen.ScreenController;
import com.teremok.influence.util.Logger;

import java.util.Locale;

public class Influence extends Game {

    public Influence(Locale locale) {
        String language = locale.getLanguage();
        Logger.log("Use language: " + language);
        switch (language) {
            case "ru":
            case "uk":
            case "lt":
            case "kk":
                Localizator.setRussianLanguage();
                break;
            case "de":
                Localizator.setGermanLanguage();
                break;
            default:
                Localizator.setEnglishLanguage();
                break;
        }
    }

    @Override
	public void create() {
        ScreenController.init(this);
        ScreenController.forceShowStartScreen();
	}
}