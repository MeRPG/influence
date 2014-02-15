package com.teremok.influence.ui;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by Alexx on 15.02.14
 */
public class RadioColored extends CheckboxColored {

    public RadioColored(String code, Color colorOn, Color colorOff, float x, float y, float width, float height) {
        super(code, colorOn, colorOff, x, y, width, height);
    }

    @Override
    public void check() {
        if (! checked) {
            super.check();
        }
    }
}
