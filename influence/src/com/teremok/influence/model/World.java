package com.teremok.influence.model;

import com.teremok.influence.util.CellSchemeGenerator;

import java.util.List;

/**
 * Created by Alexx on 11.12.13.
 */
public class World {

    public static final int MAX_CELLS_Y = 7;
    public static final int MAX_CELLS_X = 5;

    public static final float WIDTH = 7f;
    public static final float HEIGHT = 8.5f;

    private int[][] cells;
    private int[][] matrix;
    private List<Cell> list;

    public World() {
        regenerate();
    }

    public int[][] getCells() {
        return cells;
    }

    public List<Cell> getCellsList() {return list; }

    public void setCells(int[][] cells) {
        this.cells = cells;
    }

    public void regenerate() {
        CellSchemeGenerator generator = new CellSchemeGenerator(25);
        cells = generator.generate();
        list = generator.getCells();
        matrix = generator.getMatrix();
    }

    public int[][] getMatrix() {
        return matrix;
    }
}
