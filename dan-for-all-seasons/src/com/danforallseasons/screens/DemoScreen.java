package com.danforallseasons.screens;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.danforallseasons.DanForAllSeasons;
import com.danforallseasons.control.PlayerController;
import com.danforallseasons.objects.Player;

public class DemoScreen implements Screen {
	/** Pause screen message **/
	private static final String PAUSE_MSG = "Game Paused\r\nPress O to Resume";

	// Rendering
	private int width = Gdx.graphics.getWidth();
	private int height = Gdx.graphics.getHeight();
	public static float unitScale = 1 / 32f;

	// Map
	private TiledMap tMap;
	private OrthogonalTiledMapRenderer tMapRenderer;
	private int[] BG_LAYERS = { 0, 1 }, FG_LAYERS = { 2 };

	// Font
	private SpriteBatch fontSpriteBatch;
	private BitmapFont font;
	private OrthographicCamera cam;

	// Game
	private DanForAllSeasons game;
	private boolean gamePaused;
	private Player player;

	// Control
	private GestureDetector gDetector;

	public World world = new World(new Vector2(0, -10), true); 
	static final float WORLD_TO_BOX = 0.01f;
	static final float BOX_TO_WORLD = 100f;
	
	Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();	// Box2D Debug frame renderer
	BodyDef bodyDef = new BodyDef();
	Body body; 	
	CircleShape circle; 
	
	FixtureDef fixtureDef;
	Fixture fixture; 
	
	BodyDef groundBodyDef; 
	Body groundBody; 
	PolygonShape groundBox; 
	
	public DemoScreen(DanForAllSeasons dan) {
		game = dan;

		gamePaused = false;

		player = new Player();

		gDetector = new GestureDetector(new PlayerController(player));

		Gdx.input.setInputProcessor(gDetector);

		initializeFont();

		initializeCamera();

		initializeMap();
		
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(20,30);
		body = world.createBody(bodyDef);
		body.setUserData(player);
		groundBodyDef = new BodyDef();  	
		groundBodyDef.position.set(new Vector2(0, 30));  
		groundBody = world.createBody(groundBodyDef);  
		
		groundBox = new PolygonShape();  
		groundBox.setAsBox(cam.viewportWidth, 5.0f);
		groundBody.createFixture(groundBox, 0.0f); 	
		circle = new CircleShape();
		circle.setRadius(3f);
		Vector2 pos = new Vector2(0,30);
		player.setX(-80);
		player.setY(30);
		circle.setPosition(pos);
		fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.density = 0.5f;	 
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.6f;
		fixture = body.createFixture(fixtureDef);
		
	}

	private void initializeMap() {
		tMap = new TmxMapLoader().load("map/demo.tmx");
		tMapRenderer = new OrthogonalTiledMapRenderer(tMap, unitScale);
	}

	private void initializeCamera() {
		cam = new OrthographicCamera();
		cam.setToOrtho(false, width * unitScale, height * unitScale);
		cam.position.set(12.5f, 45, 1);
		cam.update();
	}

	private void initializeFont() {
		fontSpriteBatch = new SpriteBatch();
		font = new BitmapFont();
		font.setColor(Color.MAGENTA);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0, 1, 1, 1);

