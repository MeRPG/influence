package com.teremok.influence.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Alexx on 15.02.14
 */
public class RadioTexture extends CheckboxTexture {

    public RadioTexture(String code, UIElementParams params) {
        this(code, params.region, params.region2, params.x, params.y);
    }

    public RadioTexture(UIElementParams params) {
        this(params.name, params.region, params.region2, params.x, params.y);
    }

    public RadioTexture(String code, TextureRegion regionOn, TextureRegion regionOff, float x, float y) {
        super(code, regionOn, regionOff, x, y);
    }

    @Override
    public void check() {
        if (!checked)
            super.check();
    }
}
