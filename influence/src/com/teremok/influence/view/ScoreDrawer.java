package com.teremok.influence.view;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.teremok.influence.model.Score;
import com.teremok.influence.util.DrawHelper;

/**
 * Created by Alexx on 24.12.13
 */
public class ScoreDrawer extends AbstractDrawer<Score> {

    @Override
    public void draw(Score score, SpriteBatch batch, float parentAlpha) {
        super.draw(score, batch, parentAlpha);

        batch.end();

        drawBoundingBox();

        batch.begin();
        drawScores(batch);
    }

    private void drawScores(SpriteBatch batch) {
        int i = 0;
        for (int score : current.getScores()) {
            bitmapFont.setColor(DrawHelper.getCellColorByType(i));
            bitmapFont.draw(batch, score + " ", 25f + i*50f, current.getHeight()/2);
            i++;
        }
    }
}
