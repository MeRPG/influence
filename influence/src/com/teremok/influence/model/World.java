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

    private int[][] matrix;
    private int[][] minimal;
    private List<Cell> cells;

    public World() {
        regenerate();
    }

    public List<Cell> getCells() {return cells; }

    public void regenerate() {
        CellSchemeGenerator generator = new CellSchemeGenerator(25);
        generator.generate();
        cells = generator.getCells();
        matrix = generator.getMatrix();
        minimal = generator.getMinimal();
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public int[][] getMinimal() {
        return minimal;
    }
}
