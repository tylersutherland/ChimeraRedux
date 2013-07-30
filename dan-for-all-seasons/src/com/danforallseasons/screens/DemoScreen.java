package com.danforallseasons.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.danforallseasons.DanForAllSeasons;

public class DemoScreen implements Screen {

	private static final String PAUSE_MSG = "Game Paused\r\nPress R to Resume";

	private int width = Gdx.graphics.getWidth();
	private int height = Gdx.graphics.getHeight();
	private float unitScale = 1 / 32f;
	/* Rendering */
	private SpriteBatch spriteBatch;
	private TiledMap tMap;
	private OrthogonalTiledMapRenderer tMapRenderer;

	private SpriteBatch fontSpriteBatch;
	private BitmapFont font;
	private OrthographicCamera cam;

	private boolean gamePaused;

	private DanForAllSeasons game;

	public DemoScreen(DanForAllSeasons dan) {
		game = dan;
		spriteBatch = new SpriteBatch();
		fontSpriteBatch = new SpriteBatch();
		font = new BitmapFont();
		font.setColor(Color.MAGENTA);
		cam = new OrthographicCamera();
		cam.setToOrtho(false, width * unitScale, height * unitScale);
		cam.position.set(12.5f, 45, 1);
		cam.update();

		tMap = new TmxMapLoader().load("map/demo.tmx");
		tMapRenderer = new OrthogonalTiledMapRenderer(tMap, unitScale);
		gamePaused = false;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0, 1, 1, 1);

		if (gamePaused) {
			renderPauseMenu();
		} else {
			renderGame(delta);
		}
	}

	private void renderGame(float delta) {

		renderMap();

		renderDebugHUD();

		update(delta);
	}

	private void renderMap() {
		tMapRenderer.setView(cam);
		tMapRenderer.render();
	}

	private void renderDebugHUD() {
		fontSpriteBatch.begin();
		{
			font.draw(fontSpriteBatch,
					"FPS: " + Gdx.graphics.getFramesPerSecond(), 20, 20);
			font.draw(fontSpriteBatch, "Location: " + cam.position.x + ","
					+ cam.position.y, 20, 80);
			font.draw(fontSpriteBatch, "Zoom: " + cam.zoom, 20, 60);
			font.draw(
					fontSpriteBatch,
					"Press P to Pause",
					Gdx.graphics.getWidth()
							- font.getBounds("Press P to Pause").width,
					Gdx.graphics.getHeight());
			font.draw(fontSpriteBatch, "Press Esc to Quit", 0,
					Gdx.graphics.getHeight());

			font.draw(
					fontSpriteBatch,
					"Press R to Restart",
					Gdx.graphics.getWidth() / 2
							- font.getBounds("Press R to Restart").width / 2,
					Gdx.graphics.getHeight());

		}
		fontSpriteBatch.end();
	}

	private void renderPauseMenu() {
		Gdx.gl.glClearColor(0.5f, 0.9f, 0.9f, 1);

		fontSpriteBatch.begin();
		font.drawMultiLine(fontSpriteBatch, PAUSE_MSG, Gdx.graphics.getWidth()
				/ 2 - font.getBounds(PAUSE_MSG).width / 2 + 50,
				Gdx.graphics.getHeight() / 2 + font.getBounds(PAUSE_MSG).height
						/ 2);
		fontSpriteBatch.end();

		if (Gdx.input.isKeyPressed(Keys.R)) {
			Gdx.app.log(DanForAllSeasons.LOG, "Resuming Game");
			resumeGame();
		}
	}

	private void update(float delta) {
		Input input = Gdx.input;

		if (input.isKeyPressed(Keys.P)) {
			Gdx.app.log(DanForAllSeasons.LOG, "Pausing Game");
			pauseGame();
		}
		if (input.isKeyPressed(Keys.ESCAPE)) {
			Gdx.app.log(DanForAllSeasons.LOG, "Quitting Game");
			Gdx.app.exit();
		}

		if (input.isKeyPressed(Keys.R)) {
			Gdx.app.log(DanForAllSeasons.LOG, "Restting Demo");
			game.setScreen(new DemoScreen(game));
		}

		if (input.isKeyPressed(Keys.UP)) cam.translate(new Vector2(0, 1));
		if (input.isKeyPressed(Keys.DOWN)) cam.translate(new Vector2(0, -1));
		if (input.isKeyPressed(Keys.LEFT)) cam.translate(new Vector2(1, 0));
		if (input.isKeyPressed(Keys.RIGHT)) cam.translate(new Vector2(-1, 0));
		if (input.isKeyPressed(Keys.Q)) cam.zoom += 0.1f;
		if (input.isKeyPressed(Keys.E)) cam.zoom -= 0.1f;
		cam.update();
	}

	private void pauseGame() {
		gamePaused = true;
	}

	private void resumeGame() {
		gamePaused = false;
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {
		pauseGame();
	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}
}
