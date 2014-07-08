package com.teremok.influence.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.teremok.framework.ui.UIElementParams;

/**
 * Created by Alexx on 15.02.14
 */
public class RadioTexture extends CheckboxTexture {

    public RadioTexture(UIElementParams params) {
        this(params.name, params.region, params.region2, params.x, params.y);
        params.parsed = true;
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
