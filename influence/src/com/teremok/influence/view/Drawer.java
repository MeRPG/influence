package com.teremok.influence.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.teremok.influence.model.Cell;
import com.teremok.influence.model.Field;
import com.teremok.influence.model.Score;
import com.teremok.influence.screen.AbstractScreen;

/**
 * Created by Alexx on 24.12.13
 */

public class Drawer {

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

    public static Color getCellColorByType (int type) {
        Color color;
        switch (type) {
            case 0:
                color = Color.CYAN;
                break;
            case 1:
                color = Color.GREEN;
                break;
            case 2:
                color = Color.ORANGE;
                break;
            case 3:
                color = Color.PINK;
                break;
            case 4:
                color = Color.MAGENTA;
                break;
            default:
                color = Color.GRAY;
                break;
        }
        return color;
    }
}
