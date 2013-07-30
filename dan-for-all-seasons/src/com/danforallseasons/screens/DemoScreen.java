package com.danforallseasons.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.danforallseasons.DanForAllSeasons;
import com.danforallseasons.objects.Player;

public class DemoScreen implements Screen {
	/** Pause screen message **/
	private static final String PAUSE_MSG = "Game Paused\r\nPress R to Resume";

	// Rendering
	private int width = Gdx.graphics.getWidth();
	private int height = Gdx.graphics.getHeight();
	public static float unitScale = 1 / 32f;

	// Map
	private TiledMap tMap;
	private OrthogonalTiledMapRenderer tMapRenderer;

	// Font
	private SpriteBatch fontSpriteBatch;
	private BitmapFont font;
	private OrthographicCamera cam;

	// Game
	private DanForAllSeasons game;
	private boolean gamePaused;
	private Player player;

	public DemoScreen(DanForAllSeasons dan) {
		game = dan;

		gamePaused = false;

		player = new Player();

		initializeFont();

		initializeCamera();

		initializeMap();

	}

	private void initializeMap() {
		tMap = new TmxMapLoader().load("map/demo.tmx");
		tMapRenderer = new OrthogonalTiledMapRenderer(tMap, unitScale);
	}

	private void initializeCamera() {
		cam = new OrthographicCamera();
		cam.setToOrtho(false, width * unitScale, height * unitScale);
		cam.position.set(12.5f, 45, 1);
		cam.update();
	}

	private void initializeFont() {
		fontSpriteBatch = new SpriteBatch();
		font = new BitmapFont();
		font.setColor(Color.MAGENTA);
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
		update(delta);
	}

	private void renderGame(float delta) {

		renderMap();

		renderEntities();

		renderDebugHUD();

	}

	private void renderEntities() {
		SpriteBatch batch = tMapRenderer.getSpriteBatch();
		batch.begin();
		{
			player.draw(batch);
		}
		batch.end();
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
	}

	private void update(float delta) {
		Input input = Gdx.input;

		pollInput(input);
		player.updatePosition();
		cam.position.set(player.getX(), player.getY(), 1);
		cam.update();
	}

	private void pollInput(Input input) {
		if (input.isKeyPressed(Keys.P)) {
			if (!gamePaused) {
				Gdx.app.log(DanForAllSeasons.LOG, "Pausing Game");
				pauseGame();
			}
		}
		if (input.isKeyPressed(Keys.ESCAPE)) {
			Gdx.app.log(DanForAllSeasons.LOG, "Quitting Game");
			Gdx.app.exit();
		}

		if (input.isKeyPressed(Keys.R)) {

			if (!gamePaused) {
				Gdx.app.log(DanForAllSeasons.LOG, "Restting Demo");
				game.setScreen(new DemoScreen(game));
			} else {
				Gdx.app.log(DanForAllSeasons.LOG, "Resuming Game");
				resumeGame();
			}
		}
		if (input.isKeyPressed(Keys.W) || input.isKeyPressed(Keys.UP)) {
			player.jump();
		}
		if (input.isKeyPressed(Keys.S) || input.isKeyPressed(Keys.DOWN)) {

		}
		if (input.isKeyPressed(Keys.D) || input.isKeyPressed(Keys.RIGHT)) {
			player.addSpeed(1, 0);
		}
		if (input.isKeyPressed(Keys.A) || input.isKeyPressed(Keys.LEFT)) {
			player.addSpeed(-1, 0);
		}

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
