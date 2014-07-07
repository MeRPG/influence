package com.teremok.influence.util;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.teremok.influence.Influence;

/**
 * Created by Алексей on 08.06.2014
 */
public class FontFactory {

    public static final String STATUS = "statusFont";
    public static final String CELLS = "cellsFont";
    public static final String SUBSTATUS = "substatusFont";
    public static final String DEBUG = "debugFont";
    public static final String POPUP = "popupFont";
    public static final String TITLE = "titleFont";
    public static final String LABEL = "labelFont";

    private Influence game;

    public FontFactory(Influence game) {
        this.game = game;
    }

    public BitmapFont getFont(String fontName) {
        BitmapFont font;
        switch (fontName) {
            case STATUS:
                font = game.getResourceManager().getFont(STATUS);
                font.setScale(0.87f);
                break;
            case SUBSTATUS:
                font = game.getResourceManager().getFont(SUBSTATUS);
                font.setScale(1);
                break;
            case CELLS:
                font = game.getResourceManager().getFont(CELLS);
                font.setScale(1);
                break;
            case DEBUG:
                font = game.getResourceManager().getFont(SUBSTATUS);
                font.setScale(0.9f);
                break;
            case POPUP:
                font = game.getResourceManager().getFont(STATUS);
                font.setScale(0.9f);
                break;
            case TITLE:
                font = game.getResourceManager().getFont(STATUS);
                font.setScale(1);
                break;
            case LABEL:
                font = game.getResourceManager().getFont(SUBSTATUS);
                font.setScale(1);
                break;
            default:
                font = null;
        }
        return font;
    }
}
