package com.teremok.influence.model;

import java.util.Random;

/**
 * Created by Alexx on 12.12.13.
 */
public class Cell {

    static private final int POWER_BIG = 12;
    static private final int POWER_STANDARD = 8;

    static private final float BIG_POSSIBILITY = 0.3f;

    static private final int MAX_TYPE = 4;

    // доступные места для ячеек
    private boolean[] availiable;

    // для обхода графа
    boolean marked;

    // координаты на сетке
    int x, y;

    // порядковый номер
    int number;

    // тип (цвет)
    int type;

    // мощность - количество секторов
    int power;

    // максимальное количество секторов
    int maxPower;

    boolean selected;

    public Cell() {
    }

    public Cell(int number) {
        this();
        this.number = number;
    }

    public Cell(int number, int x, int y) {
        this(number);
        this.x = x;
        this.y = y;
    }

    public Cell(int number, int x, int y, int type) {
        this(number, x, y);
        this.type = type;
    }

    public static Cell makeRandomCell(int number, int x, int y) {
        Cell cell = new Cell(number, x, y);
        Random rnd = new Random();
        cell.setType(rnd.nextInt(MAX_TYPE + 1));
        cell.setMaxPower( rnd.nextFloat() > BIG_POSSIBILITY ? POWER_STANDARD : POWER_BIG);
        cell.setPower(cell.maxPower);
        return cell;
    }

    public static Cell makeInvalidCell() {
        return new Cell(-1, 0, 0);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "x=" + x +
                ", y=" + y +
                ", number=" + number +
                '}';
    }

    public boolean isValid() {
        if (number == -1)
            return false;
        if (x < 0 || y < 0 || x >= World.MAX_CELLS_Y || y >= World.MAX_CELLS_X)
            return false;

        return true;
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
