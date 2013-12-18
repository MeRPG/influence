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
    private Cell root;

    public World() {
        regenerate();
    }

    public int[][] getCells() {
        return cells;
    }

    public void setCells(int[][] cells) {
        this.cells = cells;
    }

    public void regenerate() {
        CellSchemeGenerator generator = new CellSchemeGenerator(25);
        cells = generator.generate();
        root = generator.getGraph();
    }

    public Cell getRoot() {
        return root;
    }
}
