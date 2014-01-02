package com.teremok.influence;

import com.badlogic.gdx.Game;
import com.teremok.influence.model.Calculator;
import com.teremok.influence.screen.GameScreen;
import com.teremok.influence.screen.StartScreen;

public class Influence extends Game {
	@Override
	public void create() {
    	setScreen(new GameScreen(this, GameScreen.GameType.SINGLEPLAYER));
	}
}