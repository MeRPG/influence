package com.teremok.influence.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Alexx on 08.02.14
 */
public class CheckboxTexture extends Checkbox {

    TextureRegion region;
    TextureRegion regionOn;
    TextureRegion regionOff;

    public CheckboxTexture(String code, UIElementParams params) {
        this(code, params.region, params.region2, params.x, params.y);
    }

    public CheckboxTexture(UIElementParams params) {
        this(params.name, params.region, params.region2, params.x, params.y);
    }

    public CheckboxTexture(String code, TextureRegion regionOn, TextureRegion regionOff, float x, float y) {
        this.code = code;
        region = regionOn;
        this.regionOn = regionOn;
        this.regionOff = regionOff;

        setBounds(x, y, region.getRegionWidth(), region.getRegionHeight());
    }


    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (checked) {
            region = regionOn;
        } else {
            region = regionOff;
        }
        batch.draw(region, getX(), getY());
    }
}
