package com.teremok.influence;

import com.badlogic.gdx.Game;
import com.teremok.influence.model.Localizator;
import com.teremok.influence.screen.StartScreen;

import java.util.Locale;

public class Influence extends Game {

    public Influence(Locale locale) {
        System.out.println("Game started with locale:" + locale + ", lang: " + locale.getLanguage());
        String language = locale.getLanguage();
        if (language.equals("ru") || language.equals("uk")) {
            Localizator.setRussianLanguage();
        }  else {
            Localizator.setEnglishLanguage();
        }
    }

    @Override
	public void create() {
    	setScreen(new StartScreen(this));
	}
}