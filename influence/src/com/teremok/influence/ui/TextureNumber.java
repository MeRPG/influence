package com.teremok.influence.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Алексей on 26.05.2014
 */
public class TextureNumber extends Actor {

    int number;
    Array<TextureAtlas.AtlasRegion> regions = new Array<>();
    float[] positions;

    public TextureNumber(int number, float x, float y) {
        setX(x);
        setY(y);
        this.number = number;
        setTouchable(Touchable.disabled);
    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        for (int i = 0; i < regions.size; i++) {
            batch.draw(regions.get(i), getX()+positions[i], getY());
        }
    }

    public void addRegion(TextureAtlas.AtlasRegion region) {
        regions.add(region);
    }

    public void setPositions(float[] positions) {
        this.positions = positions;
    }
}
