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
		int screenX = (int) cam.position.x;
		int screenY = (int) cam.position.y;
		int screenWidth = (int) cam.viewportWidth;
		int screenHeight = (int) cam.viewportHeight;
		batch.begin();
		{
			// NOTE: i is y and x is j, y coords are upside down so subtract
			// from the height
			for (TiledLayer layer : map.layers) {
				for (int i = 0; i < layer.getHeight(); i++)
					for (int j = 0; j < layer.getWidth(); j++) {
						int tileId = layer.tiles[i][j];
						if (tileId == 0) continue;
						batch.draw(atlas.getRegion(tileId), screenX + j * 64,
								screenHeight - (screenY + i * 64));
					}
			}
		}
		batch.end();

	}

	public void dispose() {
		batch.dispose();
	}

}
