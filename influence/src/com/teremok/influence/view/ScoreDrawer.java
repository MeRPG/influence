package com.teremok.influence.view;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.teremok.influence.model.Score;

/**
 * Created by Alexx on 24.12.13
 */
public class ScoreDrawer {

    private static Score current;
    private static ShapeRenderer renderer = new ShapeRenderer();
    private static BitmapFont bitmapFont;

    public static void draw(Score score, SpriteBatch batch, float parentAlpha) {
        current = score;

        renderer.setProjectionMatrix(batch.getProjectionMatrix());
        renderer.setTransformMatrix(batch.getTransformMatrix());
        renderer.translate(current.getX(), current.getY(), 0);

        batch.end();

        drawBoundingBox();

        batch.begin();
        drawScores(batch);
    }

    static private void drawBoundingBox() {
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.rect(0,0,current.getWidth(), current.getHeight());
        renderer.end();
    }

    static private void drawScores(SpriteBatch batch) {
        int i = 0;
        for (int score : current.getScores()) {
            bitmapFont.draw(batch, score + " ", 10f + i*25f, current.getHeight()/2);
            i++;
        }
    }

    public static void setBitmapFont(BitmapFont bitmapFont) {
        ScoreDrawer.bitmapFont = bitmapFont;
    }
}
