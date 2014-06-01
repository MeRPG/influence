package com.teremok.influence.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Array;
import com.teremok.influence.screen.AbstractScreen;

/**
 * Created by Алексей on 26.05.2014
 */
public class TextureNumber extends Actor {

    int number;
    Array<TextureAtlas.AtlasRegion> regions = new Array<>();
    float[] positions;
    boolean rate;
    boolean minus;
    float textWidth;
    float leftPadding;

    public TextureNumber(int number, float x, float y) {
        setX(x);
        setY(y);
        this.number = number;
        setTouchable(Touchable.disabled);
    }

    public TextureNumber(int number, float x, float y, boolean rate, boolean minus) {
        this(number, x, y);
        this.rate = rate;
        this.minus = minus;
    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        Color beforeColor = batch.getColor();
        batch.setColor(getColor());
        for (int i = 0; i < regions.size; i++) {
            batch.draw(regions.get(i), getX()+positions[i]-leftPadding, getY());
        }
        batch.setColor(beforeColor);
    }

    public void addRegion(TextureAtlas.AtlasRegion region) {
        regions.add(region);
    }

    public void setPositions(float[] positions) {
        this.positions = positions;
    }

    public float getTextWidth() {
        return textWidth;
    }

    public void setTextWidth(float textWidth) {
        this.textWidth = textWidth;
    }

    public void centre() {
        setX(AbstractScreen.WIDTH/2 -textWidth/2);
    }

    public float getLeftPadding() {
        return leftPadding;
    }

    public void setLeftPadding(float leftPadding) {
        this.leftPadding = leftPadding;
    }
}
