package com.teremok.influence.util;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Алексей on 26.06.2014
 */
public class FontGenerateLoader { /*extends SynchronousAssetLoader<BitmapFont, FontGenerateLoader.FontParameter> {

    private FontFactory fontFactory;

    public FontGenerateLoader (FileHandleResolver resolver) {
        super(resolver);
        fontFactory = new FontFactory();
    }

    @Override
    public BitmapFont load(AssetManager assetManager, String fileName, FileHandle file, FontParameter parameter) {
        return fontFactory.loadFont(parameter.size, parameter.fontName, parameter.chars);
    }


    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, FontParameter parameter) {
        return null;
    }

    static public class FontParameter extends AssetLoaderParameters<BitmapFont> {
        int size = 16;
        String fontName;
        String chars = null;

        public FontParameter(int size, String fontName) {
            this(size, fontName, false);
        }

        public FontParameter(int size, String fontName, boolean onlyNumbers) {
            this.size = size;
            this.fontName = fontName;
            if (onlyNumbers) {
                chars = FontFactory.numbers;
            }
        }
    }*/
}
