package com.danforallseasons.tiled;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

public class TiledMapRenderer implements Disposable {

	public static final int PIXELS_PER_METER = 64;

	private SpriteBatch batch;

	private TiledMapAtlas atlas;

	private TiledMap map;

	private int tileWidth, tileHeight;

	private int lastRow;

	private int initialRow;

	private int lastCol;

	private int initialCol;

	private int currentLayer;

	private boolean changeLayer;

	private int oldLayer;

	private float elapsedTime;

	private ShaderProgram fadeOutShader;

	private ShaderProgram defaultShader;

	public TiledMapRenderer(TiledMap map, TiledMapAtlas atlas) {
		this.map = map;
		this.atlas = atlas;
		this.tileWidth = map.tileWidth;
		this.tileHeight = map.tileHeight;
		batch = new SpriteBatch();

		ShaderProgram.pedantic = false;

		defaultShader = SpriteBatch.createDefaultShader();
		fadeOutShader = new ShaderProgram(
				Gdx.files.internal("shaders/fadeOut.vert"),
				Gdx.files.internal("shaders/fadeOut.frag"));

	}

	public void render(OrthographicCamera cam) {
		// TODO: This method probably needs tons of optimization
		Vector3 tmp = new Vector3();
		tmp.set(0, 0, 0);
		cam.unproject(tmp);

		// batch.setProjectionMatrix(cam.combined);
		int x = (int) tmp.x;
		int y = (int) tmp.y;
		int width = (int) cam.viewportWidth;
		int height = (int) cam.viewportHeight;

		int overdrawX = 2;
		int overdrawY = 2;
		lastRow = (int) (y + height) + overdrawY;
		lastRow = (lastRow >= (map.height - 1)) ? map.height - 1 : lastRow;
		initialRow = (int) y - overdrawY;
		initialRow = (initialRow > 0) ? initialRow : 0; // Clamp initial Row
														// > 0

		lastCol = (int) (x + width) + overdrawX;
		lastCol = (lastCol >= (map.width - 1)) ? map.width - 1 : lastCol;
		initialCol = (int) x - overdrawX;
		initialCol = (initialCol > 0) ? initialCol : 0; // Clamp initial Col
														// > 0
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		{
			// NOTE: i is y and x is j, y coords are upside down so subtract
			// from the height

			for (int i = initialRow; i < lastRow; i++)
				for (int j = initialCol; j < lastCol; j++) {
					int tileId = map.layers.get(currentLayer).tiles[i][j];
					if (tileId == 0) continue;
					
					batch.setShader(defaultShader);

					batch.draw(atlas.getRegion(tileId), j, i, 0, 0, tileWidth,
							tileHeight, 1f / PIXELS_PER_METER,
							1f / PIXELS_PER_METER, 0);

					if (changeLayer) {
						batch.setShader(fadeOutShader);
						fadeOutShader.setUniform1fv("alpha_mod",
								new float[] { elapsedTime }, 0, 1);

						int oldTileId = map.layers.get(oldLayer).tiles[i][j];

						batch.draw(atlas.getRegion(oldTileId), j, i, 0, 0,
								tileWidth, tileHeight, 1f / PIXELS_PER_METER,
								1f / PIXELS_PER_METER, 0);

						elapsedTime += Gdx.graphics.getDeltaTime();
						if (elapsedTime > 30) changeLayer = false;
					}
				}

		}
		batch.end();

	}

	public void dispose() {
		batch.dispose();
	}

	public int getLastRow() {
		return lastRow;
	}

	public int getInitialRow() {
		return initialRow;
	}

	public int getLastCol() {
		return lastCol;
	}

	public int getInitialCol() {
		return initialCol;
	}

	public void changeLayer() {
		if (!changeLayer) {
			changeLayer = true;
			elapsedTime = 0;
			oldLayer = currentLayer;
			currentLayer++;
			currentLayer %= map.layers.size();
		}
	}

}
