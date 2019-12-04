package com.studentdefender.juego;

import static com.studentdefender.utils.Constants.PPM;

import java.util.ArrayList;

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
import com.studentdefender.mejoras.Mejora;
import com.studentdefender.objetos_red.CuerpoRed;
import com.studentdefender.objetos_red.JuegoRed;
import com.studentdefender.objetos_red.JugadorRed;
import com.studentdefender.objetos_red.MejoraRed;
import com.studentdefender.objetos_red.PersonajeRed;
import com.studentdefender.path_finder.IndexedGraphImp;
import com.studentdefender.path_finder.Node;
import com.studentdefender.path_finder.RayCastCallbackImp;
import com.studentdefender.personajes.Enemigo;
import com.studentdefender.personajes.Jugador;
import com.studentdefender.personajes.Profesores;
import com.studentdefender.utils.Constants;
import com.studentdefender.utils.Global;
import com.studentdefender.utils.TiledObjectUtil;
import com.studentdefender.utils.WorldContactListener;

import box2dLight.RayHandler;

public class GameScreen implements Screen {
	final StudentDefender game;

	private final float SCALE = Constants.DEBUG ? 1f : 1f;

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

	private int cantEnemigosRonda;
	private int cantEnemigosRestantes;
	private int cantEnemigosMaximo;
	private int ronda;

