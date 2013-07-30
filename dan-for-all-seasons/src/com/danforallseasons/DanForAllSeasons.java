package com.danforallseasons;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.danforallseasons.screens.SplashScreen;

public class DanForAllSeasons extends Game {

	/**
	 * A handy constant that will allow for multi-platform logging via
	 * Gdx.app.log
	 */
	public static final String LOG = "A Dan for all Seasons";
	public static final String TAKEMEOUT = "TEMP LOG";

	@Override
	public void create() {
		Gdx.app.log(DanForAllSeasons.LOG, "Setting Screen to Splash Screen");
		setScreen(new SplashScreen(this));
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}

}
