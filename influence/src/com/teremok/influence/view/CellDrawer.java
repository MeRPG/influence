package com.teremok.influence.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.teremok.influence.model.Cell;

/**
 * Created by Alexx on 23.12.13
 */
public class CellDrawer extends AbstractDrawer<Cell> {

    private TextureAtlas atlas;
    Array<TextureAtlas.AtlasRegion> cellSmall;
    Array<TextureAtlas.AtlasRegion> cellBig;
    TextureRegion maskSmall;
    TextureRegion maskBig;
    TextureRegion backlightSmall;
    TextureRegion backlightBig;

    public CellDrawer() {
        super();

        atlas = new TextureAtlas(Gdx.files.internal("gameScreen.pack"));

        for (Texture txt : atlas.getTextures()) {
            txt.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }

        cellSmall = atlas.findRegions("small");
        cellBig = atlas.findRegions("big");

        maskSmall = atlas.findRegion("maskSmall");
        maskBig = atlas.findRegion("maskBig");

        backlightSmall = atlas.findRegion("backlightSmall");
        backlightBig = atlas.findRegion("backlightBig");
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

        if (bitmapFont != null) {
            BitmapFont.TextBounds textBounds = bitmapFont.getBounds(current.getPower()+"");
            bitmapFont.setColor(Color.BLACK);
            bitmapFont.draw(batch, current.getPower()+"", current.getX()+current.getWidth()/2 - textBounds.width/2,
                                                            current.getY()+current.getHeight()/2 + textBounds.height/2);
        }
    }

    private void drawBigCell(SpriteBatch batch) {

        if (current.isSelected()){
            batch.draw(backlightBig, current.getX(), current.getY());
        }

        batch.draw(maskBig, current.getX(), current.getY());
        batch.setColor(Color.WHITE);
        if (current.getType() == -1)  {
            batch.draw(cellBig.get(current.getMaxPower()), current.getX(), current.getY());
        } else {
            batch.draw(cellBig.get(current.getPower()), current.getX(), current.getY());
        }
    }

    private void drawSmallCell(SpriteBatch batch) {

        if (current.isSelected()){
            batch.draw(backlightSmall, current.getX(), current.getY());
        }

        batch.draw(maskSmall, current.getX(), current.getY());
        batch.setColor(Color.WHITE);
        if (current.getType() == -1)  {
            batch.draw(cellSmall.get(current.getMaxPower()), current.getX(), current.getY());
        } else {
            batch.draw(cellSmall.get(current.getPower()), current.getX(), current.getY());
        }
    }
}
