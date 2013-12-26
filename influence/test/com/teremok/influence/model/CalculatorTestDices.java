package com.teremok.influence.model;

import junit.framework.Assert;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Alexx on 26.12.13
 */
@RunWith(Parameterized.class)
public class CalculatorTestDices {

    int num;
    int dices;

    public CalculatorTestDices(int num, int dices) {
        this.num = num;
        this.dices = dices;
    }

    @org.junit.Test
    public void testCountDices() throws Exception {
        int realDices = Calculator.countDices(num);
        Assert.assertEquals(dices, realDices);
    }

    @Parameterized.Parameters
    public static List<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {1,1},
                {6,1},
                {7,2},
                {13,3},
                {19,4},
                {25,5},
                {12,2},
                {44,8},
                {5,1},
                {30,5},
                {32,6},
        });

    }
}
