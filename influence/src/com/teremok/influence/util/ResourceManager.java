package com.teremok.influence.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.teremok.influence.Influence;
import com.teremok.influence.model.Settings;

/**
 * Created by Alexx on 24.02.14
 */

@SuppressWarnings("unused")
public class ResourceManager {

    private static final String UI_PATH_INTERNAL = "ui/";
    private static final String UI_PATH_EXTERNAL = ".influence/ui/";
    private static final String UI_EXT = ".xml";

    static public final int STATUS_FONT_SIZE = 25;
    static public final int SUBSTATUS_FONT_SIZE = 22;
    static public final int CELLS_FONT_SIZE = 25;

    static public final String STATUS_FONT_NAME = "magistralcbold";
    static public final String SUBSTATUS_FONT_NAME = "magistralcbold";
    static public final String CELLS_FONT_NAME = "arialbd";

    private Influence game;
    private AssetManager assetManager;

    public ResourceManager(Influence game) {
        this.game = game;
        assetManager = new AssetManager(new ResourcesResolver());
        assetManager.getLogger().setLevel(Gdx.app.getLogLevel());
        preload();
    }

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

    public void reloadLocalizedAtlas(String atlasName, String oldLanguage, String newLanguage) {
        String fullOldAtlasName = getFullAtlasName(atlasName+"_"+oldLanguage);
        String fullNewAtlasName = getFullAtlasName(atlasName+"_"+newLanguage);
        if (assetManager.isLoaded(fullOldAtlasName)) {
            assetManager.unload(fullOldAtlasName);
        }
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

    private String getFullSoundName(String soundName) {
        return "sound/" + soundName + ".mp3";
    }

    private String getFullAtlasName(String atlasName) {
        return "atlas/" + atlasName + ".pack";
    }

    private String getFullFontName(String fontName) {
        return "font/" + fontName + ".fnt";
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
