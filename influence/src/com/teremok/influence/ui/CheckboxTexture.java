package com.teremok.influence.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.teremok.framework.ui.UIElementParams;

/**
 * Created by Alexx on 08.02.14
 */
public class CheckboxTexture extends Checkbox {

    TextureRegion region;
    TextureRegion regionOn;
    TextureRegion regionOff;

    public CheckboxTexture(UIElementParams params) {
        this(params.name, params.region, params.region2, params.x, params.y);

        params.parsed = true;
    }

    public CheckboxTexture(String code, TextureRegion regionOn, TextureRegion regionOff, float x, float y) {
        this.code = code;
        region = regionOn;
        this.regionOn = regionOn;
        this.regionOff = regionOff;

        setBounds(x, y, region.getRegionWidth(), region.getRegionHeight());
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (checked) {
            region = regionOn;
        } else {
            region = regionOff;
        }
        batch.draw(region, getX(), getY());
    }
}
