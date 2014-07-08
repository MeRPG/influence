package com.teremok.framework.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;

/**
 * Created by Alexx on 25.02.14
 */
public class TexturePanel extends Actor implements UIElement {

    Image image;

    public TexturePanel(TextureRegion region, float x, float y) {
        setRegion(region);
        setX(x);
        setY(y);
    }

    public TexturePanel(UIElementParams params) {
        setRegion(params.region);
        params.parsed = true;
        setX(params.x);
        setY(params.y);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float resultAlpha = parentAlpha < getColor().a ? parentAlpha : getColor().a;
        batch.setColor(getColor());
        image.setColor(getColor());
        image.draw(batch, resultAlpha);
    }

    @Override
    public void setY(float y) {
        super.setY(y);
        image.setY(y);
    }

    @Override
    public float getX() {
        return image.getX();
    }

    @Override
    public void setX(float x) {
        super.setX(x);
        image.setX(x);
    }

    @Override
    public float getY() {
        return image.getY();
    }

    @Override
    public void setColor(float r, float g, float b, float a) {
        image.setColor(r, g, b, a);
        setColor(new Color(r, g, b, a));
    }

    public void setRegion(TextureRegion region) {
        image =  new Image(new TextureRegionDrawable(region));
        image.setScaling(Scaling.fit);
        image.setAlign(Align.center);
        image.setTouchable(Touchable.disabled);
    }

    @Override
    public void setColor(Color color) {
        super.setColor(color);
        image.setColor(color);
    }
}
