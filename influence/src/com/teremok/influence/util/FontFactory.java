package com.teremok.influence.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/**
 * Created by Алексей on 08.06.2014
 */
public class FontFactory {
    static private final String russian = "АаБбВвГгДдЕеЁёЖжЗзИиЙйКкЛлМмНнОоПпРрСсТтУуФфХхЦцЧчШшЩщьЫыЪъЭэЮюЯя";

    static public final int STATUS_FONT_SIZE = 25;
    static public final int SUBSTATUS_FONT_SIZE = 22;
    static public final int CELLS_FONT_SIZE = 25;
    static public final int RATING_FONT_SIZE = 48;

    static public final String STATUS_FONT_NAME = "myriadprosemibold";
    static public final String SUBSTATUS_FONT_NAME = "myriadprosemibold";
    static public final String CELLS_FONT_NAME = "arialbd";
    static public final String RATING_FONT_NAME = "myriadprosemibold";

    private BitmapFont statusFont;
    private BitmapFont substatusFont;
    private BitmapFont cellsFont;
    private BitmapFont ratingFont;

    public void load() {
        Gdx.app.debug(this.getClass().getSimpleName(), "Loading fonts...");
        ratingFont = loadFont(RATING_FONT_SIZE, RATING_FONT_NAME);
        statusFont = loadFont(STATUS_FONT_SIZE, STATUS_FONT_NAME);
        substatusFont = loadFont(SUBSTATUS_FONT_SIZE, SUBSTATUS_FONT_NAME);
        cellsFont = loadFont(CELLS_FONT_SIZE, CELLS_FONT_NAME);
    }

    private BitmapFont loadFont(int size, String fontName) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/" + fontName + ".ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.characters += russian;
        parameter.magFilter = Texture.TextureFilter.Linear;
        parameter.minFilter = Texture.TextureFilter.Linear;
        parameter.size = size;
        BitmapFont font = generator.generateFont(parameter);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        generator.dispose();
        return font;
    }

    public void dispose() {
        statusFont.dispose();
        substatusFont.dispose();
        cellsFont.dispose();
        ratingFont.dispose();
        Gdx.app.debug(this.getClass().getSimpleName(), "Disposing fonts...");
    }

    // Auto-generated

    public BitmapFont getStatusFont() {
        return statusFont;
    }

    public BitmapFont getSubstatusFont() {
        return substatusFont;
    }

    public BitmapFont getCellsFont() {
        return cellsFont;
    }

    public BitmapFont getRatingFont() {
        return ratingFont;
    }
}
