package com.teremok.influence.model;

import java.util.Random;

/**
 * Created by Alexx on 26.12.13
 */
public class Calculator {

    private static Random rnd = new Random();

    private static int a;
    private static int b;
    private static int delta;
    private static int n;
    private static int m;

    public static void test(int inA, int inB, int inN, int inM) {
        a = inA;
        b = inB;
        n = inN;
        m = inM;

        delta = n - m;

        getResultsByDelta();

        System.out.println(inA + "\t|\t" + inB + "\t|\t" + inN  + "\t|\t" + inM  + "\t|\t" + a  + "\t|\t" + b);
        System.out.println(" - - - ");
    }

    public static int rollNDices(int number) {
        int result = 0;
        for (int i = 0; i < number; i++) {
            result += rollDice();
        }
        return result;
    }

    public static int rollDice() {
        return rnd.nextInt(6) + 1;
    }

    public static int fight(int powerA, int powerB) {

        a = powerA;
        b = powerB;

        System.out.println("Attack:\t" + powerA + " \t->\t " + powerB);

        n = rollNDices(powerA);
        m = rollNDices(powerB);

        System.out.println("Delta:\t" + n + " \t->\t " + m);

        delta = n - m;

        getResultsByDelta();

        System.out.println("Result:\t" + a + " \t->\t " + b);
        System.out.println("-\t-\t-\t-\t-");
        return delta;
    }

    private static void getResultsByDelta() {
        if (delta == 0) {
            a = 1;
            b = 1;
        }

        int overPower = 6;
        boolean greatDifference = delta > overPower;

        if (delta < 0) {
            if (greatDifference) {
                b = b-1;
                a = 1;
            } else {
                b = b - Math.abs(a - 1);
                a = 1;
            }
        }

        if (delta > 0) {
            if (greatDifference) {
                b = a - 1;
                a = 1;
            } else {
                b = a - Math.abs(a - 1);
                a = 1;
            }
        }

        if (a <= 0 )
            a = 1;
        if (b <= 0 )
            b = 1;
    }

    public static int getResultPowerA() {
        return a;
    }

    public static int getResultPowerB() {
        return b;
    }

    public static int getDelta() {
        return delta;
    }
}
