package com.teremok.influence.model;

import com.teremok.framework.screen.AbstractScreen;

import java.util.List;

/**
 * Created by Алексей on 17.05.2014
 */
public class FieldModel {
    public static float WIDTH =  480f;
    public static float HEIGHT = 624f;

    public List<Cell> cells;

    public Router router;

    public float initialX;
    public float initialY;
    public float initialWidth;
    public float initialHeight;

    public int maxCellsY;
    public int maxCellsX;
    public int cellsCount;

    public void reset(GameSettings settings) {

        maxCellsX = settings.maxCellsX;
        maxCellsY = settings.maxCellsY;
        cellsCount = settings.cellsCount;

        initialX = 0f;
        initialY = AbstractScreen.HEIGHT - HEIGHT-1f;

        initialWidth = WIDTH;
        initialHeight = HEIGHT;
    }

    public void reset(GameSettings settings, List<Cell> cells, Router router) {
        reset(settings);
        this.cells = cells;
        this.router = router;
    }

    public Cell getCell(int number) {
        for (Cell cell : cells) {
            if (cell.getNumber() == number){
                return  cell;
            }
        }
        return null;
    }

    public Cell getCell(int unitsX, int unitsY) {
        return getCell(getNum(unitsX, unitsY));
    }

    public int getNum( int i, int j) {
        return i + j* maxCellsX;
    }

    public boolean isCellsConnected(Cell from, Cell to) {
        if (from.getNumber() == to.getNumber())
            return false;
        else
            return router.routeExist(from.getNumber(), to.getNumber());
    }

    public void updateLists() {

        for (Cell cell : cells) {

            cell.clearEnemies();
            cell.clearNeighbors();

            for (Cell cell2 : cells) {
                if (isCellsConnected(cell, cell2)) {
                    cell.addNeighbor(cell2);
                    if (( cell.getType() != cell2.getType() || cell.isFree() )) {
                        cell.addEnemy(cell2);
                    }
                }
            }
        }

    }
}
