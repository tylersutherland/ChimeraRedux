package com.danforallseasons.tiled;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.tiled.TiledLayer;
import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

public class TiledMapRenderer implements Disposable {

	private SpriteBatch batch;

	private TiledMapAtlas atlas;
	private TiledMap map;

	private int tileWidth, tileHeight;

	private int lastRow;

	private int initialRow;

	private int lastCol;

	private int initialCol;

	public TiledMapRenderer(TiledMap map, TiledMapAtlas atlas) {
		this.map = map;
		this.atlas = atlas;
		this.tileWidth = map.tileWidth;
		this.tileHeight = map.tileHeight;
		batch = new SpriteBatch();
	}

	public void render(OrthographicCamera cam) {
		// TODO: This method probably needs tons of optimization
		Vector3 tmp = new Vector3();
		tmp.set(0, 0, 0);
		cam.unproject(tmp);
		//batch.setProjectionMatrix(cam.combined);
		int x = (int) tmp.x;
		int y = (int) tmp.y;
		int width = (int) cam.viewportWidth;
		int height = (int) cam.viewportHeight;

		lastRow = (int) (y + height) / 64;

		initialRow = (int) y / 64;
		initialRow = (initialRow > 0) ? initialRow : 0; // Clamp initial Row
														// > 0

		lastCol = (int) (x + width) / 64;
		initialCol = (int) x / 64;
		initialCol = (initialCol > 0) ? initialCol : 0; // Clamp initial Col
														// > 0

		batch.begin();
		{
			// NOTE: i is y and x is j, y coords are upside down so subtract
			// from the height
			for (TiledLayer layer : map.layers) {
				for (int i = initialRow; i < lastRow; i++)
					for (int j = initialCol; j < lastCol; j++) {
						int tileId = layer.tiles[i][j];
						if (tileId == 0) continue;
						Gdx.app.log("XY", x + "," + y);
						batch.draw(atlas.getRegion(tileId), x + j * 64, height
								- (y + i * 64));
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

}
