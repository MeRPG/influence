package com.teremok.framework.util;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by Алексей on 08.07.2014
 */
public interface ResourceManager {
    void preload();

    TextureAtlas getAtlas(String atlasName);
    void disposeAtlas(String atlasName);

    public BitmapFont getFont(String fontName);
    public Sound getSound(String soundName);
    public FileHandle getScreenUi(String screenName);
    public FileHandle getBundleFile(String language);
    public FileHandle getBasicBundleFile();

    public void dispose();
    public boolean update();
    public boolean update(int mills);
    public String getPercentsProgress();

    public AssetManager getAssetManager();
    public void setAssetManager(AssetManager assetManager);
}
