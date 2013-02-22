package com.danforallseasons.physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.tiled.TiledLayer;
import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class PhysicsHelper {

	/**
	 * Uses the shape and some properties of tiles to get the map shape by
	 * column
	 * 
	 * @param map
	 *            is the tmx map
	 * @param start
	 *            is the point where you want to start getting the shape of the
	 *            map
	 * @return the collision shapes
	 */
	public static Array<Vector2[]> getCollisionShapes(TiledMap map,
			Vector2 start) {

		TiledLayer layer = map.layers.get(0);
		Array<Vector2[]> collisionShapes = new Array<Vector2[]>();
		int width = layer.getWidth();
		int height = layer.getHeight();
		Array<Vector2> shape = new Array<Vector2>();

		for (int i = (int) start.x; i < width; i++) {
			for (int j = (int) start.y; j < height; j++) {
				int tileId = layer.tiles[j][i];
				if (tileId == 0)
					continue;

				shape.clear();
				if (map.getTileProperty(tileId, "shape") == null) {
					shape.add(new Vector2(i, j));
					shape.add(new Vector2((i + 1), j));
					collisionShapes.add(shape.toArray(Vector2.class));
				} else if (map.getTileProperty(tileId, "shape")
						.equals("uphill")) {
					shape.add(new Vector2(i, (j + 1)));
					shape.add(new Vector2((i + 1), j));
					collisionShapes.add(shape.toArray(Vector2.class));
				} else if (map.getTileProperty(tileId, "shape").equals(
						"downhill")) {
					shape.add(new Vector2(i, j));
					shape.add(new Vector2((i + 1), j + 1));
					collisionShapes.add(shape.toArray(Vector2.class));
				} else
					continue;
				break;
			}
		}
		return collisionShapes;
	}

	/**
	 * 
	 * Uses the shape and some properties of tiles to get the map shape by
	 * column
	 * 
	 * @param map
	 *            is the tmx map
	 * @return the collision shapes
	 */
	public static Array<Vector2[]> getCollisionShapes(TiledMap map) {
		return getCollisionShapes(map, new Vector2(0, 0));
	}

}
