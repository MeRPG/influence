package com.teremok.influence.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.teremok.influence.model.Cell;
import com.teremok.influence.util.DrawHelper;

/**
 * Created by Alexx on 23.12.13
 */
public class CellDrawer extends AbstractDrawer<Cell> {

    private static final float STANDARD_SIZE_MULTIPLIER = 0.4f;
    private static final float BIG_SIZE_MULTIPLIER = 0.6f;

    @Override
    public void draw(Cell cell, SpriteBatch batch, float parentAlpha) {
        super.draw(cell, batch, parentAlpha);

        Color color = DrawHelper.getCellColorByType(current.getType());
        renderer.setColor(color);
        batch.end();
        drawBoundingBox();

        renderer.begin(ShapeRenderer.ShapeType.Filled);

        if (current.isBig()) {
            renderWithMultiplier(BIG_SIZE_MULTIPLIER);
        } else {
            renderWithMultiplier(STANDARD_SIZE_MULTIPLIER);
        }

        renderer.end();

        renderer.begin(ShapeRenderer.ShapeType.Line);
        if (current.isSelected()) {
            renderer.setColor(Color.WHITE);
            renderer.circle(current.getWidth()/2, current.getHeight()/2, DrawHelper.UNIT_SIZE * BIG_SIZE_MULTIPLIER);
        }
        renderer.end();

        batch.begin();

        if (bitmapFont != null) {
            BitmapFont.TextBounds textBounds = bitmapFont.getBounds(current.getPower()+"");
            bitmapFont.draw(batch, current.getPower()+"", current.getX()+current.getWidth()/2 - textBounds.width/2,
                                                            current.getY()+current.getHeight()/2 + textBounds.height/2);
        }
    }

    private void renderWithMultiplier(float multiplier) {
        float centerX = current.getWidth()/2;
        float centerY = current.getHeight()/2;

        renderer.circle(centerX, centerY, DrawHelper.UNIT_SIZE * multiplier, 6);

        renderer.setColor(Color.BLACK);

        float powerArc = (float)current.getPower() / current.getMaxPower() * 360f;
        renderer.arc(centerX, centerY, DrawHelper.UNIT_SIZE * multiplier * .7f, 90f, powerArc);

        renderer.setColor(DrawHelper.getCellColorByType(current.getType()));
        renderer.circle(centerX, centerY, DrawHelper.UNIT_SIZE * multiplier * .6f);

    }
}
