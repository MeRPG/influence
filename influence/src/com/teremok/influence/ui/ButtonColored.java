package com.teremok.influence.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Alexx on 19.01.14
 */
public class ButtonColored extends ColoredPanel implements Button {

    private String label;
    private BitmapFont font;

    private Color labelColor;

    private float labelX;
    private float labelY;

    public ButtonColored(String label, BitmapFont font, Color labelColor, Color color, float x, float y, float width, float height) {
        super(color, x, y, width, height);
        this.label = label;
        this.labelColor = labelColor;
        this.font = font;

        BitmapFont.TextBounds bounds = font.getBounds(label);
        labelX = (width - bounds.width) / 2;
        labelY = height/2 + bounds.height/2;
    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        labelColor.a = getColor().a;
        font.setColor(labelColor);
        font.draw(batch, label, getX() + labelX, getY() + labelY);

    }

    @Override
    public String getCode() {
        return label;
    }

    // Auto-generated

    public String getLabel() {
        return label;
    }
}
