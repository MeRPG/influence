package com.teremok.framework.util;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by Алексей on 08.07.2014
 */
public class ResourceManagerImpl <T extends Game> implements ResourceManager {

    protected T game;
    protected AssetManager assetManager;

    public void preload(){}

    public TextureAtlas getAtlas(String atlasName) {
        String fullAtlasName = getFullAtlasName(atlasName);
        if (! assetManager.isLoaded(fullAtlasName)) {
            assetManager.load(fullAtlasName, TextureAtlas.class);
            assetManager.finishLoading();
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

    public BitmapFont getFont(String fontName) {
        String fullFontName = getFullFontName(fontName);
        if (! assetManager.isLoaded(fullFontName)) {
            assetManager.load(fullFontName, BitmapFont.class);
            assetManager.finishLoading();
        }
        BitmapFont font = assetManager.get(fullFontName);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        return font;
    }

    public Sound getSound(String soundName) {
        String fullSoundName = getFullSoundName(soundName);
        return assetManager.get(fullSoundName);
    }

    public FileHandle getScreenUi(String screenName) {
        return Gdx.files.internal(getFullScreenUiName(screenName));
    }

    public FileHandle getBundleFile(String language) {
        return Gdx.files.internal(getFullBundleName(language));
    }

    public FileHandle getBasicBundleFile() {
        return Gdx.files.internal("locale/messages");
    }

    protected String getFullBundleName(String language) {
        return "locale/messages_" + language;
    }

    protected String getFullSoundName(String soundName) {
        return "sound/" + soundName + ".mp3";
    }

    protected String getFullAtlasName(String atlasName) {
        return "atlas/" + atlasName + ".pack";
    }

    protected String getFullFontName(String fontName) {
        return "font/" + fontName + ".fnt";
    }

    protected String getFullScreenUiName(String uiName) {
        return "ui/" + uiName + ".xml";
    }

    public void dispose() {
        assetManager.clear();
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

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }
}
