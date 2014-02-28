package com.teremok.influence.util;

import com.teremok.influence.model.Cell;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by Alexx on 12.12.13
 */
public class GraphGenerator {
    private float KEEPING_ROUTES_POSSIBILITY = 0.8f;

    private int maxCellsX;
    private int maxCellsY;
    private int matrixSize;
    private int cellsCount;
    int cycles;

    private Random rnd;

    private byte[][] mask;
    private short[][] matrix;


    List<Cell> cells;

    public GraphGenerator(int cellsCount, int maxCellsX, int maxCellsY) {
        this.maxCellsX = maxCellsX;
        this.maxCellsY = maxCellsY;
        this.cellsCount = cellsCount;
        this.matrixSize = maxCellsX*maxCellsY;
        rnd = new Random();
        matrix = new short[cellsCount][cellsCount];
        cells = new LinkedList<Cell>();
    }

    public void generate() {
        mask = new byte[maxCellsY][maxCellsX];

        insertUnreachable();

        cycles = 0;
        int x, y;
        for (int i = 0; i < cellsCount; i ++) {
            do {
                cycles++;
                x = rnd.nextInt(maxCellsY);
                y = rnd.nextInt(maxCellsX);

                if (isEmpty(x, y) && i == 0)
                    break;

            } while (!isEmpty(x, y) || isAlone(x, y));
            mask[x][y] = 1;
        }
        constructMatrix();
        minimizeMatrix();
    }

    public void parse(List<Cell> cells) {
        mask = new byte[maxCellsY][maxCellsX];

        insertUnreachable();

        cycles = 0;
        for (int i = 0; i < cells.size(); i++) {
            Cell cell = cells.get(i);
            mask[cell.getUnitsX()][cell.getUnitsY()] = 1;
        }
        constructMatrix();
        minimizeMatrix();
        this.cells = cells;
    }

    private void insertUnreachable() {
        if (mask != null) {

            for (int i = 1; i < maxCellsY; i += 2) {
                mask[i][maxCellsX -1] = Byte.MAX_VALUE;

                Logger.log("add forbidden " + i + " : "+ (maxCellsX -1));
            }
        }
    }

    public void minimizeMatrix() {
        int startNumber = getFirstValidVertexNumber();

        if (markedVertexes == null) {
            markedVertexes = new boolean[matrixSize];
        }

        for (int i = 0; i < matrixSize; i++) {
            markedVertexes[i] = false;
        }

        DFS(startNumber, startNumber);
    }

    private int getFirstValidVertexNumber() {

        for (int i = 0; i < matrixSize; i++){
            for (int j = 0; j < matrixSize; j++) {
                if (matrix[i][j] == 1) {
                    return i;
                }
            }
        }
        return 0;
    }

    // получить номер элемента
    // исходя из координат в матрице и длины строки
    // num = col + (row*3) + 1;
    private int getNum( int i, int j) {
        return j + i* maxCellsX;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public void constructMatrix() {
        matrix = new short[matrixSize][matrixSize];
        cells = new LinkedList<Cell>();

        for (int i = 0; i < maxCellsY; i++) {
            for (int j = 0; j < maxCellsX; j++) {
                if (mask[i][j] > 0 && mask[i][j] < Byte.MAX_VALUE) {
                    int curNum = getNum(i, j);
                    checkAround(curNum, i, j);
                    cells.add(Cell.makeEmptyCell(curNum, i, j));
                } else {
                    //cells.add(Cell.makeInvalidCell());
                }
            }
        }
    }

    private void checkAround(int curNum, int x, int y) {
        for (int i = -1; i < 2; i++ ) {
            for (int j = -1; j < 2; j++ ) {
                if (
                        (
                                (i != -1 && j != 1 || i != 1 && j != 1)
                                        && x%2==0
                        )
                                ||
                                (
                                        (i != -1 && j != -1 || i != 1 && j != -1)
                                                && x%2==1
                                )
                        ) {
                    checkForMatrix(curNum, x + i, y + j);
                }
            }
        }
    }

    private void checkForMatrix(int curNum, int x, int y) {
        if (x < 0 || y < 0 || x >= maxCellsY || y >= maxCellsX) {
            return;
        }
        if (mask[x][y] > 0 && mask[x][y] < Integer.MAX_VALUE) {

            matrix[curNum][getNum(x,y)] = 1;
        }
    }

    private void printMatrix() {
        //Logger.log("Matrix for graph: ");
        int ones = 0;
        for (int i = 0; i < cellsCount; i++) {
            for (int j = 0; j < cellsCount; j++) {
                //Logger.append(matrix[i][j] + "\t");
                if (matrix[i][j] == 1) ones++;
            }
            //Logger.log("");
        }
        //Logger.log("Percents: " + ((float)ones * 100 )/(cellsCount*cellsCount) + "%");
        //Logger.log(" - - - ");

    }

    private void printMatrix(String desc, int[][] matrix, int sizeX, int sizeY) {
        //Logger.log(desc);
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                //Logger.append(matrix[i][j] + "\t");
            }
            //Logger.log("");
        }
        //Logger.log(" - - - ");

    }

    private boolean isAlone(int x, int y) {
        for (int i = -1; i < 2; i++ ) {
            for (int j = -1; j < 2; j++ ) {
                if (
                        (
                                (i != -1 && j != 1 || i != 1 && j != 1)
                                        && x%2==0
                        )
                                ||
                                (
                                        (i != -1 && j != -1 || i != 1 && j != -1)
                                                && x%2==1
                                )
                        ) {
                    if (! isEmptyIncludeMax(x + i, y + j))
                        return false;
                }
            }
        }
        return true;
    }

    private boolean isEmptyIncludeMax(int x, int y) {

        if (x < 0 || y < 0 || x >= maxCellsY || y >= maxCellsX) {
            return true;
        }

        if (mask[x][y] == 0 || mask[x][y] == Integer.MAX_VALUE) {
            return true;
        }

        return false;
    }

    private boolean isEmpty(int x, int y) {

        if (x < 0 || y < 0 || x >= maxCellsY || y >= maxCellsX) {
            return true;
        }

        if (mask[x][y] == 0 ) {
            return true;
        }

        return false;
    }

    private void printMask() {
        //Logger.log(" - - - - - - - - ");
        for (int i = 0; i < maxCellsY; i++) {
            for (int j = 0; j < maxCellsX; j++) {
                if (mask[i][j] == Integer.MAX_VALUE) {
                    //Logger.append("-\t");
                } else {
                    //Logger.append(mask[i][j] + "\t");
                }
            }
            //Logger.log("");
        }
        //Logger.log(" - - - Cycles: " + cycles);
    }

    private boolean[] markedVertexes = null;

    private void DFS(int current, int from) {

        if (markedVertexes[current]) {
            if (rnd.nextFloat() > KEEPING_ROUTES_POSSIBILITY) {
                matrix[current][from] = 0;
                matrix[from][current] = 0;
            }
            return;
        }

        boolean allMarked = true;
        for (int vertex = 0; vertex < matrixSize; vertex++) {
            allMarked = allMarked && markedVertexes[vertex];
        }
        if (allMarked) {
            return;
        }

        markedVertexes[current] = true;

        for (int vertex = 0; vertex < matrixSize; vertex++) {
            if (matrix[current][vertex] == 1 && vertex != current && vertex != from)
                DFS(vertex, current);  // Запускаемся из соседа
        }
    }

    // Auto-generated

    public short[][] getMatrix() {
        return matrix;
    }
}
