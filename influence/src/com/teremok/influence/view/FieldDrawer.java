package com.teremok.influence.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.teremok.influence.model.Cell;
import com.teremok.influence.model.Field;
import com.teremok.influence.util.DrawHelper;

/**
 * Created by Alexx on 23.12.13
 */
public class FieldDrawer extends AbstractDrawer<Field> {

    @Override
    public void draw (Field field, SpriteBatch batch, float parentAlpha) {
        super.draw(field, batch, parentAlpha);
        batch.end();

        drawBoundingBox();
        for (Cell c : current.getCells()) {
            drawCellRoutes(c);
        }

        batch.begin();
    }

    private void drawCellRoutes(Cell cell) {
        for (int j = 0; j < 35; j++) {
            if (cell.isValid() && current.getGraphMatrix()[cell.getNumber()][j] == 1) {
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

    @Override
    protected void drawBoundingBox() {

        Cell selected = current.getSelectedCell();

        if (selected != null && selected.isValid()) {
            renderer.setColor(DrawHelper.getCellColorByType(selected.getType()));
        } else {
            renderer.setColor(Color.WHITE);
        }

        super.drawBoundingBox();
    }
}
