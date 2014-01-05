package com.teremok.influence.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.teremok.influence.model.Cell;

/**
 * Created by Alexx on 23.12.13
 */
public class CellDrawer extends AbstractDrawer<Cell> {

    private static final float STANDARD_SIZE_MULTIPLIER = 0.4f;
    private static final float BIG_SIZE_MULTIPLIER = 0.6f;

    private TextureAtlas atlas;
    Array<TextureAtlas.AtlasRegion> cellSmall;
    Array<TextureAtlas.AtlasRegion> cellBig;
    TextureRegion maskSmall;
    TextureRegion maskBig;

    public CellDrawer() {
        super();

        atlas = new TextureAtlas(Gdx.files.internal("gameScreen.pack"));

        for (Texture txt : atlas.getTextures()) {
            txt.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }
        cellBig = atlas.findRegions("big");

        cellSmall = atlas.findRegions("small");

        maskSmall = atlas.findRegion("maskSmall");
        maskBig = atlas.findRegion("maskBig");

    }

    @Override
    public void draw(Cell cell, SpriteBatch batch, float parentAlpha) {
        super.draw(cell, batch, parentAlpha);

        Color color = Drawer.getCellColorByType(current.getType());
        renderer.setColor(color);
        batch.setColor(color);


        if (current.isBig()) {
            drawBigCell(batch);
        } else {
            drawSmallCell(batch);
        }

        batch.end();

        //drawBoundingBox();

        renderer.begin(ShapeRenderer.ShapeType.Line);
        if (current.isSelected()) {
            renderer.setColor(Color.WHITE);
            renderer.circle(current.getWidth()/2, current.getHeight()/2, Drawer.UNIT_SIZE * BIG_SIZE_MULTIPLIER);
        }
        renderer.end();

        batch.begin();

        if (bitmapFont != null) {
            BitmapFont.TextBounds textBounds = bitmapFont.getBounds(current.getPower()+"");
            bitmapFont.setColor(Color.BLACK);
            bitmapFont.draw(batch, current.getPower()+"", current.getX()+current.getWidth()/2 - textBounds.width/2,
                                                            current.getY()+current.getHeight()/2 + textBounds.height/2);
        }
    }

    private void drawBigCell(SpriteBatch batch) {
        batch.draw(maskBig, current.getX(), current.getY());
        batch.setColor(Color.BLACK);
        batch.draw(cellBig.get(current.getPower()), current.getX(), current.getY());
    }

    private void drawSmallCell(SpriteBatch batch) {
        batch.draw(maskSmall, current.getX(), current.getY());
        batch.setColor(Color.BLACK);
        batch.draw(cellSmall.get(current.getPower()), current.getX(), current.getY());
    }

    private void renderWithMultiplier(float multiplier) {
        float centerX = current.getWidth()/2;
        float centerY = current.getHeight()/2;

        renderer.circle(centerX, centerY, Drawer.UNIT_SIZE * multiplier, 6);

        renderer.setColor(Color.BLACK);

        float powerArc = (float)current.getPower() / current.getMaxPower() * 360f;
        renderer.arc(centerX, centerY, Drawer.UNIT_SIZE * multiplier * .7f, 90f, powerArc);

        renderer.setColor(Drawer.getCellColorByType(current.getType()));
        renderer.circle(centerX, centerY, Drawer.UNIT_SIZE * multiplier * .6f);

    }
}
