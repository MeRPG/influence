package com.teremok.influence.util;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.teremok.influence.Influence;

/**
 * Created by Алексей on 08.06.2014
 */
public class FontFactory {

    public static final String STATUS_FONT = "statusFont";
    private static final float STATUS_FONT_SCALE = 0.78f;

    public static final String CELLS_FONT = "cellsFont";
    private static final float CELLS_FONT_SCALE = 0.78f;

    public static final String SUBSTATUS_FONT = "substatusFont";
    private static final float SUBSTATUS_FONT_SCALE = 0.69f;

    private Influence game;

    public FontFactory(Influence game) {
        this.game = game;
    }

    public BitmapFont getFont(String fontName) {
        BitmapFont font;
        switch (fontName) {
            case STATUS_FONT:
                font = game.getResourceManager().getFont(STATUS_FONT);
                //font.setScale(STATUS_FONT_SCALE);
                break;
            case SUBSTATUS_FONT:
                font = game.getResourceManager().getFont(SUBSTATUS_FONT);
                //font.setScale(SUBSTATUS_FONT_SCALE);
                break;
            case CELLS_FONT:
                font = game.getResourceManager().getFont(CELLS_FONT);
                //font.setScale(CELLS_FONT_SCALE);
                break;
            default:
                font = null;
        }
        return font;
    }
}
