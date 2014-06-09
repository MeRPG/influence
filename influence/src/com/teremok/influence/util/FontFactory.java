package com.teremok.influence.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/**
 * Created by Алексей on 08.06.2014
 */
public class FontFactory {

    static private final String english = "";
    static private final String russian = "АаБбВвГгДдЕеЁёЖжЗзИиЙйКкЛлМмНнОоПпРрСсТтУуФфХхЦцЧчШшЩщьЫыЪъЭэЮюЯя";
    static private final String german = "";
    static private final String misc = "";

    static public final int STATUS_FONT_SIZE = 25;
    static public final int SUBSTATUS_FONT_SIZE = 22;
    static public final int CELLS_FONT_SIZE = 25;
    static public final int RATING_FONT_SIZE = 48;

    static public final String STATUS_FONT_NAME = "myriadprosemibold";
    static public final String SUBSTATUS_FONT_NAME = "myriadprosemibold";
    static public final String CELLS_FONT_NAME = "arialbd";
    static public final String RATING_FONT_NAME = "myriadprosemibold";

    static private BitmapFont statusFont;
    static private BitmapFont substatusFont;
    static private BitmapFont cellsFont;
    static private BitmapFont ratingFont;

    static public BitmapFont getStatusFont() {
        if (statusFont == null) {
            statusFont = getFont(STATUS_FONT_SIZE, STATUS_FONT_NAME);
        }
        return statusFont;
    }

    static public BitmapFont getSubstatusFont() {
        if (substatusFont == null) {
            substatusFont = getFont(SUBSTATUS_FONT_SIZE, SUBSTATUS_FONT_NAME);
        }
        return substatusFont;
    }

    static public BitmapFont getCellsFont() {
        if (cellsFont == null) {
            cellsFont = getFont(CELLS_FONT_SIZE, CELLS_FONT_NAME);
        }
        return cellsFont;
    }

    static public BitmapFont getRatingFont() {
        if (ratingFont == null) {
            ratingFont = getFont(RATING_FONT_SIZE, RATING_FONT_NAME);
        }
        return ratingFont;
    }

    static private BitmapFont getFont(int size, String fontName) {

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


}
