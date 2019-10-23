package com.studentdefender.juego;

import static com.studentdefender.utils.Constants.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.studentdefender.armas.Bala;
import com.studentdefender.personajes.Enemigo;
import com.studentdefender.personajes.Jugador;
import com.studentdefender.utils.TiledObjectUtil;
import com.studentdefender.utils.WorldContactListener;

public class GameScreen implements Screen {
	final StudentDefender game;

	private final float SCALE = 1f;

	private Box2DDebugRenderer b2dr;
	public static World world;

	// private OrthogonalTiledMapRenderer tmr;
	private TiledMap map;

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

		map = new TmxMapLoader().load("mapas\\test_map.tmx");
		// tmr = new OrthogonalTiledMapRenderer(map);
		TiledObjectUtil.parseTiledObjectLayer(world, map.getLayers().get("collision-layer").getObjects());

		jugadores.add(new Jugador(200, 200, 7.5f));

		cantEnemigos = 1;
	}

	public void render(float delta) {

		update(delta);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// tmr.render();
		b2dr.render(world, camara.combined.cpy().scl(PPM));
		game.batch.setProjectionMatrix(camara.combined);

		game.batch.begin();
		for (Enemigo enemigo : enemigosActivos) {
			enemigo.dibujar(game.batch, game.font);
		}
		for (Jugador jugador : jugadores) {
			jugador.dibujar(game.batch, game.font);
		}
		game.batch.end();

		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			Gdx.app.exit();
		}
	}

	private void update(float delta) {
		world.step(1 / 60f, 6, 2);

		actualizarBalas();
		spawnearEnemigos();
		actualizarEnemigos(delta);
		actualizarJugadores(delta);

		cameraUpdate(delta);
		// tmr.setView(camara);
	}

	private void actualizarJugadores(float delta) {
		for (Jugador jugador : jugadores) {
			jugador.actualizar(delta);
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
			Gdx.app.log("Sistema", "Round " + cantEnemigos);
			for (int i = 0; i < cantEnemigos; i++) {
				enemigoPool.obtain().init(MathUtils.random(50, 300), MathUtils.random(50, 300), 7.5f);
			}
			cantEnemigos++;
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
		position.x = camara.position.x + (((jugadores.first().getPosicion().x * PPM) - camara.position.x) * .05f);
		position.y = camara.position.y + (((jugadores.first().getPosicion().y * PPM) - camara.position.y) * .05f);
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
	}

}
