package com.teremok.influence.view;

import android.util.Log;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.teremok.influence.model.Cell;
import com.teremok.influence.model.Field;
import com.teremok.influence.model.GestureController;
import com.teremok.influence.util.Logger;

/**
 * Created by Alexx on 23.12.13
 */
public class FieldShapeDrawer extends AbstractDrawer<Field> {

    private static final float  MIN_SIZE_FOR_TEXT = 24f;

    @Override
    public void draw (Field field, SpriteBatch batch, float parentAlpha) {
        super.draw(field, batch, parentAlpha);
        batch.setColor(Color.WHITE);
        batch.end();
        drawShapeBackground(batch);

        for (Cell c : current.getCells()) {
            drawCell(c);
            drawCellRoutesShape(batch, c);
        }

        drawBoundingBox();

        batch.begin();

        for (Cell c : current.getCells()) {
            if (bitmapFont != null && GestureController.getZoom()*Drawer.UNIT_SIZE > MIN_SIZE_FOR_TEXT) {
                BitmapFont.TextBounds textBounds = bitmapFont.getBounds(c.getPower()+"");
                if (c.isFree()) {
                    bitmapFont.setColor(Drawer.getEmptyCellTextColor());
                } else {
                    bitmapFont.setColor(Drawer.getCellTextColor());
                }
                bitmapFont.draw(batch, c.getPower()+"", current.getX() + c.getX()+Field.cellWidth/2 - textBounds.width/2,
                        current.getY() + c.getY()+Field.cellHeight/2 + textBounds.height/2);
                /*
                bitmapFont.setColor(Color.WHITE.cpy());
                bitmapFont.draw(batch, c.getNumber()+"", current.getX() + c.getX()+Field.cellWidth/4 - textBounds.width/4,
                        current.getY() + c.getY()+Field.cellHeight/4 + textBounds.height/4);
                */
            }
        }

    }

    private void drawCell(Cell cell) {


        float centerX = cell.getX() + Field.cellWidth/2;
        float centerY = cell.getY() + Field.cellHeight/2;

        Color color = Drawer.getCellColorByNumber(cell.getType()).cpy();

        if (cell.isSelected()) {
            color.add(0.2f, 0.2f, 0.2f, 0f);
        }

        renderer.setColor(color);

        renderer.setColor(color);
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.circle(centerX, centerY, Drawer.UNIT_SIZE* GestureController.getZoom() * (0.4f + cell.getMaxPower()*0.03f), 6);
        renderer.end();

        renderer.setColor(color);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.circle(centerX, centerY, Drawer.UNIT_SIZE * GestureController.getZoom() * (0.4f + cell.getPower()*0.03f), 6);
        renderer.end();
    }

    private void drawShapeBackground(SpriteBatch batch) {
        renderer.begin(ShapeRenderer.ShapeType.Filled);

        renderer.setColor(Drawer.getBackgroundColor());
        renderer.rect(0,0, current.getWidth(), current.getHeight());

        renderer.end();
    }

    private void drawCellRoutesShape(SpriteBatch batch, Cell cell) {
        for (Cell toCell : cell.getNeighborsList()) {
            float centerX = cell.getX() + Field.cellWidth/2;
            float centerY = cell.getY() + Field.cellHeight/2;


            float centerXto = toCell.getX() + Field.cellWidth/2;
            float centerYto = toCell.getY() + Field.cellHeight/2;

            renderer.begin(ShapeRenderer.ShapeType.Line);
            renderer.line(centerX, centerY, centerXto, centerYto,
                    Drawer.getCellColorByNumber(cell.getType()),
                    Drawer.getCellColorByNumber(toCell.getType()));
            renderer.end();
        }
    }

    @Override
    protected void drawBoundingBox() {

        Cell selected = current.getSelectedCell();

        if (selected != null && selected.isValid()) {
            renderer.setColor(Drawer.getCellColorByNumber(selected.getType()));
        } else {
            renderer.setColor(Color.WHITE);
        }

        super.drawBoundingBox();
    }
}
