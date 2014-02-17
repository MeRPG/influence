package com.teremok.influence.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.teremok.influence.view.Drawer;

import java.util.Random;

import static com.teremok.influence.model.Field.MAX_CELLS_X;
import static com.teremok.influence.model.Field.MAX_CELLS_Y;

/**
 * Created by Alexx on 12.12.13
 */
public class Cell extends Actor {

    static private final int POWER_BIG = 12;
    static private final int POWER_STANDARD = 8;

    static private final float BIG_POSSIBILITY = 0.3f;

    static public final int MAX_TYPE = 4;

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

        float actorWidth = Field.WIDTH / MAX_CELLS_X;
        float actorHeight = Field.HEIGHT / MAX_CELLS_Y;

        float actorX;
        if (unitsX%2 == 1) {
            actorX = unitsY * actorWidth + actorWidth / 2;
        } else {
            actorX = unitsY* actorWidth;
        }
        float actorY = unitsX * actorHeight-8f;

        setBounds(actorX, actorY, actorWidth, actorHeight);
    }

    public static Cell makeRandomCell(int number, int x, int y) {
        Cell cell = new Cell(number, x, y);
        Random rnd = new Random();
        cell.setType(rnd.nextInt(MAX_TYPE + 1));
        cell.setMaxPower( rnd.nextFloat() > BIG_POSSIBILITY ? POWER_STANDARD : POWER_BIG);
        cell.setPower(1 + rnd.nextInt(cell.maxPower-1));

        cell.setWidth(Drawer.UNIT_SIZE*2);
        cell.setHeight(Drawer.UNIT_SIZE*2);
        return cell;
    }

    public static Cell makeInvalidCell() {
        return new Cell(-1, 0, 0);
    }

    public static Cell makeEmptyCell(int number, int x, int y) {
        Cell cell = new Cell(number, x, y);
        Random rnd = new Random();
        cell.setType(-1);
        cell.setMaxPower( rnd.nextFloat() > BIG_POSSIBILITY ? POWER_STANDARD : POWER_BIG);

        cell.setWidth(Drawer.UNIT_SIZE*2);
        cell.setHeight(Drawer.UNIT_SIZE*2);

        return cell;
    }

    public boolean isValid() {
        if (number == -1) {
            return false;
        }
        if (unitsX < 0 || unitsY < 0) {
            return false;
        }
        if (unitsX >= Field.MAX_CELLS_Y || unitsY >= Field.MAX_CELLS_X) {
            return false;
        }
        return true;
    }

    public boolean isBig() {
        return maxPower == POWER_BIG;
    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        com.teremok.influence.view.Drawer.draw(this, batch, parentAlpha);
    }

    // Auto-generated

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
}
