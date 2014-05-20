package com.teremok.influence.controller;

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

        //Logger.log("Attack:\t" + powerA + " \t->\t " + powerB);

        n = rollNDices(powerA);
        m = rollNDices(powerB);

        //Logger.log("Delta:\t" + n + " \t->\t " + m);

        delta = n - m;

        calculateResults();

        //Logger.log("Result:\t" + a + " \t->\t " + b);
        //Logger.log("-\t-\t-\t-\t-");
        return delta;
    }

    static void calculateResults() {

        int attackDices = countDices(n);
        int defenseDices = countDices(m);

        if (defenseDices == 0) {
            defenseDices++;
        }

        if (delta > 0) {
            b = a;
            b = b - defenseDices;
        } else {
            b = b - attackDices;
        }

        a = 1;

        if (b <= 0 || delta == 0 )
            b = 1;
    }

    static int countDices(int score) {
        int num = score/6;
        int modulo = score%6;
        if (modulo != 0) {
            num += 1;
        }
        return num;
    }

    public static int getResultPowerA() {
        return a;
    }

    public static int getResultPowerB() {
        return b;
    }

    // Auto-generated

    public static void setA(int a) {
        Calculator.a = a;
    }

    public static void setB(int b) {
        Calculator.b = b;
    }

    public static void setN(int n) {
        Calculator.n = n;
    }

    public static void setM(int m) {
        Calculator.m = m;
    }

    public static void setDelta(int delta) {
        Calculator.delta = delta;
    }

    public static int getDelta() {
        return delta;
    }

    public static int getN() {
        return n;
    }

    public static int getM() {
        return m;
    }
}
