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
import com.badlogic.gdx.utils.Array;
import com.studentdefender.armas.Bala;
import com.studentdefender.personajes.Enemigo;
import com.studentdefender.personajes.Jugador;
import com.studentdefender.personajes.Profesores;
import com.studentdefender.utils.Global;

public class GameScreen implements Screen {
	final StudentDefender game;

	private final float SCALE = 1f;

	private Box2DDebugRenderer b2dr;
	public static World world;

	private OrthogonalTiledMapRenderer tmr;
	public static TiledMap map;

	public static Array<Jugador> jugadores;

	public static Array<Bala> balasActivas;
	public static Array<Enemigo> enemigosActivos;

	private int ronda;
	private boolean fin;

	public GameScreen(final StudentDefender game, Profesores profesorSeleccionado) {
		this.game = game;

		Global.camara.setToOrtho(false, Gdx.graphics.getWidth() / SCALE, Gdx.graphics.getHeight() / SCALE);

		world = new World(new Vector2(0, 0), false);
		b2dr = new Box2DDebugRenderer();

		crearMapa();

		jugadores = new Array<Jugador>();

		balasActivas = new Array<Bala>();
		enemigosActivos = new Array<Enemigo>();

		ronda = 0;
		fin = false;
	}

	private void crearMapa() {
		map = new TmxMapLoader().load("mapas\\Mapa-PlantaBaja.tmx");
		tmr = new OrthogonalTiledMapRenderer(map, Global.batch);
	}

	public void render(float delta) {

		update(delta);

		if (!fin) {
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			Global.batch.setProjectionMatrix(Global.camara.combined);
			Global.shapeRenderer.setProjectionMatrix(Global.camara.combined);

			tmr.render();
			dibujar();
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
		Global.font.draw(Global.batch, "Round " + ronda,
				Global.camara.position.x - Global.camara.viewportWidth / 2 + 10,
				Global.camara.position.y - Global.camara.viewportHeight / 2 + 20);
		Global.batch.end();
	}

	private void dibujar() {
		for (Enemigo enemigo : enemigosActivos) {
			enemigo.dibujar();
		}
		for (Jugador jugador : jugadores) {
			jugador.dibujar();
		}
	}

	private void update(float delta) {
		world.step(1 / 60f, 6, 2);
		if (fin) {
			actualizarBalas();
			actualizarEnemigos(delta);
			actualizarJugadores(delta);
			cameraUpdate(delta);
			tmr.setView(Global.camara);
		}
	}

	private void actualizarJugadores(float delta) {
		for (Jugador jugador : jugadores) {
			jugador.actualizar(delta);
		}
	}

	private void actualizarEnemigos(float delta) {
		for (Enemigo enemigo : enemigosActivos) {
			enemigo.actualizar(delta);
		}
	}

	private void actualizarBalas() {
		for (Bala bala : balasActivas) {

		}
	}

	private void cameraUpdate(float delta) {
		Vector3 position = Global.camara.position;
		position.x = Global.camara.position.x
				+ (((jugadores.first().getPosicion().x) - Global.camara.position.x) * .05f);
		position.y = Global.camara.position.y
				+ (((jugadores.first().getPosicion().y) - Global.camara.position.y) * .05f);
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
		world = null;
		map = null;
		jugadores = null;
		balasActivas = null;
		enemigosActivos = null;
	}
}
