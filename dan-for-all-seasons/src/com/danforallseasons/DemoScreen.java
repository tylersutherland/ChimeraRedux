package com.danforallseasons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.tiled.TileAtlas;
import com.badlogic.gdx.graphics.g2d.tiled.TileMapRenderer;
import com.badlogic.gdx.graphics.g2d.tiled.TiledLayer;
import com.badlogic.gdx.graphics.g2d.tiled.TiledLoader;
import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class DemoScreen implements Screen {
	public static final int TILE_WIDTH = 64;
	public static final int TILE_HEIGHT = 64;

	private TiledMap map;
	private TileAtlas atlas;
	private TileMapRenderer renderer;
	private SpriteBatch spriteBatch;
	private BitmapFont font;

	private OrthographicCamera cam;

	private Vector3 tmp = new Vector3();

	public DemoScreen() {
		spriteBatch = new SpriteBatch();
		font = new BitmapFont();
		font.setColor(Color.MAGENTA);
		map = TiledLoader.createMap(Gdx.files.internal("map/demolevel.tmx"));
		atlas = new TileAtlas(map, Gdx.files.internal("map/"));

		// Not sure what these do completely
		int blockWidth = 15;
		int blockHeight = 10;

		renderer = new TileMapRenderer(map, atlas, blockWidth, blockHeight,
				TILE_WIDTH, TILE_HEIGHT);
		cam = new OrthographicCamera(1024, 768);
		cam.position.set(0, 0, 0);

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0, 1, 1, 1);
		renderer.render(cam);
		spriteBatch.begin();
		{

			font.draw(spriteBatch, "FPS: " + Gdx.graphics.getFramesPerSecond(),
					20, 20);
			tmp.set(0, 0, 0);
			cam.unproject(tmp);
			font.draw(spriteBatch, "Location: " + cam.position.x + ","
					+ cam.position.y, 20, 40);
		}
		spriteBatch.end();

		update(delta);
	}

	private void update(float delta) {
		Input input = Gdx.input;
		if (input.isKeyPressed(Input.Keys.D)) cam.position.x++;
		if (input.isKeyPressed(Input.Keys.A)) cam.position.x--;
		if (input.isKeyPressed(Input.Keys.W)) cam.position.y--;
		if (input.isKeyPressed(Input.Keys.S)) cam.position.y++;

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
