package com.studentdefender.juego;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.studentdefender.armas.Bala;
import com.studentdefender.objetos_red.CuerpoRed;
import com.studentdefender.objetos_red.JuegoRed;
import com.studentdefender.objetos_red.JugadorRed;
import com.studentdefender.objetos_red.PersonajeRed;
import com.studentdefender.personajes.Enemigo;
import com.studentdefender.personajes.Jugador;
import com.studentdefender.utils.Global;
import com.studentdefender.utils.TiledObjectUtil;

import box2dLight.RayHandler;

public class GameScreen implements Screen {
	final StudentDefender game;

	private final float SCALE = 2f;

	public static World world;
	public static RayHandler rayHandler;

	private OrthogonalTiledMapRenderer tmr;
	public static TiledMap map;

	public static Jugador[] jugadores;

	private Bala[] balas;
	private Enemigo[] enemigos;

	private int ronda;
	private boolean fin;

	public GameScreen(final StudentDefender game) {
		this.game = game;

		Global.camara.setToOrtho(false, Gdx.graphics.getWidth() / SCALE, Gdx.graphics.getHeight() / SCALE);

		world = new World(new Vector2(0, 0), false);

		rayHandler = new RayHandler(world);
		rayHandler.setAmbientLight(0);

		crearMapa();

		jugadores = new Jugador[0];

		balas = new Bala[0];
		enemigos = new Enemigo[0];

		ronda = 0;
		fin = false;
	}

	private void crearMapa() {
		map = new TmxMapLoader().load("mapas\\Mapa-PlantaBaja.tmx");
		tmr = new OrthogonalTiledMapRenderer(map, Global.batch);
		TiledObjectUtil.parseTiledObjectLayer(world, map.getLayers().get("collision-layer").getObjects(), false);
	}

	public void render(float delta) {

		update(delta);

		if (!fin) {
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			Global.batch.setProjectionMatrix(Global.camara.combined);
			Global.shapeRenderer.setProjectionMatrix(Global.camara.combined);
			rayHandler.setCombinedMatrix(Global.camara.combined.cpy());

			tmr.render();
			dibujar();
			rayHandler.render();
			dibujarInterfaz();
		} else {
			Gdx.app.exit();
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
		if (jugadores.length > 0)
			jugadores[Global.jugador].dibujarMejoras();
		Global.batch.begin();
		Global.font.draw(Global.batch, "Round " + (ronda + 1),
				Global.camara.position.x - Global.camara.viewportWidth / 2 + 10,
				Global.camara.position.y - Global.camara.viewportHeight / 2 + 20);
		Global.batch.end();
	}

	private void dibujar() {
		for (Enemigo enemigo : enemigos) {
			enemigo.dibujar();
		}
		for (Jugador jugador : jugadores) {
			jugador.dibujar();
		}
		for (Bala bala : balas) {
			bala.dibujar();
		}
	}

	private void update(float delta) {
		world.step(1 / 60f, 6, 2);
		rayHandler.update();
		if (!fin) {
			if (Global.mensaje.size() > 0
					&& Global.mensaje.get(Global.mensaje.size() - 1).mensaje instanceof JuegoRed) {
				JuegoRed informacion = (JuegoRed) Global.mensaje.get(Global.mensaje.size() - 1).mensaje;
				ronda = informacion.ronda;
				actualizarEnemigos(informacion.enemigos);
				actualizarBalas(informacion.balas);
				actualizarJugadores(informacion.jugadores);
				cameraUpdate(delta);
				jugadores[Global.jugador].sendInput();
			} else {
				if (Global.mensaje.size() > 0 && Global.mensaje.get(Global.mensaje.size() - 1).mensaje instanceof String
						&& ((String) Global.mensaje.get(Global.mensaje.size() - 1).mensaje).equals("fin"))
					fin = true;
			}
			tmr.setView(Global.camara);
		}
	}

	private void actualizarJugadores(JugadorRed[] jugadores) {
		if (this.jugadores.length == 0) {
			this.jugadores = new Jugador[jugadores.length];
			for (int i = 0; i < jugadores.length; i++) {
				this.jugadores[i] = new Jugador(jugadores[i]);
			}
		} else {
			for (int i = 0; i < jugadores.length; i++) {
				this.jugadores[i].actualizar(jugadores[i]);
			}
		}

	}

	private void actualizarBalas(CuerpoRed[] balas) {
		this.balas = new Bala[balas.length];
		for (int i = 0; i < balas.length; i++) {
			this.balas[i] = new Bala(balas[i]);
		}
	}

	private void actualizarEnemigos(PersonajeRed[] enemigos) {
		this.enemigos = new Enemigo[enemigos.length];
		for (int i = 0; i < enemigos.length; i++) {
			this.enemigos[i] = new Enemigo(enemigos[i]);
		}
	}

	private void cameraUpdate(float delta) {
		Vector3 position = Global.camara.position;
		position.x = Global.camara.position.x
				+ (((jugadores[Global.jugador].getPosicion().x) - Global.camara.position.x) * .075f);
		position.y = Global.camara.position.y
				+ (((jugadores[Global.jugador].getPosicion().y) - Global.camara.position.y) * .075f);
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
		map.dispose();
		tmr.dispose();
		world = null;
		map = null;
		jugadores = null;
	}
}
