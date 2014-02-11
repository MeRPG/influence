package com.teremok.influence;

import com.badlogic.gdx.Game;
import com.teremok.influence.model.Localizator;
import com.teremok.influence.model.Settings;
import com.teremok.influence.screen.CreditsScreen;
import com.teremok.influence.screen.GameScreen;
import com.teremok.influence.screen.SettingsScreen;
import com.teremok.influence.screen.StartScreen;

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
    	setScreen(new StartScreen(this));
	}
}