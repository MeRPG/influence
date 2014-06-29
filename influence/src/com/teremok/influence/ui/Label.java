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

    boolean localized = false;

    public Label(String code, BitmapFont font, Color labelColor, float x, float y) {
        this.code = code;
        this.labelColor = labelColor;
        this.font = font;

        setX(x);
        setY(y);
        setWidth(getTextBounds().width);
        setHeight(getTextBounds().height);
    }

    public Label(String text, BitmapFont font, Color color, float x, float y, boolean localized) {
        this(text, font, color, x, y);
        this.localized = localized;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        font.setScale(0.5f, 0.4f);
        labelColor.a = getColor().a < parentAlpha ? getColor().a : parentAlpha;
        font.setColor(labelColor);
        font.draw(batch, getText(), getX(), getY());
        font.setScale(1f, 1f);
    }

    public BitmapFont.TextBounds getTextBounds() {
        return font.getBounds(getText());
    }

    public BitmapFont getFont() {
        return font;
    }

    public void setFont(BitmapFont font) {
        this.font = font;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Color getLabelColor() {
        return labelColor;
    }

    public void setLabelColor(Color labelColor) {
        this.labelColor = labelColor;
    }

    public boolean isLocalized() {
        return localized;
    }

    public void setLocalized(boolean localized) {
        this.localized = localized;
    }

    public String getText() {
        String text;
        if (localized) {
            text = Localizator.getString(code);
        } else {
            text = code;
        }
        return text;
    }
}
