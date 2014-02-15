package com.teremok.influence.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.teremok.influence.model.Cell;
import com.teremok.influence.model.GestureController;

/**
 * Created by Alexx on 23.12.13
 */
public class CellShapeDrawer extends AbstractDrawer<Cell> {

    private static final float  MIN_SIZE_FOR_TEXT = 24f;

    public CellShapeDrawer() {
        super();
    }

    @Override
    public void draw(Cell cell, SpriteBatch batch, float parentAlpha) {
        super.draw(cell, batch, parentAlpha);
        drawCell(batch);
        if (bitmapFont != null && GestureController.getZoom()*Drawer.UNIT_SIZE > MIN_SIZE_FOR_TEXT) {
            BitmapFont.TextBounds textBounds = bitmapFont.getBounds(current.getPower()+"");
            if (current.getType() == -1) {
                bitmapFont.setColor(Drawer.getEmptyCellTextColor());
            } else {
                bitmapFont.setColor(Drawer.getCellTextColor());
            }
            bitmapFont.draw(batch, current.getPower()+"", current.getX()+current.getWidth()/2 - textBounds.width/2,
                    current.getY()+current.getHeight()/2 + textBounds.height/2);
        }
    }

    private void drawCell(SpriteBatch batch) {
        batch.end();

        float centerX = current.getWidth()/2;
        float centerY = current.getHeight()/2;

        Color color = Drawer.getCellColorByNumber(current.getType()).cpy();

        if (current.isSelected()) {
            color.add(0.2f, 0.2f, 0.2f, 0f);
        }

        renderer.setColor(color);

        renderer.setColor(color);
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.circle(centerX, centerY, Drawer.UNIT_SIZE* GestureController.getZoom() * (0.4f + current.getMaxPower()*0.03f), 6);
        renderer.end();

        renderer.setColor(color);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.circle(centerX, centerY, Drawer.UNIT_SIZE * GestureController.getZoom() * (0.4f + current.getPower()*0.03f), 6);
        renderer.end();

        batch.begin();
    }
}
