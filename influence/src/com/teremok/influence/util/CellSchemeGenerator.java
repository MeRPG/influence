package com.teremok.influence.util;

import com.teremok.influence.model.Cell;
import com.teremok.influence.model.Field;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by Alexx on 12.12.13
 */
public class CellSchemeGenerator {

    private int GRAPH_MATRIX_SIZE = Field.MAX_CELLS_X * Field.MAX_CELLS_Y;
    private float KEEPING_ROUTES_POSSIBILITY = 0.8f;

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
    }

    public CellSchemeGenerator(int i, float p) {
        this(i);
        this.KEEPING_ROUTES_POSSIBILITY = p;
    }

    public void generate() {
        mask = new int[Field.MAX_CELLS_Y][Field.MAX_CELLS_X];
        mask[1][4] = Integer.MAX_VALUE;
        mask[3][4] = Integer.MAX_VALUE;
        mask[5][4] = Integer.MAX_VALUE;
        cycles = 0;
        int x, y;
        for (int i = 0; i < count; i ++) {
            do {
                cycles++;
                x = rnd.nextInt(Field.MAX_CELLS_Y);
                y = rnd.nextInt(Field.MAX_CELLS_X);

                if (isEmpty(x, y) && i == 0)
                    break;

            } while (!isEmpty(x, y) || isAlone(x, y));
            mask[x][y] = 1;
        }

        //printMask();
        constructMatrix();
        //printMatrix();

        updateMinimal(KEEPING_ROUTES_POSSIBILITY);
    }

    public void updateMinimal() {
        copyMatrixToMinimal();
        System.out.println("Update minimal with p = " + KEEPING_ROUTES_POSSIBILITY);
        Cell start;
        int i = 0;
        do {
            start= cells.get(i++);
        } while (!start.isValid());
        for (i = 0; i < GRAPH_MATRIX_SIZE; i++) {
            mark[i] = false;
        }
        DFS(start.getNumber(), start.getNumber());
        //printMinimal();
        //printList();
    }

    public void updateMinimal(float newP) {
        this.KEEPING_ROUTES_POSSIBILITY = newP;
        updateMinimal();
    }
    // получить номер элемента
    // исходя из координат в матрице и длины строки
    // num = col + (row*3) + 1;
    private int getNum( int i, int j) {
        return j + i* Field.MAX_CELLS_X;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public void constructMatrix() {
        matrix = new int[GRAPH_MATRIX_SIZE][GRAPH_MATRIX_SIZE];
        cells = new LinkedList<Cell>();

        for (int i = 0; i < Field.MAX_CELLS_Y; i++) {
            for (int j = 0; j < Field.MAX_CELLS_X; j++) {
                if (mask[i][j] > 0 && mask[i][j] < Integer.MAX_VALUE) {
                    int curNum = getNum(i, j);
                    checkAround(curNum, i, j);
                    cells.add(Cell.makeRandomCell(curNum, i, j));
                } else {
                    cells.add(Cell.makeInvalidCell());
                }
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
        if (x < 0 || y < 0 || x >= Field.MAX_CELLS_Y || y >= Field.MAX_CELLS_X) {
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

        if (x < 0 || y < 0 || x >= Field.MAX_CELLS_Y || y >= Field.MAX_CELLS_X) {
            return true;
        }

        if (mask[x][y] == 0 || mask[x][y] == Integer.MAX_VALUE) {
            return true;
        }

        return false;
    }

    private boolean isEmpty(int x, int y) {

        if (x < 0 || y < 0 || x >= Field.MAX_CELLS_Y || y >= Field.MAX_CELLS_X) {
            return true;
        }

        if (mask[x][y] == 0 ) {
            return true;
        }

        return false;
    }

    private void printMask() {
        System.out.println(" - - - - - - - - ");
        for (int i = 0; i < Field.MAX_CELLS_Y; i++) {
            for (int j = 0; j < Field.MAX_CELLS_X; j++) {
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

    private boolean[] mark = new boolean[GRAPH_MATRIX_SIZE];

    private void DFS(int v, int from) {
        if (mark[v])  // Если мы здесь уже были, то тут больше делать нечего
        {
            if (rnd.nextFloat() > KEEPING_ROUTES_POSSIBILITY) {
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

    public int[][] getMinimal() {
        return minimal;
    }
}
