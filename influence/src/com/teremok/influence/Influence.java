package com.teremok.influence;

import com.badlogic.gdx.Game;
import com.teremok.influence.screen.SplashScreen;

public class Influence extends Game {
	@Override
	public void create() {		
		setScreen(new SplashScreen(this));
	}
}
