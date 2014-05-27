package com.teremok.influence.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.teremok.influence.ui.TextureNumber;

/**
 * Created by Алексей on 26.05.2014
 */
public class TextureNumberFactory {

    enum CompareResult {
        BAD, GOOD, AVERAGE
    }

    private final int LEFT_DIGEST_SIDE = 1;
    private final int RIGHT_DIGEST_SIDE = 2;

    private final float REGION_WIDTH = 91f;

    private final Color BAD_COLOR = new Color(Color.RED);
    private final Color GOOD_COLOR = new Color(Color.GREEN);
    private final Color NORMAL_COLOR = new Color(Color.CYAN);
    private final Color AVERAGE_COLOR = new Color(Color.ORANGE);

    private final float[][] sizes = {
            { 0, 37, 37},
            { 1, 41, 42},
            { 2, 37, 37},
            { 3, 37, 38},
            { 4, 35, 36},
            { 5, 37, 38},
            { 6, 37, 38},
            { 7, 37, 37},
            { 8, 37, 37},
            { 9, 37, 38}
    };

    Array<TextureAtlas.AtlasRegion> numbers;
    TextureNumber lastNumber;


    int lastDigit;
    float[] positions;

    public TextureNumber getNumber(int number, float x, float y) {

        if (numbers == null) {
            TextureAtlas atlas = ResourceManager.getAtlas("numbers");
            numbers = atlas.findRegions("number");
        }
        char[] chars = Integer.toString(number).toCharArray();
        lastNumber = new TextureNumber(number, x - sizes[chars[0]-'0'][LEFT_DIGEST_SIDE], y);

        positions = new float[chars.length];
        int i = 0;
        float position = 0f;
        for (char ch : chars) {
            int digit = ch - '0';
            if (i == 0) {
                position = 0;
            } else {
                position += REGION_WIDTH - sizes[lastDigit][RIGHT_DIGEST_SIDE] + 4 - sizes[digit][LEFT_DIGEST_SIDE];

            }
            positions[i] = position;
            i++;
            lastNumber.addRegion(numbers.get(digit));
            lastDigit = digit;
        }

        lastNumber.setColor(NORMAL_COLOR);
        lastNumber.setPositions(positions);
        lastDigit = 0;
        return lastNumber;
    }

    public Color getCompareColorBad(int a, int b) {
        Color retColor = NORMAL_COLOR;
        switch (compareBad(a, b)) {
            case BAD:
                retColor = BAD_COLOR;
                break;
            case GOOD:
                retColor = GOOD_COLOR;
                break;
            case AVERAGE:
                retColor = AVERAGE_COLOR;
                break;
        }
        return retColor;
    }

    public Color getCompareColor(int a, int b) {
        Color retColor = NORMAL_COLOR;
        switch (compare(a, b)) {
            case BAD:
                retColor = BAD_COLOR;
                break;
            case GOOD:
                retColor = GOOD_COLOR;
                break;
            case AVERAGE:
                retColor = AVERAGE_COLOR;
                break;
        }
        return retColor;
    }

    private CompareResult compare(int a, int b) {
        if (inRange(a,b,0.5f)) {
            return CompareResult.AVERAGE;
        }
        if (a < b) {
            return CompareResult.BAD;
        }
        if (a > b) {
            return CompareResult.GOOD;
        }
        return CompareResult.AVERAGE;
    }

    private CompareResult compareBad(int a, int b) {
        if (inRange(a,b,0.5f)) {
            return CompareResult.AVERAGE;
        }
        if (a < b) {
            return CompareResult.GOOD;
        }
        if (a > b) {
            return CompareResult.BAD;
        }
        return CompareResult.AVERAGE;
    }

    private boolean inRange(float a, float b, float range) {
        return (a >  b-b*range) && (a < b+b*range);
    }
}
