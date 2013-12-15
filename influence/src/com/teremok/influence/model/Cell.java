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

    public void setNeighbors(Cell[] neighbors) {
        this.neighbors = neighbors;
    }

    public Cell[] getNeighbors() {
        return neighbors;
    }

    public Cell() {
        neighbors = new Cell[6];

    }

}
