package com.danforallseasons.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.danforallseasons.DanForAllSeasons;


public class SplashScreen implements Screen {

	private BitmapFont font;
	private Texture texture;
	private Sprite sprite;
	private SpriteBatch spriteBatch;
	private DanForAllSeasons dan;

	public SplashScreen(DanForAllSeasons d) {
		this.dan = d;
	}

	
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		if ((Gdx.input.isKeyPressed(Keys.ESCAPE)) || (Gdx.input.isTouched())) {
			Gdx.app.log(DanForAllSeasons.LOG, "Setting Screen to Menu");
            dan.setScreen(new MenuScreen(dan));
		}
		

		spriteBatch.begin();{
			sprite.draw(spriteBatch);
			font.draw(spriteBatch, "Press ESC or Click for Main Menu....", 0, Gdx.graphics.getHeight());
			font.draw(spriteBatch, "A game by", (Gdx.graphics.getWidth() / 2) - 35, (Gdx.graphics.getHeight() / 2) + 80);
			font.draw(spriteBatch, "sunFlower studios", (Gdx.graphics.getWidth() / 2) - 60, (Gdx.graphics.getHeight() / 2) - 60);
		}
		spriteBatch.end();
		
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		spriteBatch = new SpriteBatch();
		
		texture = new Texture("YellowFlower.png");
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		
		sprite = new Sprite(texture);
		sprite.setX(Gdx.graphics.getWidth() / 2 - (sprite.getWidth() / 2));
        sprite.setY(Gdx.graphics.getHeight() / 2 - (sprite.getHeight() / 2));
        sprite.scale(1.1f);
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void dispose() {

	}

}
