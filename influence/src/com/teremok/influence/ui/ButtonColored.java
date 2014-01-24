package com.teremok.influence.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.teremok.influence.model.Localizator;

/**
 * Created by Alexx on 19.01.14
 */
public class ButtonColored extends ColoredPanel implements Button {

    private String code;
    private BitmapFont font;

    private Color labelColor;

    private float labelX;
    private float labelY;

    public ButtonColored(String code, BitmapFont font, Color labelColor, Color color, float x, float y, float width, float height) {
        super(color, x, y, width, height);
        this.code = code;
        this.labelColor = labelColor;
        this.font = font;
    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        BitmapFont.TextBounds bounds = font.getBounds(Localizator.getString(code));
        labelX = (getWidth() - bounds.width) / 2;
        labelY = getHeight()/2 + bounds.height/2;

        labelColor.a = getColor().a;
        font.setColor(labelColor);
        font.draw(batch, Localizator.getString(code), getX() + labelX, getY() + labelY);

    }

    @Override
    public String getCode() {
        return code;
    }
}
