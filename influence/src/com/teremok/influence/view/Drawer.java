package com.teremok.influence.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.teremok.influence.model.Cell;
import com.teremok.influence.model.Field;
import com.teremok.influence.model.Score;
import com.teremok.influence.screen.AbstractScreen;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alexx on 24.12.13
 */

public class Drawer {

    private static Map<String, Color> colors;

    static {
        colors = new HashMap<String, Color>();
        colors.put("CYAN", new Color(0x35B7DEFF));
        colors.put("GREEN", new Color(0x02D47EFF));
        colors.put("ORANGE", new Color(0xFFD95DFF));
        colors.put("PINK", new Color(0xED6D5AFF));
        colors.put("MAGENTA", new Color(0x9968FDFF));
        colors.put("GREY", new Color(0x2E2E2EFF));
        colors.put("TEXT_COLOR", Color.WHITE.cpy());
        colors.put("DIMMED_TEXT_COLOR", new Color(0x545454FF));
        colors.put("CELL_TEXT_COLOR", Color.BLACK.cpy());
        colors.put("EMPTY_CELL_TEXT_COLOR",  Color.BLACK.cpy());
        colors.put("BACKGROUND_COLOR", Color.BLACK.cpy());
        colors.put("BACKLIGHT_WIN", new Color(0x00DD00FF));
        colors.put("BACKLIGHT_LOSE", new Color(0xFF0000FF));
        colors.put("GREEN", new Color(0x02D47EFF).lerp(Drawer.getBackgroundColor(), 0.3f));
    }

    public static Color getBackgroundColor() {
        return colors.get("BACKGROUND_COLOR");
    }

    public static Color getEmptyCellTextColor(){
        return colors.get("EMPTY_CELL_TEXT_COLOR");
    }

    public static Color getCellTextColor(){
        return colors.get("CELL_TEXT_COLOR");
    }

    public static Color getTextColor() {
        return colors.get("TEXT_COLOR");
    }

    public static Color getDimmedTextColor() {
        return colors.get("DIMMED_TEXT_COLOR");
    }

    public static Color getBacklightWinColor() {
        return colors.get("BACKLIGHT_WIN");
    }

    public static Color getBacklightLoseColor() {
        return colors.get("BACKLIGHT_LOSE");
    }

    public static final float UNIT_SIZE = AbstractScreen.WIDTH/10f;

    private static AbstractDrawer<Cell> cellDrawer;
    private static AbstractDrawer<Field> fieldDrawer;
    private static ScoreDrawer scoreDrawer;

    public static void draw(Actor actor, SpriteBatch batch, float parentAlpha) {

        if (actor instanceof Cell) {
            if (cellDrawer == null)
                cellDrawer = new CellShapeDrawer();
            cellDrawer.draw((Cell)actor, batch, parentAlpha);
            return;
        }

        if (actor instanceof Field) {
            if (fieldDrawer == null)
                fieldDrawer = new FieldShapeDrawer();
            fieldDrawer.draw((Field)actor, batch, parentAlpha);
        }

        if (actor instanceof Score) {
            if (scoreDrawer == null)
                scoreDrawer = new ScoreDrawer();
            scoreDrawer.draw((Score)actor, batch, parentAlpha);
        }

    }

    public static void dispose() {
        scoreDrawer = null;
        fieldDrawer = null;
        cellDrawer = null;
    }

    public static Color getCellColorByType (int type) {
        Color color;
        switch (type) {
            case 0:
                color = colors.get("CYAN");
                break;
            case 1:
                color = colors.get("GREEN");
                break;
            case 2:
                color = colors.get("ORANGE");
                break;
            case 3:
                color = colors.get("PINK");
                break;
            case 4:
                color = colors.get("MAGENTA");
                break;
            default:
                color = colors.get("GREY");
                break;
        }
        return color;
    }
}
