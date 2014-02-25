package com.teremok.influence;

import com.badlogic.gdx.Game;
import com.teremok.influence.model.Localizator;
import com.teremok.influence.screen.ScreenController;

import java.util.Locale;

public class Influence extends Game {

    public Influence(Locale locale) {
        String language = locale.getLanguage();
        if (language.equals("ru") || language.equals("uk") || language.equals("lt") || language.equals("kk")) {
            Localizator.setRussianLanguage();
        }  else {
            Localizator.setEnglishLanguage();
        }
    }

    @Override
	public void create() {
        ScreenController.init(this);
        ScreenController.showStartScreen();
	}
}