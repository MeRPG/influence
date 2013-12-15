package com.teremok.influence.model;

/**
 * Created by Alexx on 12.12.13.
 */
public class Cell {

    // 6 штук соседей максимум
    // считать начиная с левого
    private Cell[] neighbors;

    // доступные места для ячеек
    private boolean[] availiable;

    // для обхода графа
    boolean marked;

    public void setNeighbors(Cell[] neighbors) {
        this.neighbors = neighbors;
    }

    public Cell[] getNeighbors() {
        return neighbors;
    }

    public Cell() {
        neighbors = new Cell[6];

    }

    public void addNeighbor(Cell cell) {
        for (int i = 0; i < 6; i++ ) {
            if (neighbors[i] == null) {
                neighbors[i] = cell;
                break;
            }
        }
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }
}
