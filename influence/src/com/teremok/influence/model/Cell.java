package com.teremok.influence.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.teremok.influence.view.Drawer;

import static com.teremok.influence.model.Field.*;

import java.util.*;

/**
 * Created by Alexx on 12.12.13
 */
public class Cell {

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

    float x, y;

    List<Cell> neighbors;
    List<Cell> enemies;

    boolean selected;

    private Cell() {
        enemies = new LinkedList<Cell>();
        neighbors = new LinkedList<Cell>();
    }

    public Cell(int number, int unitsX, int unitsY, int power, int maxPower, int type) {
        this();
        this.unitsX = unitsX;
        this.unitsY = unitsY;
        this.number = number;
        this.type = type;
        this.power = power;
        this.maxPower = maxPower;
    }

    private Cell(int number) {
        this();
        this.number = number;
    }

    private Cell(int number, int unitsX, int unitsY) {
        this(number);
        this.unitsX = unitsX;
        this.unitsY = unitsY;

        updateBounds();
    }

    public static Cell makeRandomCell(int number, int x, int y) {
        Cell cell = new Cell(number, x, y);
        Random rnd = new Random();
        cell.setType(rnd.nextInt(MAX_TYPE + 1));
        cell.setMaxPower(rnd.nextFloat() > BIG_POSSIBILITY ? POWER_STANDARD : POWER_BIG);
        cell.setPower(1 + rnd.nextInt(cell.maxPower - 1));

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

    public void addNeighbor(Cell cell) {
        neighbors.add(cell);
    }

    public void removeNeighbor(Cell cell) {
        if (neighbors != null) {
            neighbors.remove(cell);
        }
    }

    public void clearNeighbors() {
        enemies.clear();
    }

    public void addEnemy(Cell cell) {
        enemies.add(cell);
    }

    public void removeEnemy(Cell cell) {
        enemies.remove(cell);
    }

    public void clearEnemies() {
        enemies.clear();
    }

    public boolean isFree() {
        return type == -1;
    }

    public void updateBounds() {


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

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getNumber() {
        return number;
    }

    public List<Cell> getNeighbors() {
        return neighbors;
    }

    public List<Cell> getEnemies() {
        return enemies;
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
