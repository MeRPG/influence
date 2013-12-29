package com.teremok.influence.view;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.teremok.influence.model.player.Player;
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
        for (int i = 0; i < Player.getNumberOfPlayers(); i++) {
            Player player = Player.getPlayers()[i];
            if (player.getScore() == 0) {
                bitmapFont.setColor(DrawHelper.getCellColorByType( -1 ));
            } else {
                bitmapFont.setColor(DrawHelper.getCellColorByType(player.getType()));
            }
            String toDraw;
            if (i == Player.current().getType()) {
                toDraw = "-" + player.getScore() + "-";
            } else {
                toDraw = " " + player.getScore() + " ";
            }
            bitmapFont.draw(batch, toDraw, 25f + i*50f, current.getHeight()/2);
        }
    }

    private void drawPower(SpriteBatch batch) {
        if (GameScreen.currentPhase == GameScreen.TurnPhase.DISTRIBUTE) {
            bitmapFont.setColor(DrawHelper.getCellColorByType(Player.current().getType()));
            bitmapFont.draw(batch, "(" +Player.current().getPowerToDistribute() + ") ", current.getWidth() - 35, current.getHeight()/2);
        }
    }
}
