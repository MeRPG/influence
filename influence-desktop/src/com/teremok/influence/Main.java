package com.teremok.influence;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import java.util.Locale;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Influence";
		cfg.useGL30 = false;
		cfg.width = 320;
		cfg.height = 550;

        Locale locale = Locale.getDefault(Locale.Category.DISPLAY);

		new LwjglApplication(new Influence(locale), cfg);
	}
}
