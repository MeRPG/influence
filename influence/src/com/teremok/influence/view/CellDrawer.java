package com.teremok.influence.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.teremok.influence.model.Cell;

/**
 * Created by Alexx on 23.12.13
 */
public class CellDrawer extends AbstractDrawer<Cell> {

    private TextureAtlas atlas;
    Array<TextureAtlas.AtlasRegion> cellSmall;
    Array<TextureAtlas.AtlasRegion> cellBig;
    Array<TextureAtlas.AtlasRegion> maskSmall;
    Array<TextureAtlas.AtlasRegion> maskBig;
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

        maskSmall = atlas.findRegions("maskSmall");
        maskBig = atlas.findRegions("maskBig");

        backlightSmall = atlas.findRegion("backlightSmall");
        backlightBig = atlas.findRegion("backlightBig");
    }

    @Override
    public void draw(Cell cell, SpriteBatch batch, float parentAlpha) {
        super.draw(cell, batch, parentAlpha);




        /*
        if (current.isBig()) {
            drawCell(batch, backlightBig, maskBig, cellBig);
        } else {
            drawCell(batch, backlightSmall, maskSmall, cellSmall);
        }
        */
        //drawBacklight(batch);
        drawCellShape(batch);

        if (bitmapFont != null) {
            BitmapFont.TextBounds textBounds = bitmapFont.getBounds(current.getPower()+"");
            bitmapFont.setColor(Drawer.BACKGROUND_COLOR);
            bitmapFont.draw(batch, current.getPower()+"", current.getX()+current.getWidth()/2 - textBounds.width/2,
                                                            current.getY()+current.getHeight()/2 + textBounds.height/2);
        }
    }

    private void drawCell(SpriteBatch batch, TextureRegion backlight,
                            Array<TextureAtlas.AtlasRegion> masks,
                            Array<TextureAtlas.AtlasRegion> cells) {
        int type = current.getType();
        int power = current.getPower();

        if (current.isSelected()){
            Color color = Color.WHITE.cpy();
            color.a = 0.4f;
            batch.setColor(color);

            batch.draw(backlight, current.getX(), current.getY());

            batch.setColor(Color.WHITE);
        }
        batch.draw(masks.get(type+1), current.getX(), current.getY());
        batch.setColor(Color.WHITE);
        batch.draw(cells.get(power), current.getX(), current.getY());
    }

    private void drawBacklight(SpriteBatch batch){
        if (current.isSelected()){
            TextureRegion backlight;
            if (current.isBig()) {
                backlight = backlightBig;
            } else {
                backlight = backlightSmall;
            }

            Color color = Drawer.getCellColorByType(current.getType());
            batch.setColor(color);

            batch.draw(backlight, current.getX(), current.getY());

            batch.setColor(Color.WHITE);
        }
    }

    private void drawCellShape(SpriteBatch batch) {
        batch.end();

        float centerX = current.getWidth()/2;
        float centerY = current.getHeight()/2;

        Color color = Drawer.getCellColorByType(current.getType()).cpy();

        if (current.isSelected()) {
            color.add(0.3f, 0.3f, 0.3f, 0f);
        }

        renderer.setColor(color);

        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.circle(centerX, centerY, Drawer.UNIT_SIZE * (0.4f + current.getMaxPower()*0.03f), 6);
        renderer.end();

        renderer.setColor(color);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.circle(centerX, centerY, Drawer.UNIT_SIZE * (0.4f + current.getPower()*0.03f), 6);
        renderer.end();

        batch.begin();
    }
}
