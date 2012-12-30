package com.danforallseasons.physics;

import com.badlogic.gdx.graphics.g2d.tiled.TiledLayer;
import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class PhysicsHelper {

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
					j = findBottomOfShape(i, j, map, shape);

					collisionShapes.add(shape.toArray(Vector2.class));
				}
				if (map.getTileProperty(tileId, "shape") == "uphill") {
					shape.add(new Vector2(i, (j + 1)));
					shape.add(new Vector2((i + 1), j));
					j = findBottomOfShape(i, j, map, shape);

					collisionShapes.add(shape.toArray(Vector2.class));
				}
				if (map.getTileProperty(tileId, "shape") == "downhill") {
					shape.add(new Vector2(i, (j)));
					shape.add(new Vector2((i + 1), j + 1));
					j = findBottomOfShape(i, j, map, shape);

					collisionShapes.add(shape.toArray(Vector2.class));
				}

			}
		}
		return collisionShapes;
	}

	private static int findBottomOfShape(int i, int j, TiledMap map,
			Array<Vector2> shape) {

		while (map.layers.get(0).tiles[j + 1][i] != 0) {
			j++;
		}

		shape.add(new Vector2((i + 1), (j + 1)));
		shape.add(new Vector2(i, (j + 1)));
		return j + 1;
	}

	public static Array<Vector2[]> getCollisionShapes(TiledMap map) {
		return getCollisionShapes(map, new Vector2(0, 0));
	}
}
