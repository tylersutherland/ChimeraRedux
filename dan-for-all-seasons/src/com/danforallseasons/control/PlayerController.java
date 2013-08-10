package com.danforallseasons.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.danforallseasons.DanForAllSeasons;
import com.danforallseasons.objects.Player;

public class PlayerController implements GestureListener {
	private Player player;

	public PlayerController(Player player) {
		this.player = player;
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		Gdx.app.log(DanForAllSeasons.LOG, "touch down with x:" + x + " y:" + y
				+ " pointer:" + pointer + " button:" + button);
		if (player.onGround()) {
			player.walk();
			if (x > 2 * Gdx.graphics.getWidth() / 3) {
				player.addSpeed(2, 0);
			} else if (x < 1 * Gdx.graphics.getWidth() / 3) {
				player.addSpeed(-2, 0);
			}
		} else {
			if (x > Gdx.graphics.getWidth() / 2) {
				player.addSpeed(2.5f, 0);
			} else {
				player.addSpeed(-2.5f, 0);
			}
		}
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		Gdx.app.log(DanForAllSeasons.LOG, "tap with x:" + x + " y:" + y
				+ " count:" + count + " button:" + button);
		if (x > Gdx.graphics.getWidth() / 3
				&& x < 2 * Gdx.graphics.getWidth() / 3) if (count > 0) {
			player.jump();
		}
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		Gdx.app.log(DanForAllSeasons.LOG, "long press with x:" + x + " y:" + y);
		if (player.onGround()) {
			if (x > 2 * Gdx.graphics.getWidth() / 3) {
				player.addSpeed(5, 0);
			} else if (x < 1 * Gdx.graphics.getWidth() / 3) {
				player.addSpeed(-5, 0);
			}
		}
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		Gdx.app.log(DanForAllSeasons.LOG, "fling with velx:" + velocityX
				+ " vely:" + velocityY);
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		Gdx.app.log(DanForAllSeasons.LOG, "pan with x:" + x + " y:" + y
				+ " dx:" + deltaX + " dy:" + deltaY);
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		Gdx.app.log(DanForAllSeasons.LOG, "zoom with initDist:"
				+ initialDistance + " dist:" + distance);
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		Gdx.app.log(DanForAllSeasons.LOG,
				"pinch with initialPoint1:" + initialPointer1.toString()
						+ " 2:" + initialPointer2.toString() + "\n1:"
						+ pointer1.toString() + " 2:" + pointer2.toString());
		return false;
	}

}
