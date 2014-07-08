package com.teremok.framework.ui.font;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * Created by Алексей on 08.07.2014
 */
public class FontInfo {

    public String name;
    public BitmapFont bitmap;
    public float scalex;
    public float scaley;
    public float scalexy;

    public BitmapFont prepare() {
        if (scalexy != 0) {
            bitmap.setScale(scalexy);
        } else if (scalex != 0 && scaley != 0) {
            bitmap.setScale(scalex, scaley);
        } else {
            bitmap.setScale(1);
        }
        return bitmap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FontInfo)) return false;

        FontInfo fontInfo = (FontInfo) o;

        return name.equals(fontInfo.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
