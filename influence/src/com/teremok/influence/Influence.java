package com.teremok.influence;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.teremok.influence.controller.ChronicleController;
import com.teremok.influence.controller.MatchSaver;
import com.teremok.influence.controller.SettingsController;
import com.teremok.influence.model.Chronicle;
import com.teremok.influence.model.Localizator;
import com.teremok.influence.model.Settings;
import com.teremok.influence.screen.ScreenController;
import com.teremok.influence.util.Logger;

import java.util.Locale;

@SuppressWarnings("unused")
public class Influence extends Game {

    private Chronicle chronicle;
    private ChronicleController chronicleController;

    private Settings settings;
    private SettingsController settingsController;

    private MatchSaver matchSaver;


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
        matchSaver = new MatchSaver();

        chronicleController = new ChronicleController();
        chronicle = chronicleController.load();

        settingsController = new SettingsController();
        settings = settingsController.load();

        ScreenController.init(this);
        ScreenController.forceShowStartScreen();
	}

    public void exit() {
        settingsController.save(settings);
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

    public MatchSaver getMatchSaver() {
        return matchSaver;
    }

    public void setMatchSaver(MatchSaver matchSaver) {
        this.matchSaver = matchSaver;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public SettingsController getSettingsController() {
        return settingsController;
    }

    public void setSettingsController(SettingsController settingsController) {
        this.settingsController = settingsController;
    }
}