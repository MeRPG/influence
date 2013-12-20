package com.teremok.influence.model;

import com.teremok.influence.util.CellSchemeGenerator;

import java.util.List;

/**
 * Created by Alexx on 11.12.13.
 */
public class World {

    public static final int MAX_CELLS_Y = 7;
    public static final int MAX_CELLS_X = 5;

    //public static final float WIDTH = 7f;
    //public static final float HEIGHT = 8.5f;

    private int[][] matrix;
    private int[][] minimal;
    private List<Cell> cells;
    private CellSchemeGenerator generator;

    private Cell selectedCell;

    private int P;

    public void setP(int p) {
        this.P = p;
    }

    public int getP() {
        return this.P;
    }

    public World() {
        setP(7);
        regenerate();
    }

    public List<Cell> getCells() {return cells; }

    public void regenerate() {
        generator = new CellSchemeGenerator(25, P*0.1f);
        generator.generate();
        cells = generator.getCells();
        cells.get(15).setSelected(true);
        matrix = generator.getMatrix();
        minimal = generator.getMinimal();
    }

    public void updateMinimal() {
        generator.updateMinimal(P*0.1f);
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

    private boolean isCellsConnected(Cell from, Cell to) {
        return minimal[from.getNumber()][to.getNumber()] == 1 || minimal[to.getNumber()][from.getNumber()] == 1;
    }

    public Cell getSelectedCell() {
        return selectedCell;
    }

    public void setSelectedCell(Cell cell) {
        if (selectedCell != null && isCellsConnected(selectedCell, cell))
            cell.setType(selectedCell.getType());

        if (selectedCell != null)
            selectedCell.setSelected(false);

        cell.setSelected(true);
        selectedCell = cell;

    }
}
