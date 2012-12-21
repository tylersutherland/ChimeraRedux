package com.danforallseasons.tiled;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.IntMap;

public class TiledMapAtlas implements Disposable {

	private IntMap<TextureRegion> regionMap;
	// TODO: Add functionality for multiple sprite sheets in the same pack file
	private Texture spriteSheet;
	private int tileWidth;
	private int tileHeight;
	private int indexOffset;
	private String path;

	/**
	 * Creates an atlas for use with TiledMapRenderer
	 * 
	 * @param map
	 *            is the map that uses the sheet texturepacker creates
	 * @param packFile
	 *            is the pack file created by the texturepacker
	 */

	public TiledMapAtlas(TiledMap map, FileHandle packFile) {
		String contents = packFile.readString();
		String packFilePath = packFile.pathWithoutExtension();
		path = packFilePath.substring(0, packFilePath.length()
				- packFile.nameWithoutExtension().length());
		tileWidth = map.tileWidth;
		tileHeight = map.tileHeight;
		indexOffset = 1;
		generateMapInfo(contents);

	}

	private void generateMapInfo(String contents) {
		regionMap = new IntMap<TextureRegion>();
		int currentIndex = 0;
		spriteSheet = new Texture(path
				+ contents.substring(0, contents.indexOf('\n')).trim());
		while ((currentIndex = contents.indexOf("xy:", currentIndex)) != -1) {
			int endIndex = contents.indexOf(',', currentIndex);
			int x = Integer.parseInt(contents.substring(currentIndex + 3,
					endIndex).trim());
			currentIndex = endIndex + 1;
			endIndex = contents.indexOf('\n', currentIndex);
			int y = Integer.parseInt(contents.substring(currentIndex, endIndex)
					.trim());

			currentIndex = contents.indexOf("index:", currentIndex) + 6;
			endIndex = contents.indexOf('\n', currentIndex);

			int index = getLinearPosition(x, y, spriteSheet.getWidth(),
					spriteSheet.getHeight());

			regionMap.put(index, new TextureRegion(spriteSheet, x, y,
					tileWidth, tileHeight));

		}
	}

	private int getLinearPosition(int x, int y, int w, int h) {
		if (x > 0) x -= 2;
		if (y > 0) y -= 2;

		x /= 64;
		y /= 64;
		w /= 64;
		w -= 1;
		h /= 64;
		h -= 1;

		Gdx.app.log("X,Y,W,D,I",
				String.format("%d,%d,%d,%d,%d", x, y, w, h, x + y * w + 1));

		return x + y * w + 1;

	}

	@Override
	public void dispose() {
		spriteSheet.dispose();
	}

	public TextureRegion getRegion(int tileId) {
		return regionMap.get(tileId);

	}
}
