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
	// TODO: It's probably a good idea to get rid of the pack file completely
	// the data is in the tmx file
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
			TextureRegion region = new TextureRegion(spriteSheet, x, y,
					tileWidth, tileHeight);

			region.flip(false, true);

			regionMap.put(index, region);

		}
	}

	private int getLinearPosition(int x, int y, int w, int h) {

		x /= tileWidth;
		y /= tileHeight;
		w /= tileWidth;
		h /= tileHeight;

		return x + y * w + indexOffset;

	}

	@Override
	public void dispose() {
		spriteSheet.dispose();
	}

	/**
	 * 
	 * @param tileId
	 *            is the id of the texture in the sprite sheet
	 * @return the texture that belongs to this id
	 */
	public TextureRegion getRegion(int tileId) {

		TextureRegion tr = regionMap.get(tileId);

		if (tr == null) {

			int w = spriteSheet.getWidth() / tileWidth;
			int h = spriteSheet.getHeight() / tileHeight;
			int x = tileId % w - 1;
			int y = tileId % h + x;
			tr = new TextureRegion(spriteSheet, x * tileWidth, y * tileHeight,
					tileWidth, tileHeight);
			tr.flip(false, true);
		}

		return tr;

	}
}
