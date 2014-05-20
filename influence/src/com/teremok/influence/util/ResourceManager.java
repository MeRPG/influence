package com.teremok.influence.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.teremok.influence.model.Settings;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alexx on 24.02.14
 */
public class ResourceManager {


    private static final String UI_PATH_INTERNAL = "ui/";
    private static final String UI_PATH_EXTERNAL = ".influence/ui/";
    private static final String UI_EXT = ".xml";

    private static final String ATLAS_PATH_INTERNAL = "atlas/";
    private static final String ATLAS_PATH_EXTERNAL = ".influence/atlas/";
    private static final String ATLAS_EXT = ".pack";

    private static Map<String, TextureAtlas> atlases;

    private static TextureAtlas loadAtlas(String atlasName) {
        String internalPath = ATLAS_PATH_INTERNAL + atlasName + ATLAS_EXT;
        String externalPath = ATLAS_PATH_EXTERNAL + atlasName + ATLAS_EXT;
        TextureAtlas atlas;
        //Logger.log("Load atlas " + atlasName);
        if (Gdx.files.external(externalPath).exists()) {
            atlas= new TextureAtlas(Gdx.files.external(externalPath));
            //Logger.log("use external path: " + externalPath);
        } else {
            atlas= new TextureAtlas(Gdx.files.internal(internalPath));
            //Logger.log("use internal path: " + internalPath);
        }
        for (Texture texture : atlas.getTextures()) {
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }
        atlases.put(atlasName, atlas);
        //Logger.log("Atlas loaded: " + atlas);
        return atlas;
    }

    public static TextureAtlas getAtlas(String atlasName) {
        if (atlases != null) {
            if (! atlases.containsKey(atlasName)) {
                loadAtlas(atlasName);
            }
        } else {
            atlases = new HashMap<String, TextureAtlas>();
            loadAtlas(atlasName);
        }
        return atlases.get(atlasName);
    }

    public static void disposeAtlas(String atlasName) {
        if (atlases != null) {
            if (atlases.containsKey(atlasName)) {
                atlases.get(atlasName).dispose();
                atlases.remove(atlasName);
            }
        }
    }

    public static void disposeAll() {
        if (atlases != null) {
            for (TextureAtlas atlas : atlases.values()) {
                atlas.dispose();
            }
            atlases.clear();
        }
    }

    public static FileHandle getScreenUi(String screenName) {
        String internalPath = UI_PATH_INTERNAL + screenName + UI_EXT;
        String externalPath = UI_PATH_EXTERNAL + screenName + UI_EXT;
        if (Settings.debug && Gdx.files.external(externalPath).exists()) {
            return Gdx.files.external(externalPath);
        } else {
            return Gdx.files.internal(internalPath);
        }
    }
}
