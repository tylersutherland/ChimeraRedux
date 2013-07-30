package com.danforallseasons.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.danforallseasons.screens.DemoScreen;

public class Player {
	private static final int FLOOR = 60;
	private Vector2 position;
	private Vector2 speed;
	private PhysicsState pState;
	private TextureRegion tex;

	public Player() {
		position = new Vector2(12.5f, 60);
		speed = new Vector2();
		pState = PhysicsState.FALLING;
		tex = new TextureRegion(new Texture(
				Gdx.files.internal("DanWalking.png")));
	}

	public float getX() {
		return position.x;
	}

	public float getY() {
		return position.y;
	}

	public void updatePosition() {
		

		if (pState == PhysicsState.FALLING || pState == PhysicsState.JUMPING) {
			// This is basically gravity
			speed.add(0, -DemoScreen.unitScale);
			if (speed.y < 0) pState = PhysicsState.FALLING;
		} else if (pState == PhysicsState.STANDING) {
			speed.y = 0;
			speed.x /= 1.15f;
		}

		// TODO: get rid of this hardcoded limit
		if (position.y < FLOOR && pState == PhysicsState.FALLING) {
			pState = PhysicsState.STANDING;
			position.y = FLOOR;
		}
		
		position.add(speed);

	}

	public void draw(SpriteBatch batch) {
		batch.draw(tex, getX(), getY(), 0, 0, tex.getRegionWidth(),
				tex.getRegionHeight(), DemoScreen.unitScale,
				DemoScreen.unitScale, 0);
	}

	public void jump() {
		if (onGround()) {
			speed.y = DemoScreen.unitScale * 15;
			speed.x /= 1.15f;
			pState = PhysicsState.JUMPING;
		}
	}

	public void addSpeed(float sX, float sY) {
		speed.add(sX * DemoScreen.unitScale, sY * DemoScreen.unitScale);
	}

	public void walk() {
		if (onGround()) {
			pState = PhysicsState.WALKING;
		}
	}

	public void stop() {
		if (onGround()) pState = PhysicsState.STANDING;
	}

	public boolean onGround() {
		return pState == PhysicsState.STANDING
				|| pState == PhysicsState.WALKING;
	}
}
