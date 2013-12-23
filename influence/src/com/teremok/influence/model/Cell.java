package com.teremok.influence.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.teremok.influence.util.RenderHelper;

import java.util.Random;

/**
 * Created by Alexx on 12.12.13.
 */
public class Cell extends Actor {

    private static final float STANDARD_SIZE_MULTIPLIER = 0.4f;
    private static final float BIG_SIZE_MULTIPLIER = 0.6f;

    static private final int POWER_BIG = 12;
    static private final int POWER_STANDARD = 8;

    static private final float BIG_POSSIBILITY = 0.3f;

    static private final int MAX_TYPE = 4;

    // доступные места для ячеек
    private boolean[] availiable;

    // для обхода графа
    boolean marked;

    // координаты на сетке
    int unitsX, unitsY;

    // порядковый номер
    int number;

    // тип (цвет)
    int type;

    // мощность - количество секторов
    int power;

    // максимальное количество секторов
    int maxPower;

    boolean selected;

    private static ShapeRenderer renderer = new ShapeRenderer();
    BitmapFont bitmapFont;

    private Cell() {
    }

    private Cell(int number) {
        this();
        this.number = number;
    }

    private Cell(int number, int unitsX, int unitsY) {
        this(number);
        this.unitsX = unitsX;
        this.unitsY = unitsY;

        float actorWidth = Field.WIDTH / 5;
        float actorHeight = Field.HEIGHT / 7;

        float actorX;
        if (unitsX%2 == 1) {
            actorX = unitsY * actorWidth + actorWidth / 2;
        } else {
            actorX = unitsY* actorWidth;
        }
        float actorY = unitsX * actorHeight;

        setBounds(actorX, actorY, actorWidth, actorHeight);
    }

    private Cell(int number, int unitsX, int unitsY, int type) {
        this(number, unitsX, unitsY);
        this.type = type;
    }

    public static Cell makeRandomCell(int number, int x, int y) {
        Cell cell = new Cell(number, x, y);
        Random rnd = new Random();
        cell.setType(rnd.nextInt(MAX_TYPE + 1));
        cell.setMaxPower( rnd.nextFloat() > BIG_POSSIBILITY ? POWER_STANDARD : POWER_BIG);
        cell.setPower(1 + rnd.nextInt(cell.maxPower-1));


        cell.setWidth(RenderHelper.UNIT_SIZE*2);
        cell.setHeight(RenderHelper.UNIT_SIZE*2);

        return cell;
    }

    public static Cell makeInvalidCell() {
        return new Cell(-1, 0, 0);
    }

    public int getUnitsX() {
        return unitsX;
    }

    public int getUnitsY() {
        return unitsY;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "unitsX=" + unitsX +
                ", unitsY=" + unitsY +
                ", number=" + number +
                '}';
    }

    public boolean isValid() {
        if (number == -1)
            return false;
        if (unitsX < 0 || unitsY < 0 || unitsX >= Field.MAX_CELLS_Y || unitsY >= Field.MAX_CELLS_X)
            return false;

        return true;
    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        batch.end();

        renderer.setProjectionMatrix(batch.getProjectionMatrix());
        renderer.setTransformMatrix(batch.getTransformMatrix());
        renderer.translate(getX(), getY(), 0);
        Color color = RenderHelper.getCellColorByType(type);
        renderer.setColor(color);

        //drawBoundingBox();

        renderer.begin(ShapeRenderer.ShapeType.Filled);

        if (isBig()) {
            renderWithMultiplier(BIG_SIZE_MULTIPLIER);
        } else {
            renderWithMultiplier(STANDARD_SIZE_MULTIPLIER);
        }

        if (isSelected()) {
            renderer.setColor(Color.WHITE);
            renderer.circle(getWidth()/2, getHeight()/2, RenderHelper.UNIT_SIZE * 0.1f);
        }
        renderer.end();

        batch.begin();

        if (bitmapFont != null) {
            BitmapFont.TextBounds textBounds = bitmapFont.getBounds(power+"");
            bitmapFont.draw(batch, power+"", getX()+getWidth()/2 - textBounds.width/2, getY()+getHeight()/2 + textBounds.height/2);
        }

    }

    private void drawBoundingBox() {
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.rect(0,0,getWidth(), getHeight());
        renderer.end();
    }

    private void renderWithMultiplier(float multiplier) {
        float centerX = getWidth()/2;
        float centerY = getHeight()/2;

        renderer.circle(centerX, centerY, RenderHelper.UNIT_SIZE * multiplier, 6);

        renderer.setColor(Color.BLACK);

        float powerArc = (float)power / maxPower * 360f;
        renderer.arc(centerX, centerY, RenderHelper.UNIT_SIZE * multiplier * .7f, 90f, powerArc);

        renderer.setColor(RenderHelper.getCellColorByType(type));
        renderer.circle(centerX, centerY, RenderHelper.UNIT_SIZE * multiplier * .6f);

    }

    public void setBitmapFont(BitmapFont bitmapFont) {
        this.bitmapFont = bitmapFont;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getMaxPower() {
        return maxPower;
    }

    public void setMaxPower(int maxPower) {
        this.maxPower = maxPower;
    }

    public boolean isBig() {
        return maxPower == POWER_BIG;
    }

    static public int calcNumber(int x, int y) {
        return x + y*5;
    }

}
