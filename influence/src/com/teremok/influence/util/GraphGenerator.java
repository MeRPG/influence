package com.teremok.influence.util;

import com.teremok.influence.model.Cell;
import static com.teremok.influence.model.Field.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by Alexx on 12.12.13
 */
public class GraphGenerator {

    private int MATRIX_SIZE = MAX_CELLS_X * MAX_CELLS_Y;
    private float KEEPING_ROUTES_POSSIBILITY = 0.8f;

    private int count;
    private Random rnd;
    private int[][] mask;

    private int[][] matrix;

    int cycles;

    List<Cell> cells;

    public GraphGenerator(int i) {
        count = i;
        rnd = new Random();
        matrix = new int[i][i];
        cells = new LinkedList<Cell>();
    }

    public void generate() {
        mask = new int[MAX_CELLS_Y][MAX_CELLS_X];
        mask[1][4] = Integer.MAX_VALUE;
        mask[3][4] = Integer.MAX_VALUE;
        mask[5][4] = Integer.MAX_VALUE;
        cycles = 0;
        int x, y;
        for (int i = 0; i < count; i ++) {
            do {
                cycles++;
                x = rnd.nextInt(MAX_CELLS_Y);
                y = rnd.nextInt(MAX_CELLS_X);

                if (isEmpty(x, y) && i == 0)
                    break;

            } while (!isEmpty(x, y) || isAlone(x, y));
            mask[x][y] = 1;
        }
        constructMatrix();
        minimizeMatrix();
    }

    public void minimizeMatrix() {
        int startNumber = getFirstValidVertexNumber();

        for (int i = 0; i < MATRIX_SIZE; i++) {
            markedVertexes[i] = false;
        }

        DFS(startNumber, startNumber);
    }

    private int getFirstValidVertexNumber() {

        for (int i = 0; i < MATRIX_SIZE; i++){
            for (int j = 0; j < MATRIX_SIZE; j++) {
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
        return j + i*MAX_CELLS_X;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public void constructMatrix() {
        matrix = new int[MATRIX_SIZE][MATRIX_SIZE];
        cells = new LinkedList<Cell>();

        for (int i = 0; i < MAX_CELLS_Y; i++) {
            for (int j = 0; j < MAX_CELLS_X; j++) {
                if (mask[i][j] > 0 && mask[i][j] < Integer.MAX_VALUE) {
                    int curNum = getNum(i, j);
                    checkAround(curNum, i, j);
                    cells.add(Cell.makeEmptyCell(curNum, i, j));
                } else {
                    cells.add(Cell.makeInvalidCell());
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
        if (x < 0 || y < 0 || x >= MAX_CELLS_Y || y >= MAX_CELLS_X) {
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

        if (x < 0 || y < 0 || x >= MAX_CELLS_Y || y >= MAX_CELLS_X) {
            return true;
        }

        if (mask[x][y] == 0 || mask[x][y] == Integer.MAX_VALUE) {
            return true;
        }

        return false;
    }

    private boolean isEmpty(int x, int y) {

        if (x < 0 || y < 0 || x >= MAX_CELLS_Y || y >= MAX_CELLS_X) {
            return true;
        }

        if (mask[x][y] == 0 ) {
            return true;
        }

        return false;
    }

    private void printMask() {
        System.out.println(" - - - - - - - - ");
        for (int i = 0; i < MAX_CELLS_Y; i++) {
            for (int j = 0; j < MAX_CELLS_X; j++) {
                if (mask[i][j] == Integer.MAX_VALUE)
                    System.out.print("-\t");
                else
                    System.out.print(mask[i][j] + "\t");
            }
            System.out.println();
        }
        System.out.println(" - - - Cycles: " + cycles);
    }

    private boolean[] markedVertexes = new boolean[MATRIX_SIZE];

    private void DFS(int current, int from) {
        if (markedVertexes[current]) {
            if (rnd.nextFloat() > KEEPING_ROUTES_POSSIBILITY) {
                matrix[current][from] = 0;
                matrix[from][current] = 0;
            }
            return;
        }

        boolean allMarked = true;
        for (int vertex = 0; vertex < MATRIX_SIZE; vertex++) {
            allMarked = allMarked && markedVertexes[vertex];
        }
        if (allMarked) {
            return;
        }

        markedVertexes[current] = true;

        for (int vertex = 0; vertex < MATRIX_SIZE; vertex++) {
            if (matrix[current][vertex] == 1 && vertex != current && vertex != from)
                DFS(vertex, current);  // Запускаемся из соседа
        }
    }

    // Auto-generated

    public int[][] getMatrix() {
        return matrix;
    }
}