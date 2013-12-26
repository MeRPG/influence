package com.teremok.influence.view;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.teremok.influence.model.Player;
import com.teremok.influence.model.Score;
import com.teremok.influence.screen.GameScreen;
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
        drawPower(batch);
    }

    private void drawScores(SpriteBatch batch) {
        int i = 0;
        for (int score : current.getScores()) {
            bitmapFont.setColor(DrawHelper.getCellColorByType(i));
            String toDraw;
            if (i == Player.current().getType()) {
                toDraw = "-" + score + "-";
            } else {
                toDraw = " " + score + " ";
            }
            bitmapFont.draw(batch, toDraw, 25f + i*50f, current.getHeight()/2);
            i++;
        }
    }

    private void drawPower(SpriteBatch batch) {
        if (GameScreen.currentPhase == GameScreen.TurnPhase.DISTRIBUTE) {
            bitmapFont.setColor(DrawHelper.getCellColorByType(Player.current().getType()));
            bitmapFont.draw(batch, "(" +Player.current().getPowerToDistribute() + ") ", current.getWidth() - 25, current.getHeight()/2);
        }
    }
}
