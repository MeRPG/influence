package com.teremok.influence.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.teremok.influence.model.Cell;
import com.teremok.influence.util.RenderHelper;

/**
 * Created by Alexx on 23.12.13
 */
public class CellDrawer {

    private static final float STANDARD_SIZE_MULTIPLIER = 0.4f;
    private static final float BIG_SIZE_MULTIPLIER = 0.6f;

    private static ShapeRenderer renderer = new ShapeRenderer();
    private static BitmapFont bitmapFont;
    private static Cell current;

    public static void draw(Cell cell, SpriteBatch batch, float parentAlpha) {
        current = cell;
        batch.end();

        renderer.setProjectionMatrix(batch.getProjectionMatrix());
        renderer.setTransformMatrix(batch.getTransformMatrix());
        renderer.translate(current.getX(), current.getY(), 0);
        Color color = RenderHelper.getCellColorByType(current.getType());
        renderer.setColor(color);

        //drawBoundingBox();

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
            renderer.circle(current.getWidth()/2, current.getHeight()/2, RenderHelper.UNIT_SIZE * BIG_SIZE_MULTIPLIER);
        }
        renderer.end();

        batch.begin();

        if (bitmapFont != null) {
            BitmapFont.TextBounds textBounds = bitmapFont.getBounds(current.getPower()+"");
            bitmapFont.draw(batch, current.getPower()+"", current.getX()+current.getWidth()/2 - textBounds.width/2,
                                                            current.getY()+current.getHeight()/2 + textBounds.height/2);
        }
    }

    static private void drawBoundingBox() {
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.rect(0,0,current.getWidth(), current.getHeight());
        renderer.end();
    }

    private static void renderWithMultiplier(float multiplier) {
        float centerX = current.getWidth()/2;
        float centerY = current.getHeight()/2;

        renderer.circle(centerX, centerY, RenderHelper.UNIT_SIZE * multiplier, 6);

        renderer.setColor(Color.BLACK);

        float powerArc = (float)current.getPower() / current.getMaxPower() * 360f;
        renderer.arc(centerX, centerY, RenderHelper.UNIT_SIZE * multiplier * .7f, 90f, powerArc);

        renderer.setColor(RenderHelper.getCellColorByType(current.getType()));
        renderer.circle(centerX, centerY, RenderHelper.UNIT_SIZE * multiplier * .6f);

    }

    public static void setBitmapFont(BitmapFont bitmapFont) {
        CellDrawer.bitmapFont = bitmapFont;
    }

}
