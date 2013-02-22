package com.danforallseasons.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.danforallseasons.DanForAllSeasons;
import com.danforallseasons.tweenaccessors.SpriteAccessor;

public class SplashScreen implements Screen {

	private Texture texture;
	private Sprite sprite;
	private SpriteBatch spriteBatch;
	private DanForAllSeasons dan;
	private TweenManager manager;

	public SplashScreen(DanForAllSeasons d) {
		this.dan = d;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		manager.update(delta);

		spriteBatch.begin();
		sprite.setPosition(Gdx.graphics.getWidth() / 2 - sprite.getWidth() / 2,
				Gdx.graphics.getHeight() / 2);
		sprite.draw(spriteBatch);
		spriteBatch.end();

		if ((Gdx.input.isKeyPressed(Keys.SPACE)) || (Gdx.input.isTouched())) {
			Gdx.app.log(DanForAllSeasons.LOG, "Setting Screen to Menu");
			dan.setScreen(new MenuScreen(dan));
		}
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {
		texture = new Texture("YellowFlower.png");
		texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

		sprite = new Sprite(texture);
		sprite.setColor(1, 1, 1, 0);
		sprite.scale(2f);

		spriteBatch = new SpriteBatch();

		Tween.registerAccessor(Sprite.class, new SpriteAccessor());

		manager = new TweenManager();

		TweenCallback callback = new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				tweenCompleted();
			}
		};

		Tween.to(sprite, SpriteAccessor.ALPHA, 1.25f).target(1)
				.ease(TweenEquations.easeInQuad).repeatYoyo(1, 1.75f)
				.setCallback(callback)
				.setCallbackTriggers(TweenCallback.COMPLETE).start(manager);

	}

	private void tweenCompleted() {
		Gdx.app.log(DanForAllSeasons.LOG, "Setting Screen to Menu");
		dan.setScreen(new MenuScreen(dan));
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
