package com.teremok.influence;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Influence";
		cfg.useGL20 = false;
		cfg.width = 480;
		cfg.height = 720;

		new LwjglApplication(new Influence(), cfg);
	}
}
