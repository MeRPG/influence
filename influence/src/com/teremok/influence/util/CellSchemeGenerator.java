package com.teremok.influence.util;

import com.teremok.influence.model.Cell;
import com.teremok.influence.model.World;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by Alexx on 12.12.13.
 */
public class CellSchemeGenerator {
    /*
    private int MAX_CELLS_X = World.MAX_CELLS_X;
    private int MAX_CELLS_Y = World.MAX_CELLS_Y;
    */
    private int count;
    private Random rnd;
    private int[][] mask;
    private int[][] matrix;

    int cycles;

    List<Cell> cells;

    public CellSchemeGenerator(int i) {
        count = i;
        rnd = new Random();
        matrix = new int[i][i];
        cells = new LinkedList<Cell>();
    }

    public int[][] generate() {
        mask = new int[World.MAX_CELLS_Y][World.MAX_CELLS_X];
        mask[1][4] = Integer.MAX_VALUE;
        mask[3][4] = Integer.MAX_VALUE;
        mask[5][4] = Integer.MAX_VALUE;
        cycles = 0;
        for (int i = 0; i < count; i ++) {
            int x, y;
            do {
                cycles++;
                x = rnd.nextInt(World.MAX_CELLS_Y);
                y = rnd.nextInt(World.MAX_CELLS_X);

                if (isEmpty(x, y) && i == 0)
                    break;

            } while (!isEmpty(x, y) || isAlone(x, y));
            mask[x][y] = 1;
        }

        printMask();
        constructMatrix();
        printMatrix();
        printList();

        return transpose(mask);
    }
    // получить номер элемента
    // исходя из координат в матрице и длины строки
    // num = col + (row*3) + 1;
    private int getNum( int i, int j) {
        return j + i*5;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public void constructMatrix() {
        matrix = new int[World.MAX_CELLS_Y*World.MAX_CELLS_X][World.MAX_CELLS_Y*World.MAX_CELLS_X];
        cells = new LinkedList<Cell>();

        for (int i = 0; i < World.MAX_CELLS_Y; i++) {
            for (int j = 0; j < World.MAX_CELLS_X; j++) {
                if (mask[i][j] > 0 && mask[i][j] < Integer.MAX_VALUE) {
                    int curNum = getNum(i, j);
                    checkAround(curNum, i, j);
                    cells.add(new Cell(curNum,i,j));
                } else {    cells.add(new Cell(-1, 0, 0));  }
            }
        }
    }

    public int[][] getMatrix() {
        return matrix;
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
        if (x < 0 || y < 0 || x >= World.MAX_CELLS_Y || y >= World.MAX_CELLS_X) {
            return;
        }
        if (mask[x][y] > 0 && mask[x][y] < Integer.MAX_VALUE) {

            matrix[curNum][getNum(x,y)] = 1;
        }
    }

    private void printMatrix() {
        System.out.println("Matrix for graph: ");
        int ones = 0;
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < count; j++) {
                System.out.print(matrix[i][j] + "\t");
                if (matrix[i][j] == 1) ones++;
            }
            System.out.println();
        }
        System.out.println("Percents: " + ((float)ones * 100 )/(count*count) + "%");
        System.out.println(" - - - ");

    }

    private void printList() {
        System.out.println(" List size: " + cells.size());

        for (Cell c : cells) {
            System.out.println(c);
        }

        System.out.println(" - - - ");
    }

    private int[][] transpose(int[][] mask) {
        int[][] tran = new int[World.MAX_CELLS_X][World.MAX_CELLS_Y];

        for (int i = 0; i < World.MAX_CELLS_Y; i++) {
            for (int j = 0; j < World.MAX_CELLS_X; j++) {
                tran[j][i] = mask[i][j];
            }
        }

        return tran;
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

        if (x < 0 || y < 0 || x >= World.MAX_CELLS_Y || y >= World.MAX_CELLS_X) {
            return true;
        }

        if (mask[x][y] == 0 || mask[x][y] == Integer.MAX_VALUE) {
            return true;
        }

        return false;
    }

    private boolean isEmpty(int x, int y) {

        if (x < 0 || y < 0 || x >= World.MAX_CELLS_Y || y >= World.MAX_CELLS_X) {
            return true;
        }

        if (mask[x][y] == 0 ) {
            return true;
        }

        return false;
    }

    private void printMask() {
        System.out.println(" - - - - - - - - ");
        for (int i = 0; i < World.MAX_CELLS_Y; i++) {
            for (int j = 0; j < World.MAX_CELLS_X; j++) {
                if (mask[i][j] == Integer.MAX_VALUE)
                    System.out.print("-\t");
                else
                    System.out.print(mask[i][j] + "\t");
            }
            System.out.println();
        }
        System.out.println(" - - - Cycles: " + cycles);
    }
}
