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

    private int GRAPH_MATRIX_SIZE = World.MAX_CELLS_X * World.MAX_CELLS_Y;
    private float P = 0.7f;

    private int count;
    private Random rnd;
    private int[][] mask;

    private int[][] matrix;

    private int[][] minimal;

    int cycles;

    List<Cell> cells;

    public CellSchemeGenerator(int i) {
        count = i;
        rnd = new Random();
        matrix = new int[i][i];
        cells = new LinkedList<Cell>();
/*
        printMatrix("pre: ", matr, size, size);
        smallDFS(0, 0);
        printMatrix("post: ", matr, size, size);
*/
    }

    public void generate() {
        mask = new int[World.MAX_CELLS_Y][World.MAX_CELLS_X];
        mask[1][4] = Integer.MAX_VALUE;
        mask[3][4] = Integer.MAX_VALUE;
        mask[5][4] = Integer.MAX_VALUE;
        cycles = 0;
        int x = 0, y = 0;
        for (int i = 0; i < count; i ++) {
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

        copyMatrixToMinimal();
        //minimizeRoutes();
        DFS(getNum(x,y), getNum(x,y));
        printMinimal();
        // printList();
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
        matrix = new int[GRAPH_MATRIX_SIZE][GRAPH_MATRIX_SIZE];
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

    private void printMatrix(String desc, int[][] matrix, int sizeX, int sizeY) {
        System.out.println(desc);
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                System.out.print(matrix[i][j] + "\t");
            }
            System.out.println();
        }
        System.out.println(" - - - ");

    }

    private void printMinimal() {
        System.out.println("Minimal for graph: ");
        int ones = 0;
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < count; j++) {
                System.out.print(minimal[i][j] + "\t");
                if (minimal[i][j] == 1) ones++;
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

    private void copyMatrixToMinimal() {
        minimal = new int[GRAPH_MATRIX_SIZE][GRAPH_MATRIX_SIZE];

        for (int i = 0; i < GRAPH_MATRIX_SIZE; i++) {
            for (int j = 0; j < GRAPH_MATRIX_SIZE; j++) {
                minimal[i][j] = matrix[i][j];
            }
        }
    }

    private void minimizeRoutes() {
        boolean[] checked = new boolean[GRAPH_MATRIX_SIZE];

        checked[0] = true;
        for (int current = 0; current < GRAPH_MATRIX_SIZE; current++) {
            for (int check = current+1; check < GRAPH_MATRIX_SIZE; check++) {
                if (check != current && minimal[current][check] == 1) {
                    for (int i = current+1; i < GRAPH_MATRIX_SIZE; i++) {
                        if (checked[i] && i!= check) {
                            minimal[check][i] = 0;
                            minimal[i][check] = 0;
                        }
                    }
                    checked[check] = true;
                }
            }
        }

    }

    private boolean[] mark = new boolean[GRAPH_MATRIX_SIZE];

    int size = 5;
    int[][] matr = {
            {1, 1, 0, 0, 1},
            {1, 1, 1, 0, 1},
            {0, 1, 1, 1, 0},
            {0, 0, 1, 1, 1},
            {1, 1, 1, 1, 1}
    };

    private void smallDFS(int v, int from) {
        if (mark[v])  // Если мы здесь уже были, то тут больше делать нечего
        {
            matr[v][from] = 0;
            matr[from][v] = 0;
            return;
        }

        boolean flag = true;
        for (int i = 0; i < size; i++) {
            flag = flag && mark[i];
        }
        if (flag) {
            return;
        }

        mark[v] = true;   // Помечаем, что мы здесь были


        for (int i = 0; i < size; i++)  // Для каждого ребра
        {
            if (matr[v][i] == 1 && i != v && i != from)
                smallDFS(i, v);  // Запускаемся из соседа
        }


    }

    private void DFS(int v, int from) {
        if (mark[v])  // Если мы здесь уже были, то тут больше делать нечего
        {
            if (rnd.nextFloat() > P) {
                minimal[v][from] = 0;
                minimal[from][v] = 0;
            }
            return;
        }

        boolean flag = true;
        for (int i = 0; i < GRAPH_MATRIX_SIZE; i++) {
            flag = flag && mark[i];
        }
        if (flag) {
            return;
        }

        mark[v] = true;   // Помечаем, что мы здесь были


        for (int i = 0; i < GRAPH_MATRIX_SIZE; i++)  // Для каждого ребра
        {
            if (minimal[v][i] == 1 && i != v && i != from)
                DFS(i, v);  // Запускаемся из соседа
        }
    }

    private void testAlgo() {

        boolean[] checked = new boolean[size];

        printMatrix("matr do: ", matr, size, size);
        checked[0] = true;
        for (int current = 0; current < size; current++) {
            for (int check = current+1; check < size; check++) {
                if (check != current && matr[current][check] == 1) {
                    for (int i = current+1; i < size; i++) {
                        if (checked[i] && i!= check) {
                            matr[check][i] = 0;
                            matr[i][check] = 0;
                        }
                    }
                    checked[check] = true;
                }
            }
        }

        printMatrix("matr posle: ", matr, size, size);
    }

    public int[][] getMinimal() {
        return minimal;
    }
}
