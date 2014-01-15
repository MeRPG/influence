package com.teremok.influence.view;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.teremok.influence.model.Cell;
import com.teremok.influence.model.player.PlayerManager;
import com.teremok.influence.model.player.Player;
import com.teremok.influence.model.Score;
import com.teremok.influence.screen.AbstractScreen;

import static com.teremok.influence.view.Drawer.UNIT_SIZE;

/**
 * Created by Alexx on 24.12.13
 */
public class ScoreDrawer extends AbstractDrawer<Score> {

    private static float LEFT_MARGIN = UNIT_SIZE * 0.8f;
    private static float RIGHT_MARGIN = UNIT_SIZE * 1.1f;
    private static float TEXT_PADDING = UNIT_SIZE * 1.6f;


    float[] newWidth = new float[5];

    @Override
    public void draw(Score score, SpriteBatch batch, float parentAlpha) {
        super.draw(score, batch, parentAlpha);

        batch.end();

        drawBoundingBox();

        batch.begin();

        drawStatusString(batch);
        //drawScores(batch);
        drawPower(batch);
    }

    @Override
    protected void drawBoundingBox() {

        PlayerManager pm = current.getPm();

        renderer.setColor(Drawer.getBackgroundColor());
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.rect(0,0,current.getWidth(), current.getHeight());
        renderer.end();

        float lastWidth = 0;
        float myWidth = 0;
        int totalScore = pm.getTotalScore();

        for (int i = 0; i < pm.getNumberOfPlayers(); i++) {
            myWidth = (AbstractScreen.WIDTH-1)*((float) (pm.getPlayers()[i]).getScore() / totalScore);
            newWidth[i] = myWidth;
            renderer.setColor(Drawer.getCellColorByType(i));
            renderer.begin(ShapeRenderer.ShapeType.Filled);
            renderer.rect(lastWidth, current.getHeight()-24f, myWidth, 24f);
            renderer.end();

            lastWidth += myWidth;
        }
    }

    private void drawScores(SpriteBatch batch) {
        PlayerManager pm = current.getPm();

        for (int i = 0; i < pm.getNumberOfPlayers(); i++) {
            Player player = pm.getPlayers()[i];
            if (player.getScore() == 0) {
                bitmapFont.setColor(Drawer.getCellColorByType(-1));
            } else {
                bitmapFont.setColor(Drawer.getCellColorByType(player.getType()));
            }
            String toDraw;
            if (i == pm.current().getType()) {
                toDraw = "-" + player.getScore() + "-";
            } else {
                toDraw = " " + player.getScore() + " ";
            }
            bitmapFont.draw(batch, toDraw, LEFT_MARGIN + i*TEXT_PADDING, current.getHeight()/2-16f);
        }
    }

    private void drawPower(SpriteBatch batch) {
        PlayerManager pm = current.getPm();
        if (current.getMatch().isInDistributePhase()) {
            bitmapFont.setColor(Drawer.getCellColorByType(pm.current().getType()));
            String text = "(" +pm.current().getPowerToDistribute() + ") ";
            bitmapFont.draw(batch, text, current.getWidth() - RIGHT_MARGIN, current.getHeight()/2);
        }
    }

    private void drawStatusString(SpriteBatch batch) {
        BitmapFont.TextBounds bounds = bitmapFont.getBounds(current.getStatus());
        float x = current.getX() + (current.getWidth() - bounds.width)/2;
        float y = current.getY() + (current.getHeight() - 24f)/2;
        bitmapFont.setColor(Drawer.getTextColor());
        bitmapFont.draw(batch, current.getStatus(), x, y);
    }
}