	public GameScreen(final StudentDefender game, Profesores[] profesoresSeleccionados) {
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
		for (int i = 0; i < profesoresSeleccionados.length; i++) {
			jugadores.add(new Jugador((int) (spawnsJugadores.get(i).getPosition().x * PPM),
					(int) (spawnsJugadores.get(i).getPosition().y * PPM), 25f, profesoresSeleccionados[i]));
		}

		balasActivas = new Array<Bala>();
		balaPool = Pools.get(Bala.class);
		enemigosActivos = new Array<Enemigo>();
		enemigoPool = Pools.get(Enemigo.class);

		cantEnemigosRonda = 3;
		cantEnemigosRestantes = 3;
		cantEnemigosMaximo = 15;

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

			b2dr.render(world, Global.camara.combined.cpy().scl(PPM));
			dibujar();
			rayHandler.render();
			dibujarInterfaz();
		} else {
			Global.servidor.enviarMensaje("fin");
			Global.servidor.reiniciar();
			game.setScreen(new PantallaSeleccion(game));
			dispose();
		}

	}

	private void dibujarInterfaz() {
		int posCuadrox = 0;
		for (Jugador jugador : jugadores) {
			jugador.dibujarInterfaz(posCuadrox);
			posCuadrox += 200;
		}
		Global.batch.begin();
		Global.font.draw(Global.batch, "Round " + (ronda + 1),
				Global.camara.position.x - Global.camara.viewportWidth / 2 + 10,
				Global.camara.position.y - Global.camara.viewportHeight / 2 + 20);
		Global.batch.end();
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
				node.dibujar(Global.batch, Global.font);
			}
		}
	}

	private void update(float delta) {
		world.step(1 / 60f, 6, 2);
		rayHandler.update();
		if (cheaquearFin()) {
			actualizarBalas();
			spawnearEnemigos();
			actualizarEnemigos(delta);
			actualizarJugadores(delta);
			cameraUpdate(delta);
			enviarInformacion();
		}
	}

	private void enviarInformacion() {
		JuegoRed informacion = new JuegoRed();
		informacion.jugadores = new JugadorRed[jugadores.size];
		for (int i = 0; i < jugadores.size; i++) {
			Jugador jugador = jugadores.get(i);
			informacion.jugadores[i] = new JugadorRed();
			informacion.jugadores[i].x = jugador.getPosition().x * Constants.PPM;
			informacion.jugadores[i].y = jugador.getPosition().y * Constants.PPM;
			informacion.jugadores[i].orientacion = jugador.getOrientation() * MathUtils.radiansToDegrees - 90;
			informacion.jugadores[i].radio = jugador.getBoundingRadius() * Constants.PPM;
			informacion.jugadores[i].vida = jugador.getVida();
			informacion.jugadores[i].vidaActual = jugador.getVidaActual();
			informacion.jugadores[i].personaje = jugador.getProfesor();
			informacion.jugadores[i].municionTotal = jugador.getArma().getMunicionTotal();
			informacion.jugadores[i].municionEnArma = jugador.getArma().getMunicionEnArma();
			informacion.jugadores[i].tamañoCartucho = jugador.getArma().getTamañoCartucho();
			informacion.jugadores[i].dinero = jugador.getDinero();
			informacion.jugadores[i].abatido = jugador.isAbatido();
			informacion.jugadores[i].muerto = jugador.isMuerto();
			informacion.jugadores[i].momentoAbatido = jugador.getMomentoAbatido();
			informacion.jugadores[i].tiempoReviviendo = jugador.getTiempoReviviendo();
			informacion.jugadores[i].tiempoRevivir = jugador.getTiempoRevivir();
			informacion.jugadores[i].maxTiempoAbatido = jugador.getMaxTiempoAbatido();
			informacion.jugadores[i].reviviendo = jugador.getJugadorReviviendo() != null;
			informacion.jugadores[i].mejoras = new MejoraRed[jugador.getMejoras().length];
			for (int j = 0; j < jugador.getMejoras().length; j++) {
				informacion.jugadores[i].mejoras[j] = new MejoraRed();
				Mejora mejora = jugador.getMejoras()[j];
				informacion.jugadores[i].mejoras[j].nombre = mejora.getNombre();
				informacion.jugadores[i].mejoras[j].nivelMaximo = mejora.getNivelMaximo();
				informacion.jugadores[i].mejoras[j].nivelActual = mejora.getNivelActual();
				informacion.jugadores[i].mejoras[j].precio = mejora.getPrecio();
			}
		}
		informacion.balas = new CuerpoRed[balasActivas.size];
		for (int i = 0; i < balasActivas.size; i++) {
			Bala bala = balasActivas.get(i);
			informacion.balas[i] = new CuerpoRed();
			informacion.balas[i].x = bala.getPosition().x * Constants.PPM;
			informacion.balas[i].y = bala.getPosition().y * Constants.PPM;
			informacion.balas[i].orientacion = bala.getOrientation() * MathUtils.radiansToDegrees - 90;
			informacion.balas[i].radio = bala.getRadio() * Constants.PPM;
		}
		informacion.enemigos = new PersonajeRed[enemigosActivos.size];
		for (int i = 0; i < enemigosActivos.size; i++) {
			Enemigo enemigo = enemigosActivos.get(i);
			informacion.enemigos[i] = new PersonajeRed();
			informacion.enemigos[i].x = enemigo.getPosition().x * Constants.PPM;
			informacion.enemigos[i].y = enemigo.getPosition().y * Constants.PPM;
			informacion.enemigos[i].orientacion = enemigo.getOrientation() * MathUtils.radiansToDegrees - 90;
			informacion.enemigos[i].vida = enemigo.getVida();
			informacion.enemigos[i].vidaActual = enemigo.getVidaActual();
			informacion.enemigos[i].radio = enemigo.getBoundingRadius() * Constants.PPM;
			informacion.enemigos[i].personaje = enemigo.getAlumno().ordinal();
		}
		informacion.ronda = ronda;
		Global.servidor.enviarMensaje(informacion);
	}

	private boolean cheaquearFin() {
		for (Jugador jugador : jugadores) {
			if (!(jugador.isMuerto() || jugador.isAbatido()))
				return true;
		}
		return false;
	}

	private void actualizarJugadores(float delta) {
		for (int i = 0; i < jugadores.size; i++) {
			Jugador jugador = jugadores.get(i);
			if (!jugador.isMuerto()) {
				if (Global.mensajesJugadores[i] != null && Global.mensajesJugadores[i] instanceof ArrayList<?>) {
					jugador.actualizar(delta, (ArrayList<Integer>) Global.mensajesJugadores[i]);
				}
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
		if (enemigosActivos.size < cantEnemigosMaximo && cantEnemigosRestantes > 0) {
			Vector2 posicion = spawnsEnemigos.get(MathUtils.random(spawnsEnemigos.size - 1)).getPosition();
			enemigoPool.obtain().init((int) (posicion.x * PPM), (int) (posicion.y * PPM), 20f, 100 + ronda * 10);
			cantEnemigosRestantes--;
		}
		if (enemigosActivos.isEmpty() && cantEnemigosRestantes == 0) {
			spawnearJugadores();
			Gdx.app.log("Sistema", "Round " + ++ronda);
			cantEnemigosRonda *= 1.5;
			cantEnemigosRestantes = cantEnemigosRonda;
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
			if (!bala.isActivo()) {
				balaPool.free(bala);
			}
		}
	}

	private void cameraUpdate(float delta) {
		Vector3 position = Global.camara.position;
		position.x = Global.camara.position.x
				+ (((jugadores.first().getPosition().x * PPM) - Global.camara.position.x) * .05f);
		position.y = Global.camara.position.y
				+ (((jugadores.first().getPosition().y * PPM) - Global.camara.position.y) * .05f);
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
		jugadores = null;
		balasActivas = null;
		enemigosActivos = null;
		balaPool = null;
		enemigoPool = null;
	}
}
