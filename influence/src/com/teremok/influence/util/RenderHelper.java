package com.teremok.influence.util;

import com.badlogic.gdx.graphics.Color;
import com.teremok.influence.model.World;

/**
 * Created by Alexx on 23.12.13
 */
public class RenderHelper {

    public static final float UNIT_SIZE = 32f;

    public static final float SCREEN_WIDTH = UNIT_SIZE*10f; // 320px
    public static final float SCREEN_HEIGHT = UNIT_SIZE*15f; // 348px

    public static final float FIELD_WIDTH = UNIT_SIZE*10f;
    public static final float FIELD_HEIGHT = UNIT_SIZE*13f;

    public static Color getCellColorByType (int type) {
            Color color;
            switch (type) {
                case 0:
                    color = Color.CYAN;
                    break;
                case 1:
                    color = Color.GREEN;
                    break;
                case 2:
                    color = Color.ORANGE;
                    break;
                case 3:
                    color = Color.PINK;
                    break;
                case 4:
                    color = Color.MAGENTA;
                    break;
                default:
                    color = Color.GRAY;
                    break;
            }
            return color;
    }


    public static float getSqX(int i) {
        float sqWidth = getSqWidth();
        return (i+1) * sqWidth - sqWidth/2;
    }

    public static float getSqY(int j) {
        float sqHeight = getSqHeight();
        return (j+1) * sqHeight - sqHeight/2;
    }

    public static float getSqHeight() {
        return FIELD_HEIGHT / World.MAX_CELLS_Y;
    }

    public static float getSqWidth() {
        return FIELD_WIDTH / World.MAX_CELLS_X;
    }

    public static float shiftY(float sqY, int i) {
        if (i%2 == 1) {
            sqY += getSqHeight() / 2;
        }
        return sqY;
    }

    public static float shiftX(float sqX, int j) {
        if (j%2 == 1) {
            sqX += getSqWidth() / 2;
        }
        return sqX;
    }

    public static float reflectY(float y) {
        return RenderHelper.SCREEN_HEIGHT - y;
    }
}