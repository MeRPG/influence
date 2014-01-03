package com.teremok.influence.view;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.teremok.influence.model.player.Player;
import com.teremok.influence.model.Score;
import com.teremok.influence.model.player.PlayerManager;
import com.teremok.influence.screen.GameScreen;

/**
 * Created by Alexx on 24.12.13
 */
public class ScoreDrawer extends AbstractDrawer<Score> {

    private static float LEFT_MARGIN = 25f;
    private static float RIGHT_MARGIN = 35f;
    private static float TEXT_PADDING = 50f;

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
        for (int i = 0; i < PlayerManager.getNumberOfPlayers(); i++) {
            Player player = PlayerManager.getPlayers()[i];
            if (player.getScore() == 0) {
                bitmapFont.setColor(Drawer.getCellColorByType(-1));
            } else {
                bitmapFont.setColor(Drawer.getCellColorByType(player.getType()));
            }
            String toDraw;
            if (i == PlayerManager.current().getType()) {
                toDraw = "-" + player.getScore() + "-";
            } else {
                toDraw = " " + player.getScore() + " ";
            }
            bitmapFont.draw(batch, toDraw, LEFT_MARGIN + i*TEXT_PADDING, current.getHeight()/2);
        }
    }

    private void drawPower(SpriteBatch batch) {
        if (GameScreen.currentPhase == GameScreen.TurnPhase.DISTRIBUTE) {
            bitmapFont.setColor(Drawer.getCellColorByType(PlayerManager.current().getType()));
            String text = "(" +PlayerManager.current().getPowerToDistribute() + ") ";
            bitmapFont.draw(batch, text, current.getWidth() - RIGHT_MARGIN, current.getHeight()/2);
        }
    }
}
