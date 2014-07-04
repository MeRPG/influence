package com.teremok.influence.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.teremok.influence.util.Localizator;

/**
 * Created by Alexx on 31.01.14
 */
public class Label extends Actor {

    public enum Align {
        LEFT, CENTER, RIGHT
    }

    private BitmapFont font;
    private String code;
    private Color labelColor;
    private Align align;

    boolean localized = false;

    public Label(String code, BitmapFont font, Color labelColor, float x, float y) {
        this.code = code;
        this.labelColor = labelColor;
        this.font = font;
        this.align = Align.LEFT;

        setX(x);
        setY(y);
        setWidth(getTextBounds().width);
        setHeight(getTextBounds().height);
    }

    public Label(String text, BitmapFont font, Color color, float x, float y, boolean localized) {
        this(text, font, color, x, y);
        this.localized = localized;
    }

    public Label(String text, BitmapFont font, Color color, float x, float y, boolean localized, Align align) {
        this(text, font, color, x, y, localized);
        this.align = align;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        labelColor.a = getColor().a < parentAlpha ? getColor().a : parentAlpha;
        font.setColor(labelColor);

        float x = getX();
        float y = getY();
        BitmapFont.TextBounds textBounds;

        switch (align) {
            case LEFT:
                x = getX();
                break;
            case CENTER:
                textBounds = getTextBounds();
                x = getX() - textBounds.width /2;
                break;
            case RIGHT:
                textBounds = getTextBounds();
                x = getX() - textBounds.width;
                break;
        }

        font.draw(batch, getText(), x, y);
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

    public Align getAlign() {
        return align;
    }

    public void setAlign(Align align) {
        this.align = align;
    }
}
