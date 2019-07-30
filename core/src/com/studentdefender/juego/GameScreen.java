package com.studentdefender.juego;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.studentdefender.personajes.Jugador;
import com.studentdefender.personajes.Personaje;
import com.studentdefender.utils.TiledObjectUtil;
import com.studentdefender.utils.WorldContactListener;
import static com.studentdefender.utils.Constants.PPM;

public class GameScreen implements Screen {
	final StudentDefender game;
	
	//private final float SCALE = 2.0f; 
	
	private Box2DDebugRenderer b2dr;
	public static World world;

	//private OrthogonalTiledMapRenderer tmr;
	private TiledMap map;
	
	OrthographicCamera camara;
	
	Personaje jugador;
	
	public static Array<Body> bodysToEliminate = new Array<Body>();

	public GameScreen(final StudentDefender game) {
		this.game = game;

		camara = new OrthographicCamera();
		camara.setToOrtho(false, Gdx.graphics.getWidth()/* / SCALE*/, Gdx.graphics.getHeight() /*/ SCALE*/);

		world = new World(new Vector2(0, 0), false);
		world.setContactListener(new WorldContactListener());
		b2dr = new Box2DDebugRenderer();
		
		map = new TmxMapLoader().load("mapas\\test_map.tmx");
		//tmr = new OrthogonalTiledMapRenderer(map);
		TiledObjectUtil.parseTiledObjectLayer(world, map.getLayers().get("collision-layer").getObjects());
	
		jugador = new Jugador(200, 200, 10);
	}

	public void render(float delta) {

		update(delta);
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//tmr.render();
		b2dr.render(world, camara.combined/*.scl(PPM)*/);
		
		if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)) Gdx.app.exit();
	}

	private void update(float delta) {
		world.step(1/60f, 6, 2);
		
		for (Body body : bodysToEliminate) {
			world.destroyBody(body);
			bodysToEliminate.removeValue(body, true);
		}
		jugador.actualizar(camara, delta);
		
		cameraUpdate(delta);
		//tmr.setView(camara);
	}

	private void cameraUpdate(float delta) {
		Vector3 position = camara.position;
		position.x = camara.position.x + (jugador.getPosicion().x - camara.position.x) * .1f; //* PPM;
		position.y = camara.position.y + (jugador.getPosicion().y - camara.position.y) * .1f; //* PPM;
		camara.position.set(position);
		
		camara.update();
	}

	public void resize(int width, int height) {
		camara.setToOrtho(false, Gdx.graphics.getWidth() /*/ SCALE*/, Gdx.graphics.getHeight() /*/ SCALE*/);
		camara.update();
	}

	public void show() {

	}

	public void hide() {

	}

	public void pause() {

	}

	public void resume() {

	}

	public void dispose() {
		world.dispose();
		b2dr.dispose();
	}

}
