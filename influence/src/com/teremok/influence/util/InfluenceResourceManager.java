package com.teremok.influence.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.teremok.framework.util.ResourceManagerImpl;
import com.teremok.influence.Influence;
import com.teremok.influence.model.Settings;

/**
 * Created by Alexx on 24.02.14
 */

public class InfluenceResourceManager extends ResourceManagerImpl <Influence>{

    private static final String UI_PATH_INTERNAL = "ui/";
    private static final String UI_PATH_EXTERNAL = ".influence/ui/";
    private static final String UI_EXT = ".xml";

    public InfluenceResourceManager(Influence game) {
        this.game = game;
        assetManager = new AssetManager(new ResourcesResolver());
        assetManager.getLogger().setLevel(Gdx.app.getLogLevel());
        preload();
    }

    @Override
    public void preload() {
        assetManager.load("atlas/background.pack", TextureAtlas.class);
        assetManager.load("atlas/gameScreen.pack", TextureAtlas.class);
        assetManager.load("atlas/mapSize.pack", TextureAtlas.class);
        assetManager.load("atlas/pausePanel.pack", TextureAtlas.class);
        assetManager.load("atlas/players.pack", TextureAtlas.class);
        assetManager.load("atlas/startScreen.pack", TextureAtlas.class);
        assetManager.load("atlas/modeScreen.pack", TextureAtlas.class);

        assetManager.load("sound/click.mp3", Sound.class);
        assetManager.load("sound/win.mp3", Sound.class);
        assetManager.load("sound/lose.mp3", Sound.class);
        assetManager.load("sound/winMatch.mp3", Sound.class);
        assetManager.load("sound/loseMatch.mp3", Sound.class);

        assetManager.load("font/cellsFont.fnt", BitmapFont.class);
        assetManager.load("font/statusFont.fnt", BitmapFont.class);
        assetManager.load("font/substatusFont.fnt", BitmapFont.class);
    }

    @Override
    public FileHandle getBundleFile(String language) {
        String fullBundleName = getFullBundleName(language);
        String externalPath = ".influence/" + fullBundleName;
        Settings settings = game.getSettings();
        if (settings.debug && Gdx.files.external(fullBundleName).exists()) {
            return Gdx.files.external(externalPath);
        } else {
            FileHandle fileHandle = Gdx.files.internal(fullBundleName);
            Gdx.app.debug(getClass().getSimpleName(), "Base bundle file: " + fileHandle.path() + " : exists " + fileHandle.exists());
            return fileHandle;
        }
    }

    @Override
    public FileHandle getScreenUi(String screenName) {
        String internalPath = UI_PATH_INTERNAL + screenName + UI_EXT;
        String externalPath = UI_PATH_EXTERNAL + screenName + UI_EXT;
        Settings settings = game.getSettings();
        if (settings.debug && Gdx.files.external(externalPath).exists()) {
            return Gdx.files.external(externalPath);
        } else {
            return Gdx.files.internal(internalPath);
        }
    }
}
