package com.teremok.influence.model.player;

import com.teremok.influence.model.Cell;
import com.teremok.influence.model.Field;
import com.teremok.influence.model.Match;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by Alexx on 26.12.13
 */
public abstract class Player {

    protected int number;
    protected int powerToDistribute;
    protected int score;
    protected Match match;
    protected Field field;
    protected PlayerType type;
    protected Set<Cell> cells;

    protected Player(int number, Match match) {
        this.number = number;
        this.match = match;
        this.field = match.getField();
        cells = new HashSet<Cell>();
    }

    public void subtractPowerToDistribute() {
        if (powerToDistribute > 0)
            powerToDistribute--;
        else
            powerToDistribute = 0;
    }

    public void act(float delta) {
        actLogic(delta);
    }

    protected abstract void actLogic(float delta);

    public boolean hasPowerToDistribute() {
        return powerToDistribute > 0;
    }

    public void addCell(Cell cell) {
        cells.add(cell);
    }

    public void removeCell(Cell cell) {
        cells.remove(cell);
    }

    public void clearCells() {
        cells.clear();
    }

    public List<Cell> getCellsList() {
        if (cells != null) {
            return new LinkedList<Cell>(cells);
        } else {
            return null;
        }
    }

    // Auto-generated

    public void setPowerToDistribute(int power) {
        this.powerToDistribute = power;
    }

    public int getPowerToDistribute() {
        return powerToDistribute;
    }

    public int getNumber() {
        return number;
    }

    public PlayerType getType() { return type; }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setField(Field field) {
        this.field = field;
    }
}
