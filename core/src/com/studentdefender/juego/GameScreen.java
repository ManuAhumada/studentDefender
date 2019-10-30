package com.studentdefender.juego;

import static com.studentdefender.utils.Constants.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.studentdefender.armas.Bala;
import com.studentdefender.path_finder.IndexedGraphImp;
import com.studentdefender.path_finder.Node;
import com.studentdefender.path_finder.RayCastCallbackImp;
import com.studentdefender.personajes.Enemigo;
import com.studentdefender.personajes.Jugador;
import com.studentdefender.personajes.JugadorTest;
import com.studentdefender.utils.Constants;
import com.studentdefender.utils.TiledObjectUtil;
import com.studentdefender.utils.WorldContactListener;

import box2dLight.RayHandler;

public class GameScreen implements Screen {
	final StudentDefender game;

	private final float SCALE = 1f;

	public static ShapeRenderer shapeRenderer;

	private Box2DDebugRenderer b2dr;
	public static World world;
	public static RayCastCallbackImp rayCastCallback;
	public static RayHandler rayHandler;

	private OrthogonalTiledMapRenderer tmr;
	public static TiledMap map;
	public static IndexedGraphImp indexedGraphImp;
	public static IndexedAStarPathFinder<Node> indexedAStarPathFinder;

	public static OrthographicCamera camara;

	public static Array<Jugador> jugadores = new Array<Jugador>();

	public static Array<Bala> balasActivas = new Array<Bala>();
	public static Array<Enemigo> enemigosActivos = new Array<Enemigo>();

	public static final Pool<Bala> balaPool = Pools.get(Bala.class);
	public static final Pool<Enemigo> enemigoPool = Pools.get(Enemigo.class);

	private int cantEnemigos;

	public GameScreen(final StudentDefender game) {
		this.game = game;

		camara = new OrthographicCamera();
		camara.setToOrtho(false, Gdx.graphics.getWidth() / SCALE, Gdx.graphics.getHeight() / SCALE);

		world = new World(new Vector2(0, 0), false);
		world.setContactListener(new WorldContactListener());
		b2dr = new Box2DDebugRenderer();
		rayHandler = new RayHandler(world);
		rayHandler.setAmbientLight(0);
		rayCastCallback = new RayCastCallbackImp();

		shapeRenderer = new ShapeRenderer();

		map = new TmxMapLoader().load("mapas\\test_map.tmx");
		tmr = new OrthogonalTiledMapRenderer(map, game.batch);
		TiledObjectUtil.parseTiledObjectLayer(world, map.getLayers().get("collision-layer").getObjects());
		indexedGraphImp = new IndexedGraphImp(
				TiledObjectUtil.crearNodos(world, map.getLayers().get("nodos").getObjects()));
		indexedGraphImp.setConnections(
				TiledObjectUtil.crearConexiones(world, map.getLayers().get("connections").getObjects()));
		indexedAStarPathFinder = new IndexedAStarPathFinder<>(indexedGraphImp);

		jugadores.add(new Jugador((int) indexedGraphImp.getNodes().get(0).getPosicion().x,
				(int) indexedGraphImp.getNodes().get(0).getPosicion().y, 7.5f));
		jugadores.add(new JugadorTest((int) indexedGraphImp.getNodes().get(1).getPosicion().x,
				(int) indexedGraphImp.getNodes().get(1).getPosicion().y, 7.5f));

		cantEnemigos = 3;
	}

	public void render(float delta) {

		update(delta);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		game.batch.setProjectionMatrix(camara.combined);
		shapeRenderer.setProjectionMatrix(camara.combined);
		rayHandler.setCombinedMatrix(camara.combined.cpy().scl(PPM));

		// tmr.render();
		b2dr.render(world, camara.combined.cpy().scl(PPM));
		dibujar();
		rayHandler.render();

		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			this.pause();
		}
	}

	private void dibujar() {
		for (Enemigo enemigo : enemigosActivos) {
			enemigo.dibujar(game.batch, game.font);
		}
		for (Jugador jugador : jugadores) {
			if (!jugador.isMuerto()) {
				jugador.dibujar(game.batch, game.font);
			}
		}
		if (Constants.DEBUG) {
			for (Node node : indexedGraphImp.getNodes()) {
				node.dibujar(game.batch, game.font);
			}
		}
	}

	private void update(float delta) {
		world.step(1 / 60f, 6, 2);
		rayHandler.update();

		actualizarBalas();
		spawnearEnemigos();
		actualizarEnemigos(delta);
		actualizarJugadores(delta);

		cameraUpdate(delta);
		// tmr.setView(camara);
	}

	private void actualizarJugadores(float delta) {
		for (Jugador jugador : jugadores) {
			if (!jugador.isMuerto()) {
				jugador.actualizar(delta);
			} else {
				jugador.getBody().setActive(false);
			}
		}
	}

	private void actualizarEnemigos(float delta) {
		for (Enemigo enemigo : enemigosActivos) {
			if (enemigo.isActivo()) {
				enemigo.actualizar(delta);
			} else {
				enemigoPool.free(enemigo);
			}
		}
	}

	private void spawnearEnemigos() {
		if (enemigosActivos.isEmpty()) {
			spawnearJugadores();
			Gdx.app.log("Sistema", "Round " + cantEnemigos);
			for (int i = 0; i < cantEnemigos; i++) {
				Vector2 posicion = indexedGraphImp.getNodes().get(5).getPosicion();
				enemigoPool.obtain().init((int) posicion.x, (int) posicion.y, 7.5f);
			}
			cantEnemigos *= 1.5;
		}
	}

	private void spawnearJugadores() {
		for (Jugador jugador : jugadores) {
			if (jugador.isMuerto()) {
				jugador.reiniciar();
			}
		}
	}

	private void actualizarBalas() {
		for (Bala bala : balasActivas) {
			if (bala.isActivo()) {

			} else {
				balaPool.free(bala);
			}
		}
	}

	private void cameraUpdate(float delta) {
		Vector3 position = camara.position;
		position.x = camara.position.x + (((jugadores.first().getPosition().x * PPM) - camara.position.x) * .05f);
		position.y = camara.position.y + (((jugadores.first().getPosition().y * PPM) - camara.position.y) * .05f);
		camara.position.set(position);

		camara.update();
	}

	public void resize(int width, int height) {
		camara.setToOrtho(false, Gdx.graphics.getWidth() / SCALE, Gdx.graphics.getHeight() / SCALE);
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
		map.dispose();
		tmr.dispose();
		rayHandler.dispose();
	}

}
