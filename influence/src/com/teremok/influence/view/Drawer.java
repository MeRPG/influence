package com.teremok.influence.view;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.teremok.influence.model.Cell;
import com.teremok.influence.model.Field;
import com.teremok.influence.model.Score;

/**
 * Created by Alexx on 24.12.13
 */

public class Drawer {
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
}
