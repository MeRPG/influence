package com.teremok.influence.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

    public TexturePanel(UIElementParams params) {
        image =  new Image(new TextureRegionDrawable(params.region));
        image.setScaling(Scaling.fit);
        image.setAlign(Align.center);
        image.setTouchable(Touchable.disabled);
        params.parsed = true;
        setX(params.x);
        setY(params.y);
    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        image.draw(batch, parentAlpha);
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
    }
}
