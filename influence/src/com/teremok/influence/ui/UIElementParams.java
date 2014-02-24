package com.teremok.influence.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Alexx on 23.02.14
 */
public class UIElementParams {
    public String name;
    public boolean localized;
    public TextureRegion region;
    public TextureRegion region2;
    public float x;
    public float y;
    public boolean parsed;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UIElementParams)) return false;

        UIElementParams that = (UIElementParams) o;

        if (localized != that.localized) return false;
        if (Float.compare(that.x, x) != 0) return false;
        if (Float.compare(that.y, y) != 0) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (localized ? 1 : 0);
        result = 31 * result + (x != +0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UIElementParams{" +
                "name='" + name + '\'' +
                ", localized=" + localized +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
