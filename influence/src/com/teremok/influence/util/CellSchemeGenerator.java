package com.teremok.influence.util;

import com.teremok.influence.model.Cell;

import java.util.Random;

/**
 * Created by Alexx on 12.12.13.
 */
public class CellSchemeGenerator {

    private int count;
    private Random rnd;
    private int[][] mask;

    int cycles;

    public CellSchemeGenerator(int i) {
        count = i;
        rnd = new Random();
    }

    public int[][] generate() {
        mask = new int[7][5];
        mask[1][4] = Integer.MAX_VALUE;
        mask[3][4] = Integer.MAX_VALUE;
        mask[5][4] = Integer.MAX_VALUE;
        cycles = 0;
        for (int i = 0; i < count; i ++) {
            int x, y;
            do {
                cycles++;
                x = rnd.nextInt(7);
                y = rnd.nextInt(5);

                if (isEmpty(x, y) && i == 0)
                    break;

            } while (!isEmpty(x, y) || isAlone(x, y));
            mask[x][y] = 1;
        }

        printMask();

        return mask;
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

        if (x < 0 || y < 0 || x >= 7 || y >= 5) {
            return true;
        }

        if (mask[x][y] == 0 || mask[x][y] == Integer.MAX_VALUE) {
            return true;
        }

        return false;
    }

    private boolean isEmpty(int x, int y) {

        if (x < 0 || y < 0 || x >= 7 || y >= 5) {
            return true;
        }

        if (mask[x][y] == 0 ) {
            return true;
        }

        return false;
    }

    private void printMask() {
        System.out.println(" - - - - - - - - ");
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 5; j++) {
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
