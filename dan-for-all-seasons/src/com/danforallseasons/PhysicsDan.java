package com.danforallseasons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
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
	private TextureRegion tex;

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
		body.createFixture(fixtureDef);
		danPoly.dispose();

		tex = new TextureRegion(new Texture(Gdx.files.internal("Dan2.png")));
		tex.flip(false, true);

	}

	public void draw(SpriteBatch batch) {

		Vector2 pos = body.getPosition().add(-0.5f, -1.0f);
		batch.draw(tex, pos.x, pos.y, 0, 0, tex.getRegionWidth(),
				tex.getRegionHeight(), 1f / TiledMapRenderer.PIXELS_PER_METER,
				1f / TiledMapRenderer.PIXELS_PER_METER, 0);
	}

	public void update(Input in, float delta) {
		if (in.isKeyPressed(Input.Keys.D)) {
			body.applyLinearImpulse(new Vector2(1f, 0), body.getPosition());
		}
		if (in.isKeyPressed(Input.Keys.A)) {
			body.applyLinearImpulse(new Vector2(-1f, 0), body.getPosition());
		}
		if (in.isKeyPressed(Input.Keys.W)) {
			body.applyLinearImpulse(new Vector2(0f, -1f), body.getPosition());
		}
		if (in.isKeyPressed(Input.Keys.S)) {
			body.applyLinearImpulse(new Vector2(0f, 1f), body.getPosition());
		}
	}

	public Vector3 getPosition() {
		return new Vector3(body.getPosition().x, body.getPosition().y, 0);
	}
}
