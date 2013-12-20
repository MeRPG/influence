package com.teremok.influence;

import com.badlogic.gdx.Game;
import com.teremok.influence.screen.StartScreen;

public class Influence extends Game {
	@Override
	public void create() {		
		setScreen(new StartScreen(this));
	}
}
