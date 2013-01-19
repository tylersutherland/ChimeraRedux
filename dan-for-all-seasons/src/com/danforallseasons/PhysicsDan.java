package com.danforallseasons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.danforallseasons.tiled.TiledMapRenderer;

public class PhysicsDan {
	private Body body;
	private boolean jump;

	private Animation walkAnimation;
	private Texture walkSheet;
	private TextureRegion standing;
	private TextureRegion[] walkFrames;
	private TextureRegion currentFrame;

	private boolean movingRight;
	private boolean movingLeft;
	private float stateTime;

	public PhysicsDan(float x, float y, World world) {

		PolygonShape danPoly = new PolygonShape();
		danPoly.setAsBox(0.5f, 1f);
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		body = world.createBody(bodyDef);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = danPoly;
		fixtureDef.filter.groupIndex = 0;
		fixtureDef.friction = 0.5f;
		fixtureDef.density = 1.0f;
		body.createFixture(fixtureDef);
		danPoly.dispose();

		standing = new TextureRegion(
				new Texture(Gdx.files.internal("Dan2.png")));
		standing.flip(false, true);

		walkSheet = new Texture("danWalkSheet.png");
		TextureRegion[][] temp = TextureRegion.split(walkSheet,
				walkSheet.getWidth() / 2, walkSheet.getHeight() / 2);
		walkFrames = new TextureRegion[4];

		int index = 0;
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				walkFrames[index++] = temp[i][j];
			}
		}

		for (int i = 0; i < 4; i++) {
			walkFrames[i].flip(false, true);
		}

		walkAnimation = new Animation(0.05f, walkFrames);
		stateTime = 0f;
	}

	public void draw(SpriteBatch batch) {
		Vector2 pos = body.getPosition().add(-0.5f, -1.0f);
		stateTime += Gdx.graphics.getDeltaTime();

		currentFrame = walkAnimation.getKeyFrame(stateTime, true);

		if (movingRight) {
			batch.draw(currentFrame, pos.x, pos.y, 0, 0,
					currentFrame.getRegionWidth(),
					currentFrame.getRegionHeight(),
					1f / TiledMapRenderer.PIXELS_PER_METER,
					1f / TiledMapRenderer.PIXELS_PER_METER, 0);
		}
		else if (movingLeft) {
			batch.draw(currentFrame, pos.x + 1, pos.y, 0, 0,
					currentFrame.getRegionWidth(),
					currentFrame.getRegionHeight(), -1f
							/ TiledMapRenderer.PIXELS_PER_METER,
					1f / TiledMapRenderer.PIXELS_PER_METER, 0);
		}
		else {
			batch.draw(standing, pos.x, pos.y, 0, 0, standing.getRegionWidth(),
					standing.getRegionHeight(),
					1f / TiledMapRenderer.PIXELS_PER_METER,
					1f / TiledMapRenderer.PIXELS_PER_METER, 0);
		}
	}

	public void update(Input in, float delta) {
		notMoving();

		if (in.isKeyPressed(Input.Keys.D)) {
			body.applyLinearImpulse(new Vector2(1f, 0), body.getPosition());
			moveRight();
		}
		if (in.isKeyPressed(Input.Keys.A)) {
			body.applyLinearImpulse(new Vector2(-1f, 0), body.getPosition());
			moveLeft();
		}
		if (in.isKeyPressed(Input.Keys.W)) {
			body.applyLinearImpulse(new Vector2(0f, -1f), body.getPosition());
		}
		if (in.isKeyPressed(Input.Keys.S)) {
			body.applyLinearImpulse(new Vector2(0f, 1f), body.getPosition());
		}

	}

	private void moveRight() {
		movingRight = true;
		movingLeft = false;
	}

	private void moveLeft() {
		movingLeft = true;
		movingRight = false;
	}

	private void notMoving() {
		movingRight = false;
		movingLeft = false;
	}

	public Vector3 getPosition() {
		return new Vector3(body.getPosition().x, body.getPosition().y, 0);
	}
}
