package com.teremok.influence;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.teremok.influence.controller.ChronicleController;
import com.teremok.influence.controller.SettingsSaver;
import com.teremok.influence.model.Chronicle;
import com.teremok.influence.model.Localizator;
import com.teremok.influence.screen.ScreenController;
import com.teremok.influence.util.Logger;

import java.util.Locale;

public class Influence extends Game {

    private Chronicle chronicle;
    private ChronicleController chronicleController;

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
        chronicleController = new ChronicleController();
        chronicle = chronicleController.load();
        ScreenController.init(this);
        ScreenController.forceShowStartScreen();
	}

    public void exit() {
        SettingsSaver.save();
        chronicleController.save(chronicle);
        Gdx.app.exit();
    }
    // Auto-generated

    public Chronicle getChronicle() {
        return chronicle;
    }

    public void setChronicle(Chronicle chronicle) {
        this.chronicle = chronicle;
    }

    public ChronicleController getChronicleController() {
        return chronicleController;
    }

    public void setChronicleController(ChronicleController chronicleController) {
        this.chronicleController = chronicleController;
    }
}