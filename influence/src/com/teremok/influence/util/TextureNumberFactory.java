package com.teremok.influence.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.teremok.influence.Influence;
import com.teremok.influence.ui.TextureNumber;

/**
 * Created by Алексей on 26.05.2014
 */
public class TextureNumberFactory {

    enum CompareResult {
        BAD, GOOD, AVERAGE
    }

    private final int PERCENT_DIGIT = 10;
    private final int POINT_DIGIT = 11;
    private final int QUOTE_DIGIT = 12;
    private final int MINUS_DIGIT = 13;
    private final int COMMA_DIGIT = 14;

    private final int LEFT_DIGIT_SIDE = 1;
    private final int RIGHT_DIGIT_SIDE = 2;

    private final float REGION_WIDTH = 91f;

    public final Color BAD_COLOR = new Color(0xde3535FF);
    public final Color GOOD_COLOR = new Color(0x48d827FF);
    public final Color NORMAL_COLOR = new Color(0x35b7deFF);
    public final Color AVERAGE_COLOR = new Color(0xe9bd11FF);

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
            { 9, 37, 38},
            { 10, 35, 35},
            { 11, 43, 44},
            { 12, 44, 43},
            { 13, 40, 40},
            { 13, 44, 43}
    };

    Array<TextureAtlas.AtlasRegion> numbers;
    TextureNumber lastNumber;


    int lastDigit;
    float[] positions;

    public TextureNumber getNumber(int number, float x, float y, boolean percents) {

        if (numbers == null) {
            ResourceManager resourceManager = ((Influence) Gdx.app.getApplicationListener()).getResourceManager();
            TextureAtlas atlas = resourceManager.getAtlas("numbers");
            numbers = atlas.findRegions("number");
        }
        String stringNumber = Integer.toString(Math.abs(number));
        int quotes = stringNumber.length()/3;

        if (quotes > 0) {
            String numberWithQuotes = "";
            for (int i = 0; i < stringNumber.length(); i++) {
                if ((stringNumber.length()-i) % 3 == 0 && i != 0) {
                    numberWithQuotes += "'";
                }
                numberWithQuotes += stringNumber.charAt(i);
            }
            Gdx.app.debug(getClass().getSimpleName(), "Number, string: " + stringNumber);
            stringNumber = numberWithQuotes;
            Gdx.app.debug(getClass().getSimpleName(), "Number, quoted: " + stringNumber);
            Gdx.app.debug(getClass().getSimpleName(), "Quotes: " + quotes);
        }

        char[] chars = stringNumber.toCharArray();

        float leftPadding =  sizes[chars[0]-'0'][LEFT_DIGIT_SIDE];
        lastNumber = new TextureNumber(number, x, y);
        int firstDigit = chars[0]-'0';
        int length = chars.length;
        if (number < 0)
            length++;
        if (percents)
            length++;

        positions = new float[length];
        int i = 0;
        float position = 0f;
        if (number < 0) {
            positions[0] = 0f;
            lastNumber.addRegion(numbers.get(MINUS_DIGIT));
            i++;
            lastDigit = MINUS_DIGIT;
            firstDigit = MINUS_DIGIT;
            leftPadding = sizes[MINUS_DIGIT][LEFT_DIGIT_SIDE];
        }
        for (char ch : chars) {
            int digit = ch - '0';
            if (digit < 0) {
                if (Localizator.getLanguage().equals(Localizator.LANGUAGE_ENGLISH)) {
                    digit = COMMA_DIGIT;
                } else {
                    digit = QUOTE_DIGIT;
                }
            }
            if (i == 0) {
                position = 0;
            } else {
                position += REGION_WIDTH - sizes[lastDigit][RIGHT_DIGIT_SIDE] + 4 - sizes[digit][LEFT_DIGIT_SIDE];

            }
            positions[i] = position;
            i++;
            lastNumber.addRegion(numbers.get(digit));
            lastDigit = digit;
        }

        if (percents) {
            position += REGION_WIDTH - sizes[lastDigit][RIGHT_DIGIT_SIDE] + 4 - sizes[PERCENT_DIGIT][LEFT_DIGIT_SIDE];
            positions[i] = position;
            lastNumber.addRegion(numbers.get(PERCENT_DIGIT));
            lastDigit = PERCENT_DIGIT;
        }

        float textWidth = position + REGION_WIDTH - sizes[lastDigit][RIGHT_DIGIT_SIDE] - leftPadding;
        lastNumber.setTextWidth(textWidth);
        lastNumber.setColor(NORMAL_COLOR);
        lastNumber.setPositions(positions);
        lastNumber.setLeftPadding(leftPadding);
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
        if (inRange(a,b,0.3f)) {
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
