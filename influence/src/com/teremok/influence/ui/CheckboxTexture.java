package com.teremok.influence.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashSet;
import java.util.List;

/**
 * Created by Alexx on 08.02.14
 */
public class CheckboxTexture extends Checkbox {

    TextureRegion region;
    TextureRegion regionOn;
    TextureRegion regionOff;

    public CheckboxTexture(String code, TextureRegion regionOn, TextureRegion regionOff, float x, float y) {
        this.code = code;
        region = regionOn;
        this.regionOn = regionOn;
        this.regionOff = regionOff;

        setBounds(x, y, region.getRegionWidth(), region.getRegionHeight());
    }

    @Override
    public void check() {
        super.check();
        region = regionOn;
    }

    @Override
    public void unCheck() {
        super.unCheck();
        region = regionOff;
    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        super.draw(batch, parentAlpha);
        batch.draw(region, getX(), getY());
    }
}
