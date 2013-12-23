package com.teremok.influence.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.teremok.influence.model.Cell;
import com.teremok.influence.model.Field;
import com.teremok.influence.util.DrawHelper;

/**
 * Created by Alexx on 23.12.13
 */
public class FieldDrawer {


    private static ShapeRenderer renderer = new ShapeRenderer();
    private static BitmapFont bitmapFont;

    private static Field current;

    public static void draw (Field field, SpriteBatch batch, float patentAlpha) {
        current = field;

        batch.end();

        for (Cell c : current.getCells()) {
            drawCellRoutes(c);
        }

        renderer.setProjectionMatrix(batch.getProjectionMatrix());
        renderer.setTransformMatrix(batch.getTransformMatrix());
        renderer.translate(current.getX(), current.getY(), 0);

        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.rect(0, 0, current.getWidth(), current.getHeight());
        renderer.end();

        batch.begin();
    }

    private static void drawCellRoutes(Cell cell) {
        for (int j = 0; j < 35; j++) {
            if (cell.isValid() && current.getMinimal()[cell.getNumber()][j] == 1) {
                Cell toCell = current.getCells().get(j);
                if (toCell.isValid()) {
                    if (cell.getType() == toCell.getType()) {
                        renderer.setColor(DrawHelper.getCellColorByType(cell.getType()));
                    } else {
                        renderer.setColor(Color.GRAY);
                    }
                    renderer.begin(ShapeRenderer.ShapeType.Line);
                    renderer.line(cell.getX()+cell.getWidth()/2, cell.getY() + cell.getHeight()/2,
                            toCell.getX()+toCell.getWidth()/2, toCell.getY() + toCell.getHeight()/2);
                    renderer.end();
                }
            }
        }
    }


    public static void setBitmapFont(BitmapFont bitmapFont) {
        FieldDrawer.bitmapFont = bitmapFont;
    }
}