		if (gamePaused) {
			renderPauseMenu();
		} else {
			renderGame(delta);
		}
		update(delta);
	}

	private void renderGame(float delta) {

		renderMapBackground();

		renderEntities();

		renderMapForeground();

		renderDebugHUD();

	}

	private void renderEntities() {
		SpriteBatch batch = tMapRenderer.getSpriteBatch();
		batch.begin();
		{
			player.draw(batch);
		}
		batch.end();
	}

	private void renderMapBackground() {
		tMapRenderer.setView(cam);
		tMapRenderer.render(BG_LAYERS);
	}

	private void renderMapForeground() {
		tMapRenderer.setView(cam);
		tMapRenderer.render(FG_LAYERS);
	}

	private void renderDebugHUD() {
		fontSpriteBatch.begin();
		{
			font.draw(fontSpriteBatch,
					"FPS: " + Gdx.graphics.getFramesPerSecond(), 20, 20);
			font.draw(fontSpriteBatch, "X Location: " + cam.position.x, 20, 100);
			font.draw(fontSpriteBatch, "Y Location: " + cam.position.y, 20, 80);
			font.draw(fontSpriteBatch, "Zoom: " + cam.zoom, 20, 60);
			font.draw(fontSpriteBatch, "Layer: " + tMap.getTileSets().getTileSet(0).getName(), 20, 120);
			font.draw(fontSpriteBatch, "Ball X: " + body.getPosition().x, 20, 160);
			font.draw(fontSpriteBatch, "Ball Y: " + body.getPosition().y, 20, 140);
			
			font.draw(
					fontSpriteBatch,
					"Press P to Pause",
					Gdx.graphics.getWidth()
							- font.getBounds("Press P to Pause").width,
					Gdx.graphics.getHeight());
			font.draw(fontSpriteBatch, "Press Esc to Quit", 0,
					Gdx.graphics.getHeight());

			font.draw(
					fontSpriteBatch,
					"Press R to Restart",
					Gdx.graphics.getWidth() / 2
							- font.getBounds("Press R to Restart").width / 2,
					Gdx.graphics.getHeight());

		}
		fontSpriteBatch.end();
	}

	private void renderPauseMenu() {
		Gdx.gl.glClearColor(0.5f, 0.9f, 0.9f, 1);

		fontSpriteBatch.begin();
		font.drawMultiLine(fontSpriteBatch, PAUSE_MSG, Gdx.graphics.getWidth()
				/ 2 - font.getBounds(PAUSE_MSG).width / 2 + 50,
				Gdx.graphics.getHeight() / 2 + font.getBounds(PAUSE_MSG).height
						/ 2);
		fontSpriteBatch.end();
	}

	private void update(float delta) {
		Input input = Gdx.input;

		pollInput(input);
		player.update(delta);
		cam.position.set(player.getX(), player.getY(), 1);
		cam.update();
		
		Array<Body> bodies = new Array<Body>();

	    world.getBodies(bodies);
		Iterator<Body> bi = bodies.iterator();//world.getBodies(bodies);
		Body bod;
		while (bi.hasNext()) {
			bod = bi.next();
			if (bod.getUserData() == player) {
				player.setX(bod.getPosition().x);
				player.setY(bod.getPosition().y);
			}
		
		}	
		
		world.step(1/60f, 6, 2);
		debugRenderer.render(world, cam.combined);
	}

	private void pollInput(Input input) {
		if (input.isKeyPressed(Keys.P)) {
			if (!gamePaused) {
				Gdx.app.log(DanForAllSeasons.LOG, "Pausing Game");
				pauseGame();
			}
		}
		if (input.isKeyPressed(Keys.O)) {
			if (gamePaused) {
				Gdx.app.log(DanForAllSeasons.LOG, "Resuming Game");
				resumeGame();
			}
		}
		if (input.isKeyPressed(Keys.ESCAPE)) {
			Gdx.app.log(DanForAllSeasons.LOG, "Quitting Game");
			Gdx.app.exit();
		}

		if (input.isKeyPressed(Keys.R)) {

			if (!gamePaused) {
				Gdx.app.log(DanForAllSeasons.LOG, "Restting Demo");
				game.setScreen(new DemoScreen(game));
			}
		}
		if (input.isKeyPressed(Keys.W) || input.isKeyPressed(Keys.UP)) {
			if (player.onGround()) player.jump();
		}
		if (input.isKeyPressed(Keys.S) || input.isKeyPressed(Keys.DOWN)) {
		}
		if (input.isKeyPressed(Keys.D) || input.isKeyPressed(Keys.RIGHT)) {
			player.addSpeed(1, 0);
			if (player.onGround()) {
				player.walk();
			}
		}
		if (input.isKeyPressed(Keys.A) || input.isKeyPressed(Keys.LEFT)) {
			player.addSpeed(-1, 0);
			if (player.onGround()) {
				player.walk();
			}
		}

		if (!input.isTouched()) player.stop();
	}

	private void pauseGame() {
		gamePaused = true;
	}

	private void resumeGame() {
		gamePaused = false;
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {
		pauseGame();
	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}
}
