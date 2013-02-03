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
	private Vector2[] danVertices;
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
		createShape();
		danPoly.set(danVertices);
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		body = world.createBody(bodyDef);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = danPoly;
		fixtureDef.filter.groupIndex = 0;
		fixtureDef.friction = 0.5f;
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

		walkAnimation = new Animation(0.1f, walkFrames);
		
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
			if (!(body.getLinearVelocity().x > 10.0f)) body.applyLinearImpulse(new Vector2(1f, 0), body.getPosition());
			moveRight();
		}
		if (in.isKeyPressed(Input.Keys.A)) {
			if (!(body.getLinearVelocity().x < -10.0f)) body.applyLinearImpulse(new Vector2(-1f, 0), body.getPosition());
			moveLeft();
		}
		if (in.isKeyPressed(Input.Keys.W)) {
			if (!(body.getLinearVelocity().y < -10.0f)) body.applyLinearImpulse(new Vector2(0f, -1f), body.getPosition());
		}
		if (in.isKeyPressed(Input.Keys.S)) {
			if (!(body.getLinearVelocity().y > 10.0f)) body.applyLinearImpulse(new Vector2(0f, 1f), body.getPosition());
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
	
	public void createShape() {
		//Setting up the vector array
		danVertices = new Vector2[6];
		danVertices[0] = new Vector2();
		danVertices[1] = new Vector2();
		danVertices[2] = new Vector2();
		danVertices[3] = new Vector2();
		danVertices[4] = new Vector2();
		danVertices[5] = new Vector2();
	
		//Setting all the vertices
		danVertices[0].set(0.45f,  -0.9f);
		danVertices[1].set(0.45f,  0.65f);
		danVertices[2].set(0.1f,  1.0f);
		danVertices[3].set(-0.1f, 1.0f);
		danVertices[4].set(-0.45f,  0.65f);
		danVertices[5].set(-0.45f,  -0.9f);
	}
}
