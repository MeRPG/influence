package com.teremok.framework;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.teremok.framework.screen.ScreenController;
import com.teremok.framework.util.FontFactory;
import com.teremok.framework.util.ResourceManager;

/**
 * Created by Алексей on 08.07.2014
 */
public abstract class TeremokGame extends Game {

    protected String language;
    protected ResourceManager resourceManager;
    protected ScreenController screenController;
    protected FontFactory fontFactory;

    public void exit() {
        Gdx.app.exit();
    }

    @Override
    public void dispose() {
        super.dispose();
        resourceManager.dispose();
    }

    // Auto-generated

    public ResourceManager getResourceManager() {
        return resourceManager;
    }

    public void setResourceManager(ResourceManager resourceManager){
        this.resourceManager = resourceManager;
    }

    public ScreenController getScreenController() {
        return screenController;
    }

    public void setScreenController(ScreenController screenController) {
        this.screenController = screenController;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public FontFactory getFontFactory() {
        return fontFactory;
    }

    public void setFontFactory(FontFactory fontFactory) {
        this.fontFactory = fontFactory;
    }
}
