package com.teremok.influence.util;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.teremok.influence.Influence;

/**
 * Created by Алексей on 08.06.2014
 */
public class FontFactory {

    public static final String STATUS_FONT = "statusFont";

    public static final String CELLS_FONT = "cellsFont";

    public static final String SUBSTATUS_FONT = "substatusFont";

    public static final String DEBUG_FONT = "debugFont";

    private Influence game;

    public FontFactory(Influence game) {
        this.game = game;
    }

    public BitmapFont getFont(String fontName) {
        BitmapFont font;
        switch (fontName) {
            case STATUS_FONT:
                font = game.getResourceManager().getFont(STATUS_FONT);
                break;
            case SUBSTATUS_FONT:
                font = game.getResourceManager().getFont(SUBSTATUS_FONT);
                break;
            case CELLS_FONT:
                font = game.getResourceManager().getFont(CELLS_FONT);
                break;
            case DEBUG_FONT:
                font = game.getResourceManager().getFont(SUBSTATUS_FONT);
                font.setScale(0.9f);
                break;
            default:
                font = null;
        }
        return font;
    }
}
