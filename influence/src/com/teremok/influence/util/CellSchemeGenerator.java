package com.teremok.influence.util;

import com.teremok.influence.model.World;

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

        return transpose(mask);
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
