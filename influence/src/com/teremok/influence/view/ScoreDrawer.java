package com.teremok.influence.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.teremok.influence.model.Localizator;
import com.teremok.influence.model.Score;

import static com.teremok.influence.view.Drawer.getCellColorByType;

/**
 * Created by Alexx on 24.12.13
 */
public class ScoreDrawer extends AbstractDrawer<Score> {

    @Override
    public void draw(Score score, SpriteBatch batch, float parentAlpha) {
        super.draw(score, batch, parentAlpha);

        batch.end();

        //drawBoundingBox();

        batch.begin();

        drawStatusString(batch);
    }

    @Override
    protected void drawBoundingBox() {

        Gdx.gl.glEnable(GL10.GL_BLEND);
        Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        renderer.setColor(Drawer.getBackgroundColor());
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.rect(0,0,current.getWidth(), current.getHeight());
        renderer.end();
    }

    private void drawStatusString(SpriteBatch batch) {

        String colorString = Localizator.getString("ofYourColor");

        BitmapFont.TextBounds bounds = bitmapFont.getBounds(current.getStatus());

        float Z = current.getWidth();
        float X = bounds.width;

        float x = current.getX() + (Z - X)/2;
        float y = current.getY() + 16 + (current.getHeight() - 24f + bounds.height)/2;

        // если ход игрок и ничего не выбрано
        if ((current.getMatch().isInAttackPhase() && current.getMatch().getField().getSelectedCell() == null) && current.getPm().isHumanActing()) {
            BitmapFont.TextBounds colorBounds = bitmapFont.getBounds(colorString);
            float Y = colorBounds.width;
            x = x - Y/2;
            bitmapFont.setColor(Drawer.getTextColor());
            bitmapFont.draw(batch, current.getStatus(), x, y);
            bitmapFont.setColor(getCellColorByType(current.getPm().current().getType()));
            bitmapFont.draw(batch, colorString, x + X, y);

        } else if (current.getMatch().isInDistributePhase() && current.getPm().isHumanActing()) {
            String powerString = " (" +current.getPm().current().getPowerToDistribute() + ")";
            BitmapFont.TextBounds colorBounds = bitmapFont.getBounds(colorString);
            float Y = colorBounds.width;
            BitmapFont.TextBounds powerBounds = bitmapFont.getBounds(powerString);
            float W = powerBounds.width;
            x = x - Y/2 - W/2;
            bitmapFont.setColor(Drawer.getTextColor());
            bitmapFont.draw(batch, current.getStatus(), x, y);
            bitmapFont.setColor(getCellColorByType(current.getPm().current().getType()));
            bitmapFont.draw(batch, colorString, x + X, y);
            bitmapFont.draw(batch, powerString, x + X + Y, y);

        } else {

            bitmapFont.setColor(Drawer.getTextColor());
            bitmapFont.draw(batch, current.getStatus(), x, y);
        }
    }
}
