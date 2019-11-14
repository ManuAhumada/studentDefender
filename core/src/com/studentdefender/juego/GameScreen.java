package com.studentdefender.juego;

import static com.studentdefender.utils.Constants.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
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
import com.studentdefender.utils.Global;
import com.studentdefender.utils.TiledObjectUtil;
import com.studentdefender.utils.WorldContactListener;

import box2dLight.RayHandler;

public class GameScreen implements Screen {
	final StudentDefender game;

	private final float SCALE = Constants.DEBUG ? 1f : 2f;

	private Box2DDebugRenderer b2dr;
	public static World world;
	public static RayCastCallbackImp rayCastCallback;
	public static RayHandler rayHandler;

	private OrthogonalTiledMapRenderer tmr;
	public static TiledMap map;
	public static IndexedGraphImp indexedGraphImp;
	public static IndexedAStarPathFinder<Node> indexedAStarPathFinder;

	public static Array<Jugador> jugadores;
	private Array<Node> spawnsJugadores;
	private Array<Node> spawnsEnemigos;

	public static Array<Bala> balasActivas;
	public static Array<Enemigo> enemigosActivos;

	public static Pool<Bala> balaPool;
	public static Pool<Enemigo> enemigoPool;

	private int cantEnemigos;
	private int ronda;

	public GameScreen(final StudentDefender game) {
		this.game = game;

		Global.camara.setToOrtho(false, Gdx.graphics.getWidth() / SCALE, Gdx.graphics.getHeight() / SCALE);

		world = new World(new Vector2(0, 0), false);
		world.setContactListener(new WorldContactListener());
		b2dr = new Box2DDebugRenderer();
		rayHandler = new RayHandler(world);
		rayHandler.setAmbientLight(Constants.DEBUG ? 1 : 0);
		rayCastCallback = new RayCastCallbackImp();

		crearMapa();

		jugadores = new Array<Jugador>();
		jugadores.add(new Jugador((int) (spawnsJugadores.get(0).getPosition().x * PPM),
				(int) (spawnsJugadores.get(0).getPosition().y * PPM), 25f));
		jugadores.add(new JugadorTest((int) (spawnsJugadores.get(1).getPosition().x * PPM),
				(int) (spawnsJugadores.get(1).getPosition().y * PPM), 25f));

		balasActivas = new Array<Bala>();
		balaPool = Pools.get(Bala.class);
		enemigosActivos = new Array<Enemigo>();
		enemigoPool = Pools.get(Enemigo.class);

		cantEnemigos = 3;
		ronda = 0;
	}

	private void crearMapa() {
		map = new TmxMapLoader().load("mapas\\Mapa-PlantaBaja.tmx");
		tmr = new OrthogonalTiledMapRenderer(map, Global.batch);
		TiledObjectUtil.parseTiledObjectLayer(world, map.getLayers().get("collision-layer").getObjects(), false);
		TiledObjectUtil.parseTiledObjectLayer(world, map.getLayers().get("spawns-puertas").getObjects(), true);
		indexedGraphImp = new IndexedGraphImp(
				TiledObjectUtil.crearNodos(world, map.getLayers().get("nodos").getObjects()));
		indexedGraphImp.setConnections(
				TiledObjectUtil.crearConexiones(world, map.getLayers().get("connections").getObjects()));
		indexedAStarPathFinder = new IndexedAStarPathFinder<>(indexedGraphImp);
		spawnsJugadores = TiledObjectUtil.crearNodos(world, map.getLayers().get("spawns-jugadores").getObjects());
		spawnsEnemigos = TiledObjectUtil.crearNodos(world, map.getLayers().get("spawns-enemigos").getObjects());
	}

	public void render(float delta) {

		update(delta);
		
		if (cheaquearFin()) {  
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			Global.batch.setProjectionMatrix(Global.camara.combined);
			Global.shapeRenderer.setProjectionMatrix(Global.camara.combined);
			rayHandler.setCombinedMatrix(Global.camara.combined.cpy().scl(PPM));

			// tmr.render();
			b2dr.render(world, Global.camara.combined.cpy().scl(PPM));
			dibujar();
			rayHandler.render();
			dibujarInterfaz();
		} else {
			dispose();
			game.setScreen(new MainMenuScreen(game));
		}
		
	}

	private void dibujarInterfaz() {
		int posCuadrox = 0;
		for (Jugador jugador : jugadores) {
			jugador.dibujarInterfaz(posCuadrox);
			posCuadrox += 200;		
		}
		Global.batch.begin();
		Global  .font.draw(Global  .batch, "Round " + ronda, Global.camara.position.x - Global.camara.viewportWidth/2 + 10, Global.camara.position.y - Global.camara.viewportHeight/2 + 20);
		Global  .batch.end();
	}

	private void dibujar() {
		for (Enemigo enemigo : enemigosActivos) {
			enemigo.dibujar();
		}
		for (Jugador jugador : jugadores) {
			if (!jugador.isMuerto()) {
				jugador.dibujar();
			}
		}
		if (Constants.DEBUG) {
			for (Node node : indexedGraphImp.getNodes()) {
				node.dibujar(Global  .batch, Global  .font);
			}
		}
	}

	private void update(float delta) {
		world.step(1 / 60f, 6, 2);
		rayHandler.update();
		if(cheaquearFin()) {
			actualizarBalas();
			spawnearEnemigos();
			actualizarEnemigos(delta);
			actualizarJugadores(delta);
			cameraUpdate(delta);
			// tmr.setView(camara);
		} 	
	}

	private boolean cheaquearFin() {
		for (Jugador jugador : jugadores) {
			if(!(jugador.isMuerto() || jugador.isAbatido()))
				return true;
		}
		return false;
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
			Gdx.app.log("Sistema", "Round " + ++ronda);
			for (int i = 0; i < cantEnemigos; i++) {
				Vector2 posicion = spawnsEnemigos.get(MathUtils.random(spawnsEnemigos.size - 1)).getPosition();
				enemigoPool.obtain().init((int) (posicion.x * PPM), (int) (posicion.y * PPM), 20f);
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
		Vector3 position = Global.camara.position;
		position.x = Global.camara.position.x + (((jugadores.first().getPosition().x * PPM) - Global.camara.position.x) * .05f);
		position.y = Global.camara.position.y + (((jugadores.first().getPosition().y * PPM) - Global.camara.position.y) * .05f);
		Global.camara.position.set(position);

		Global.camara.update();
	}

	public void resize(int width, int height) {
		Global.camara.setToOrtho(false, Gdx.graphics.getWidth() / SCALE, Gdx.graphics.getHeight() / SCALE);
		Global.camara.update();
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
		world = null;
		rayHandler = null;
		map = null;
		indexedGraphImp = null;
		indexedAStarPathFinder = null;
		jugadores=null;
		balasActivas=null;
		enemigosActivos=null;
		balaPool=null;
		enemigoPool=null;
	}
}
