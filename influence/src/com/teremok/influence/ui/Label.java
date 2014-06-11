package com.teremok.influence.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.teremok.influence.model.Localizator;

/**
 * Created by Alexx on 31.01.14
 */
public class Label extends Actor {

    private BitmapFont font;
    private String code;
    private Color labelColor;

    public Label(String code, BitmapFont font, Color labelColor, float x, float y) {
        this.code = code;
        this.labelColor = labelColor;
        this.font = font;

        setX(x);
        setY(y);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        labelColor.a = getColor().a < parentAlpha ? getColor().a : parentAlpha;
        font.setColor(labelColor);
        font.draw(batch, Localizator.getString(code), getX(), getY());
    }
}
