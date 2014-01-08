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
        colors.put("CYAN", new Color(0x1691dfFF));
        colors.put("GREEN", new Color(0x30d614FF));
        colors.put("ORANGE", new Color(0xeade1aFF));
        colors.put("PINK", new Color(0xe72020FF));
        colors.put("MAGENTA", new Color(0xd91ceaFF));
        colors.put("GREY", new Color(0x888888FF));
    }

    public static final float UNIT_SIZE = AbstractScreen.WIDTH/10f;

    private static CellDrawer cellDrawer;
    private static FieldDrawer fieldDrawer;
    private static ScoreDrawer scoreDrawer;

    public static void draw(Actor actor, SpriteBatch batch, float parentAlpha) {

        if (actor instanceof Cell) {
            if (cellDrawer == null)
                cellDrawer = new CellDrawer();
            cellDrawer.draw((Cell)actor, batch, parentAlpha);
            return;
        }

        if (actor instanceof Field) {
            if (fieldDrawer == null)
                fieldDrawer = new FieldDrawer();
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
