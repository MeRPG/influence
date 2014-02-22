package com.teremok.influence;

import com.badlogic.gdx.Game;
import com.teremok.influence.model.Localizator;
import com.teremok.influence.screen.StartScreen;
import com.teremok.influence.screen.StartScreenAlt;
import com.teremok.influence.screen.StaticScreen;

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
    	setScreen(new StartScreenAlt(this, "abstractScreen.xml"));
	}
}