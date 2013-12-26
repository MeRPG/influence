package com.teremok.influence;

import com.badlogic.gdx.Game;
import com.teremok.influence.model.Calculator;
import com.teremok.influence.screen.GameScreen;

public class Influence extends Game {
	@Override
	public void create() {
        testCalculator();
		setScreen(new GameScreen(this));
	}

    private void testCalculator() {
        Calculator.test(2,2,2,10);
        Calculator.test(2,2,4,5);
        Calculator.test(2,2,6,10);
        Calculator.test(2,1,6,1);
        Calculator.test(2,2,6,4);
        Calculator.test(2,2,7,7);
        Calculator.test(2,1,7,4);
        Calculator.test(2,2,7,8);
        Calculator.test(2,2,7,6);
        Calculator.test(2,2,7,8);
        Calculator.test(2,2,7,2);
        Calculator.test(2,2,7,5);
        Calculator.test(2,2,8,8);
        Calculator.test(2,2,8,9);
        Calculator.test(2,2,8,6);
        Calculator.test(2,2,8,6);
        Calculator.test(2,4,8,14);
        Calculator.test(2,3,8,12);
        Calculator.test(2,4,8,9);
        Calculator.test(2,3,8,14);
        Calculator.test(2,3,8,11);
        Calculator.test(2,2,9,6);
        Calculator.test(2,4,9,10);
        Calculator.test(3,2,10,2);
        Calculator.test(3,2,10,5);
        Calculator.test(3,2,10,6);
        Calculator.test(2,2,10,6);
        Calculator.test(3,1,10,3);
        Calculator.test(3,1,11,2);
        Calculator.test(3,2,11,5);
        Calculator.test(2,1,12,5);
        Calculator.test(4,3,12,6);
        Calculator.test(2,2,12,4);
        Calculator.test(4,2,13,7);
        Calculator.test(3,3,13,11);
        Calculator.test(3,2,13,8);
        Calculator.test(4,2,14,7);
        Calculator.test(4,3,14,10);
        Calculator.test(4,2,15,7);
        Calculator.test(4,2,16,6);
        Calculator.test(4,2,17,3);
        Calculator.test(4,2,18,6);
        Calculator.test(4,2,18,5);
        Calculator.test(3,2,18,8);
    }
}