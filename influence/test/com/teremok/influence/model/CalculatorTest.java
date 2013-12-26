package com.teremok.influence.model;

import junit.framework.Assert;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Alexx on 26.12.13
 */

@RunWith(Parameterized.class)
public class CalculatorTest {

    BufferedReader br;

    private final int a;
    private final int b;
    private final int n;
    private final int m;
    private final int resA;
    private final int resB;

    public CalculatorTest(int a, int b, int n, int m, int resA, int resB) {
        this.a = a;
        this.b = b;
        this.n = n;
        this.m = m;
        this.resA = resA;
        this.resB = resB;
    }
    
    @org.junit.Test
    public void testFight() throws Exception {

        Calculator.setA(a);
        Calculator.setB(b);
        Calculator.setN(n);
        Calculator.setM(m);

        int delta = n - m;

        Calculator.setDelta(delta);
        Calculator.calculateResults();

        Assert.assertEquals("delta",delta, Calculator.getDelta());
        Assert.assertEquals("resA",resA, Calculator.getResultPowerA());
        Assert.assertEquals("resB", resB, Calculator.getResultPowerB());
    }

    @Parameterized.Parameters
    public static List<Object[]> dataFromCW() {
        return Arrays.asList(new Object[][]{
                {2,2,2,10,1,2},
                {2,2,4,5,1,1},
                {2,2,6,10,1,2},
                {2,1,6,1,1,1},
                {2,2,6,4,1,1},
                {2,2,7,7,1,1},
                {2,1,7,4,1,1},
                {2,2,7,8,1,1},
                {2,2,7,6,1,1},
                {2,2,7,8,1,1},
                {2,2,7,2,1,1},
                {2,2,7,5,1,1},
                {2,2,8,8,1,1},
                {2,2,8,9,1,1},
                {2,2,8,6,1,1},
                {2,2,8,6,1,1},
                {2,4,8,14,1,3},
                {2,3,8,12,1,2},
                {2,4,8,9,1,2},
                {2,3,8,14,1,2},
                {2,3,8,11,1,2},
                {2,2,9,6,1,1},
                {2,4,9,10,1,2},
                {3,2,10,2,1,2},
                {3,2,10,5,1,2},
                {3,2,10,6,1,2},
                {2,2,10,6,1,1},
                {3,1,10,3,1,2},
                {3,1,11,2,1,2},
                {3,2,11,5,1,2},
                {2,1,12,5,1,1},
                {4,3,12,6,1,2},
                {2,2,12,4,1,1},
                {4,2,13,7,1,2},
                {3,3,13,11,1,1},
                {3,2,13,8,1,2},
                {4,2,14,7,1,2},
                {4,3,14,10,1,2},
                {4,2,15,7,1,2},
                {4,2,16,6,1,2},
                {4,2,17,3,1,3},
                {4,2,18,6,1,2},
                {4,2,18,5,1,3},
                {3,2,18,8,1,2},
        });
    }

}
