package com.teremok.influence.controller;

import com.badlogic.gdx.Gdx;

import java.util.Random;

/**
 * Created by Alexx on 26.12.13
 */
public class Calculator {

    private static Random rnd = new Random();

    private int a;
    private int b;
    private int delta;
    private int n;
    private int m;

    public int rollNDices(int number) {
        int result = 0;
        for (int i = 0; i < number; i++) {
            result += rollDice();
        }
        return result;
    }

    public int rollDice() {
        return rnd.nextInt(6) + 1;
    }

    public int fight(int powerA, int powerB) {

        a = powerA;
        b = powerB;

        Gdx.app.debug(getClass().getSimpleName(), "Attack:\t" + powerA + " \t->\t " + powerB);

        n = rollNDices(powerA);
        m = rollNDices(powerB);

        Gdx.app.debug(getClass().getSimpleName(), "Delta:\t" + n + " \t->\t " + m);

        delta = n - m;

        calculateResults();

        Gdx.app.debug(getClass().getSimpleName(), "Result:\t" + a + " \t->\t " + b);
        Gdx.app.debug(getClass().getSimpleName(), "-\t-\t-\t-\t-");
        return delta;
    }

    void calculateResults() {

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

    int countDices(int score) {
        int num = score/6;
        int modulo = score%6;
        if (modulo != 0) {
            num += 1;
        }
        return num;
    }

    public int getResultPowerA() {
        return a;
    }

    public int getResultPowerB() {
        return b;
    }

    // Auto-generated

    public void setA(int a) {
        this.a = a;
    }

    public void setB(int b) {
        this.b = b;
    }

    public void setN(int n) {
        this.n = n;
    }

    public void setM(int m) {
        this.m = m;
    }

    public void setDelta(int delta) {
        this.delta = delta;
    }

    public int getDelta() {
        return delta;
    }

    public int getN() {
        return n;
    }

    public int getM() {
        return m;
    }
}
