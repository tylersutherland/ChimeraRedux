package com.danforallseasons;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "dan-for-all-seasons";
		cfg.useGL20 = false;
		cfg.width = 1024;
		cfg.height = 768;
		new LwjglApplication(new DanForAllSeasons(), cfg);
	}
}
