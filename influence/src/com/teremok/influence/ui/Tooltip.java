package com.teremok.influence.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

/**
 * Created by Alexx on 27.12.13
 */
public class Tooltip extends Actor {

    int id;
    float delay;
    String message;
    BitmapFont font;
    Color color;

    public Tooltip(String message, BitmapFont font, Color color, float x, float y) {
        this.message = message;
        this.font = font;
        this.color = color;
        delay = 0;

        BitmapFont.TextBounds bounds = font.getBounds(message);

        setBounds(x, y, bounds.width, bounds.height);
    }

    public Tooltip(String message, BitmapFont font, Color color, float x, float y, float delay) {
        this(message, font, color, x, y);
        this.delay = delay;
    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        color.a = getColor().a;
        font.setColor(color);
        font.draw(batch, message, getX(), getY());
    }

    // Auto-generated

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
