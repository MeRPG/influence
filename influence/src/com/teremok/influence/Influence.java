package com.teremok.influence;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.teremok.framework.TeremokGame;
import com.teremok.framework.util.ResourceManager;
import com.teremok.influence.controller.ChronicleController;
import com.teremok.influence.controller.MatchSaver;
import com.teremok.influence.controller.SettingsController;
import com.teremok.influence.model.Chronicle;
import com.teremok.influence.model.Settings;
import com.teremok.influence.screen.InfluenceScreenController;
import com.teremok.influence.screen.SplashScreen;
import com.teremok.influence.util.FXPlayer;
import com.teremok.influence.util.InfluenceResourceManager;
import com.teremok.framework.util.Localizator;

import java.util.Locale;

@SuppressWarnings("unused")
public class Influence extends TeremokGame {

    private Chronicle chronicle;
    private ChronicleController chronicleController;

    private Settings settings;
    private SettingsController settingsController;

    private MatchSaver matchSaver;

    private FXPlayer fxPlayer;

    public Influence(Locale locale) {
        this.language = locale.getLanguage();
    }

    @Override
	public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        matchSaver = new MatchSaver();

        chronicleController = new ChronicleController();
        chronicle = chronicleController.load();

        fxPlayer = new FXPlayer(this);

        settingsController = new SettingsController();
        settings = settingsController.load();

        resourceManager = new InfluenceResourceManager(this);

        Localizator.init(this);

        screenController = new InfluenceScreenController(this);
        setScreen(new SplashScreen(this));
	}

    @Override
    public void exit() {
        Gdx.app.debug(getClass().getSimpleName(), "exiting game");
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

    public ResourceManager getResourceManager() {
        return resourceManager;
    }

    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public FXPlayer getFXPlayer() {
        return fxPlayer;
    }

    public void setFXPlayer(FXPlayer fxPlayer) {
        this.fxPlayer = fxPlayer;
    }
}