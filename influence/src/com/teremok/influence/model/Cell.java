package com.teremok.influence.model;

/**
 * Created by Alexx on 12.12.13.
 */
public class Cell {
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

    static public int calcNumber(int x, int y) {
        return x + y*5;
    }
}
