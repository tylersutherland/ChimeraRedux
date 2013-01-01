package com.danforallseasons.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.tiled.TiledLoader;
import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.danforallseasons.DanForAllSeasons;
import com.danforallseasons.PhysicsDan;
import com.danforallseasons.physics.PhysicsHelper;
import com.danforallseasons.tiled.TiledMapAtlas;
import com.danforallseasons.tiled.TiledMapRenderer;

public class DemoScreen implements Screen {
	public static final int TILE_WIDTH = 64;
	public static final int TILE_HEIGHT = 64;
	private static final int PIXELS_PER_METER = TiledMapRenderer.PIXELS_PER_METER;

	/* Map */
	private TiledMap map;
	private TiledMapAtlas atlas;
	private TiledMapRenderer mapRenderer;

	/* Rendering */
	private SpriteBatch spriteBatch;

	private SpriteBatch fontSpriteBatch;
	private BitmapFont font;
	private OrthographicCamera cam;

	/* Physics */
	private World world;
	private Array<Body> groundBodies;
	private Box2DDebugRenderer physicsDebugRenderer;

	private PhysicsDan pd;

	public DemoScreen(DanForAllSeasons dan) {
		spriteBatch = new SpriteBatch();
		fontSpriteBatch = new SpriteBatch();
		font = new BitmapFont();
		font.setColor(Color.MAGENTA);
		map = TiledLoader.createMap(Gdx.files.internal("map/demo.tmx"));
		atlas = new TiledMapAtlas(map, Gdx.files.internal("map/demo.pack"));
		map.setTileProperty(31, "shape", "uphill");
		map.setTileProperty(24, "shape", "downhill");
		setupPhysics();

		mapRenderer = new TiledMapRenderer(map, atlas);
		physicsDebugRenderer = new Box2DDebugRenderer();
		cam = new OrthographicCamera(
				Gdx.graphics.getWidth() / PIXELS_PER_METER,
				Gdx.graphics.getHeight() / PIXELS_PER_METER);

		cam.setToOrtho(true, cam.viewportWidth, cam.viewportHeight);
		cam.position.set(5, 6, 0);

	}

	private void setupPhysics() {
		world = new World(new Vector2(0, 10), true);
		groundBodies = new Array<Body>();
		Array<Vector2[]> groundVertices = PhysicsHelper.getCollisionShapes(map);
		for (int i = 0; i < groundVertices.size; i++) {

			PolygonShape groundPoly = new PolygonShape();
			groundPoly.set(groundVertices.get(i));
			BodyDef groundBodyDef = new BodyDef();
			groundBodyDef.type = BodyType.StaticBody;
			Body groundBody = world.createBody(groundBodyDef);

			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = groundPoly;
			fixtureDef.filter.groupIndex = 0;
			fixtureDef.friction = 0.5f;
			groundBody.createFixture(fixtureDef);
			groundPoly.dispose();

			groundBodies.add(groundBody);
		}
		pd = new PhysicsDan(10, 10, world);

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0, 1, 1, 1);

		mapRenderer.render(cam);
		physicsDebugRenderer.render(world, cam.combined);

		spriteBatch.setProjectionMatrix(cam.combined);
		spriteBatch.begin();
		{
			pd.draw(spriteBatch);
		}
		spriteBatch.end();

		fontSpriteBatch.begin();
		{

			font.draw(fontSpriteBatch,
					"FPS: " + Gdx.graphics.getFramesPerSecond(), 20, 20);
			font.draw(fontSpriteBatch,
					"Initial Col, Last Col " + mapRenderer.getInitialCol()
							+ "," + mapRenderer.getLastCol(), 20, 60);
			font.draw(fontSpriteBatch,
					"Initial Row, Last Row " + mapRenderer.getInitialRow()
							+ "," + mapRenderer.getLastRow(), 20, 40);
			font.draw(fontSpriteBatch, "Location: " + cam.position.x + ","
					+ cam.position.y, 20, 80);

		}
		fontSpriteBatch.end();

		update(delta);
	}

	private void update(float delta) {
		world.step(delta, 4, 4);
		Input input = Gdx.input;
		pd.update(input, delta);
		cam.position.set(pd.getPosition());
		cam.update();

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
