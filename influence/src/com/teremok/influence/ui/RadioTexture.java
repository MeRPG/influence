package com.teremok.influence.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Alexx on 15.02.14
 */
public class RadioTexture extends CheckboxTexture {
    public RadioTexture(String code, TextureRegion regionOn, TextureRegion regionOff, float x, float y) {
        super(code, regionOn, regionOff, x, y);
    }

    @Override
    public void check() {
        if (!checked)
            super.check();
    }
}
