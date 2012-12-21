package com.danforallseasons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.tiled.TiledLoader;
import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.danforallseasons.tiled.TiledMapAtlas;
import com.danforallseasons.tiled.TiledMapRenderer;

public class DemoScreen implements Screen {
	public static final int TILE_WIDTH = 64;
	public static final int TILE_HEIGHT = 64;

	private TiledMap map;
	private TiledMapAtlas atlas;
	private TiledMapRenderer renderer;
	private SpriteBatch spriteBatch;
	private BitmapFont font;
	private OrthographicCamera cam;
	// Physics Stuff
	private ShapeRenderer shapeRenderer;
	private Box2DDebugRenderer debugRenderer;
	private World world;
	private Body dan;
	private Body groundBody;
	private TextureRegion danFace;

	public DemoScreen() {
		spriteBatch = new SpriteBatch();
		font = new BitmapFont();
		font.setColor(Color.MAGENTA);
		map = TiledLoader.createMap(Gdx.files.internal("map/demo.tmx"));
		atlas = new TiledMapAtlas(map, Gdx.files.internal("map/demo.pack"));

		/*
		 * prints out a text representation of the map for test purposes
		 */
		/*
		 * for (int i = 0; i < map.width; i++) { for (int j = 0; j < map.height;
		 * j++) { System.out.print(map.layers.get(0).tiles[i][j] + "\t"); }
		 * System.out.println(); }
		 */

		danFace = new TextureRegion(new Texture(
				Gdx.files.internal("sprites/danface.png")));

		shapeRenderer = new ShapeRenderer();
		debugRenderer = new Box2DDebugRenderer();

		renderer = new TiledMapRenderer(map, atlas);

		cam = new OrthographicCamera(1024f, 768f);
		cam.position.set(512, -256, 0);

		createPhysicsWorld();

	}

	private void createPhysicsWorld() {
		world = new World(new Vector2(0, -64), true);

		PolygonShape groundPoly = new PolygonShape();
		// groundPoly.setAsBox(1000, 1, new Vector2(512, -512), 0);
		groundPoly.set(new Vector2[] { new Vector2(0, -512),
				new Vector2(708, -512), new Vector2(708 + 64 * 4, -256),
				new Vector2(708 + 64 * 4, 100) });
		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.type = BodyType.StaticBody;
		groundBody = world.createBody(groundBodyDef);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = groundPoly;
		fixtureDef.filter.groupIndex = 0;
		groundBody.createFixture(fixtureDef);
		groundPoly.dispose();

		PolygonShape boxPoly = new PolygonShape();
		boxPoly.setAsBox(100, 100);

		BodyDef boxBodyDef = new BodyDef();
		boxBodyDef.type = BodyType.DynamicBody;
		boxBodyDef.position.x = 512 + (float) (Math.random() * 48);
		boxBodyDef.position.y = -256 + (float) (Math.random() * 100);
		Body boxBody = world.createBody(boxBodyDef);

		boxBody.createFixture(boxPoly, 1);

		dan = boxBody;

		boxPoly.dispose();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0, 1, 1, 1);
		world.step(Gdx.graphics.getDeltaTime(), 8, 3);
		renderer.render(cam);
		renderBox(groundBody, 1000, 1);

		spriteBatch.begin();
		{

			font.draw(spriteBatch, "FPS: " + Gdx.graphics.getFramesPerSecond(),
					20, 20);
			font.draw(spriteBatch,
					"Initial Col, Last Col " + renderer.getInitialCol() + ","
							+ renderer.getLastCol(), 20, 60);
			font.draw(spriteBatch,
					"Initial Row, Last Row " + renderer.getInitialRow() + ","
							+ renderer.getLastRow(), 20, 40);
			font.draw(spriteBatch, "Location: " + cam.position.x + ","
					+ cam.position.y, 20, 80);

		}
		spriteBatch.end();

		debugRenderer.render(world, cam.combined);

		shapeRenderer.setProjectionMatrix(cam.combined);
		shapeRenderer.begin(ShapeType.Point);
		shapeRenderer.setColor(0, 1, 0, 1);
		for (int i = 0; i < world.getContactCount(); i++) {
			Contact contact = world.getContactList().get(i);
			if (contact.isTouching()) {
				WorldManifold manifold = contact.getWorldManifold();
				int numContactPoints = manifold.getNumberOfContactPoints();
				for (int j = 0; j < numContactPoints; j++) {
					Vector2 point = manifold.getPoints()[j];
					shapeRenderer.point(point.x, point.y, 0);
				}
			}
		}
		shapeRenderer.end();

		update(delta);
	}

	Matrix4 transform = new Matrix4();

	private void renderBox(Body body, float halfWidth, float halfHeight) {
		// get the bodies center and angle in world coordinates
		Vector2 pos = body.getWorldCenter();
		float angle = body.getAngle();

		// set the translation and rotation matrix
		transform.setToTranslation(pos.x, pos.y, 0);
		transform.rotate(0, 0, 1, (float) Math.toDegrees(angle));

		// render the box
		shapeRenderer.begin(ShapeType.Rectangle);
		shapeRenderer.setTransformMatrix(transform);
		shapeRenderer.setColor(1, 1, 1, 1);
		shapeRenderer.rect(-halfWidth, -halfHeight, halfWidth * 2,
				halfHeight * 2);
		shapeRenderer.end();
	}

	private void update(float delta) {
		cam.update();
		Input input = Gdx.input;
		if (input.isTouched()) {
			int x = input.getX();
			int y = input.getY();

			dan.applyForceToCenter(
					(x - dan.getTransform().getPosition().x) * 10000,
					(Gdx.graphics.getHeight() - (y - dan.getTransform()
							.getPosition().y)) * 10000);
		}
		if (input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
			if (input.isKeyPressed(Input.Keys.D)) cam.position.x += 100;
			if (input.isKeyPressed(Input.Keys.A)) cam.position.x -= 100;
			if (input.isKeyPressed(Input.Keys.W)) cam.position.y -= 100;
			if (input.isKeyPressed(Input.Keys.S)) cam.position.y += 100;
		}

		if (input.isKeyPressed(Input.Keys.D)) cam.position.x++;
		if (input.isKeyPressed(Input.Keys.A)) cam.position.x--;
		if (input.isKeyPressed(Input.Keys.W)) cam.position.y--;
		if (input.isKeyPressed(Input.Keys.S)) cam.position.y++;

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
