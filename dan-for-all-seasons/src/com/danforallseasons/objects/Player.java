package com.danforallseasons.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.danforallseasons.screens.DemoScreen;

enum PhysicsState {
	STANDING, WALKING, JUMPING, FALLING
}

public class Player {
	/** Temporary variable to stop dan from falling through the floor **/
	private static final int FLOOR = 30;

	private Vector2 position;

	private Vector2 speed;

	private PhysicsState pState;

	private Texture spriteSheet;

	private TextureRegion[][] sprites;

	private ObjectMap<String, Animation> animations;

	/** The name of the animation currently playing **/
	private String currentAnimation;

	/** keeps track of what frame to show regardless of animation **/
	private float stateTime;

	public Player() {
		position = new Vector2(12.5f, 30);
		speed = new Vector2(0, 0);
		pState = PhysicsState.FALLING;

		addAnimations();

	}

	private void addAnimations() {
		initializeSpriteSheet(Gdx.files.internal("danSpriteSheet.png"));

		currentAnimation = "standingFront";
		stateTime = 0;

		animations = new ObjectMap<String, Animation>();

		animations.put("standingFront", new Animation(0, sprites[0][0]));
		animations.put("standingRight", new Animation(0, sprites[0][1]));
		animations.put("standingLeft", new Animation(0, sprites[0][3]));

		animations.put("walkingRight", new Animation(0.2f, sprites[1][0],
				sprites[0][1], sprites[1][2]));
		animations.put("walkingLeft", new Animation(0.2f, sprites[2][0],
				sprites[0][3], sprites[2][2]));

		animations.get("walkingRight").setPlayMode(Animation.LOOP_PINGPONG);
		animations.get("walkingLeft").setPlayMode(Animation.LOOP_PINGPONG);
	}

	public float getX() {
		return position.x;
	}

	public float getY() {
		return position.y;
	}
	
	public void setX(float incomingX) {
		position.x = incomingX;
	}
	
	public void setY(float incomingY) {
		position.y = incomingY;
	}

	public PhysicsState getPState()
	{
		return pState;
	}
	
	public void initializeSpriteSheet(FileHandle handle) {
		spriteSheet = new Texture(handle);
		sprites = TextureRegion.split(spriteSheet, 64, 128);
	}

	public void update(float deltaTime) {
		updatePosition();
		updateAnimation(deltaTime);
	}

	private void updateAnimation(float deltaTime) {
		stateTime += deltaTime;

		if (pState == PhysicsState.STANDING) {
			if (movingLeft()) {
				currentAnimation = "standingLeft";
			} else if (movingRight()) {
				currentAnimation = "standingRight";
			} else {
				currentAnimation = "standingFront";
			}
		} else {
			if (movingLeft()) {
				currentAnimation = "walkingLeft";
			} else if (movingRight()) {
				currentAnimation = "walkingRight";
			}
		}
	}

	private void updatePosition() {

		if (pState == PhysicsState.FALLING || pState == PhysicsState.JUMPING) {
			// This is basically gravity
			speed.add(0, -DemoScreen.unitScale);
			speed.x /= 1.05f;
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
		TextureRegion currentTex = animations.get(currentAnimation)
				.getKeyFrame(stateTime);

		batch.draw(currentTex, getX(), getY(), 0, 0,
				currentTex.getRegionWidth(), currentTex.getRegionHeight(),
				DemoScreen.unitScale, DemoScreen.unitScale, 0);

	}

	public void jump() {
		speed.y = DemoScreen.unitScale * 15;

		pState = PhysicsState.JUMPING;

	}

	public void addSpeed(float sX, float sY) {
		speed.add(sX * DemoScreen.unitScale, sY * DemoScreen.unitScale);
	}

	public void walk() {
		pState = PhysicsState.WALKING;
	}

	public void stop() {
		if (onGround()) pState = PhysicsState.STANDING;
	}

	public boolean onGround() {
		return pState == PhysicsState.STANDING
				|| pState == PhysicsState.WALKING;
	}

	public boolean movingLeft() {
		return speed.x < 0;
	}

	public boolean movingRight() {
		return speed.x > 0;
	}
}
