package com.danforallseasons.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.danforallseasons.DanForAllSeasons;

public class MenuScreen implements Screen {

	private DanForAllSeasons dan;
	private Stage stage;
	private TextButton playButton;
	private TextButton quitButton;
	private BitmapFont font;
	private TextureAtlas atlas;
	private Skin skin;
	private SpriteBatch spriteBatch;

	public MenuScreen(DanForAllSeasons d) {
		this.dan = d;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		stage.act(delta);

		spriteBatch.begin();
		stage.draw();
		spriteBatch.end();
	}

	@Override
	public void resize(int width, int height) {
		if (stage == null)
			stage = new Stage(width, height, true);
		stage.clear();

		Gdx.input.setInputProcessor(stage);

		TextButtonStyle style = new TextButtonStyle();
		style.fontColor = Color.WHITE;
		style.font = font;
		style.up = skin.getDrawable("buttonNormal");
		style.down = skin.getDrawable("buttonPressed");

		playButton = new TextButton("Play", style);
		playButton.setWidth(Gdx.graphics.getWidth() / 3);
		playButton.setHeight(Gdx.graphics.getHeight() / 6);
		playButton
				.setX(Gdx.graphics.getWidth() / 2 - playButton.getWidth() / 2);
		playButton.setY(Gdx.graphics.getHeight() / 2 - playButton.getHeight()
				/ 2 + playButton.getHeight());

		playButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				return true;
			}

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				Gdx.app.log(DanForAllSeasons.LOG, "Setting Screen to Demo");
				dan.setScreen(new DemoScreen(dan));
			}
		});

		quitButton = new TextButton("Quit", style);
		quitButton.setWidth(Gdx.graphics.getWidth() / 3);
		quitButton.setHeight(Gdx.graphics.getHeight() / 6);
		quitButton
				.setX(Gdx.graphics.getWidth() / 2 - quitButton.getWidth() / 2);
		quitButton.setY(Gdx.graphics.getHeight() / 2 - quitButton.getHeight()
				/ 2 - quitButton.getHeight());
		quitButton.center();

		quitButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				return true;
			}

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				Gdx.app.log(DanForAllSeasons.LOG, "Quitting Game");
				Gdx.app.exit();
			}
		});

		stage.addActor(playButton);
		stage.addActor(quitButton);
	}

	@Override
	public void show() {
		spriteBatch = new SpriteBatch();
		atlas = new TextureAtlas("button.pack");
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		skin = new Skin();
		skin.addRegions(atlas);
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
		stage.dispose();
	}

}
