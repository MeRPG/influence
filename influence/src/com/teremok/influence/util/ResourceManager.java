package com.teremok.influence.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Logger;
import com.teremok.influence.Influence;
import com.teremok.influence.model.Settings;

/**
 * Created by Alexx on 24.02.14
 */

//TODO избавиться от статичности
@SuppressWarnings("unused")
public class ResourceManager {

    private static final String UI_PATH_INTERNAL = "ui/";
    private static final String UI_PATH_EXTERNAL = ".influence/ui/";
    private static final String UI_EXT = ".xml";

    private static final String ATLAS_PATH_INTERNAL = "atlas/";
    private static final String ATLAS_PATH_EXTERNAL = ".influence/atlas/";
    private static final String ATLAS_EXT = ".pack";


    private Influence game;
    private AssetManager assetManager;

    public ResourceManager(Influence game) {
        this.game = game;
        assetManager = new AssetManager(new ResourcesResolver());
        assetManager.getLogger().setLevel(Logger.DEBUG);
        preload();
    }

    public void preload() {
        assetManager.load("atlas/gameScreen.pack", TextureAtlas.class);
        assetManager.load("atlas/mapSize.pack", TextureAtlas.class);
        assetManager.load("atlas/pausePanel.pack", TextureAtlas.class);
        assetManager.load("atlas/screenPlayers.pack", TextureAtlas.class);
        assetManager.load("atlas/startScreen.pack", TextureAtlas.class);

        assetManager.load("sound/click.mp3", Sound.class);
        assetManager.load("sound/win.mp3", Sound.class);
        assetManager.load("sound/lose.mp3", Sound.class);
        assetManager.load("sound/winMatch.mp3", Sound.class);
        assetManager.load("sound/loseMatch.mp3", Sound.class);
    }

    public TextureAtlas getAtlas(String atlasName) {
        String fullAtlasName = getFullAtlasName(atlasName);
        if (! assetManager.isLoaded(fullAtlasName)) {
            assetManager.load(fullAtlasName, TextureAtlas.class);
            while(!assetManager.update()) {
            }
        }
        TextureAtlas atlas = assetManager.get(fullAtlasName);
        for (Texture texture : atlas.getTextures()) {
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }
        return assetManager.get(fullAtlasName);
    }

    public void disposeAtlas(String atlasName) {
        String fullAtlasName = getFullAtlasName(atlasName);
        if (assetManager.isLoaded(fullAtlasName))
        assetManager.unload(fullAtlasName);
    }

    public void reloadLocalizedAtlas(String atlasName, String oldLanguage, String newLanguage) {
        String fullOldAtlasName = getFullAtlasName(atlasName+"_"+oldLanguage);
        String fullNewAtlasName = getFullAtlasName(atlasName+"_"+newLanguage);
        if (assetManager.isLoaded(fullOldAtlasName)) {
            assetManager.unload(fullOldAtlasName);
        }
    }

    private String getFullAtlasName(String atlasName) {
        return "atlas/" + atlasName + ".pack";
    }

    public Sound getSound(String soundName) {
        String fullSoundName = getFullSoundName(soundName);
        return assetManager.get(fullSoundName);
    }

    private String getFullSoundName(String soundName) {
        return "sound/" + soundName + ".mp3";
    }

    public void dispose() {
        assetManager.dispose();
    }

    public boolean update() {
        return assetManager.update();
    }

    public boolean update(int mills) {
        return assetManager.update(mills);
    }

    public String getPercentsProgress() {
        return assetManager.getProgress() * 100 + "%";
    }

    public FileHandle getScreenUi(String screenName) {
        String internalPath = UI_PATH_INTERNAL + screenName + UI_EXT;
        String externalPath = UI_PATH_EXTERNAL + screenName + UI_EXT;
        Settings settings = ((Influence)Gdx.app.getApplicationListener()).getSettings();
        if (settings.debug && Gdx.files.external(externalPath).exists()) {
            return Gdx.files.external(externalPath);
        } else {
            return Gdx.files.internal(internalPath);
        }
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }
}
