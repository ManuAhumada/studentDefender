package com.studentdefender.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;
import com.studentdefender.armas.Bala;
import com.studentdefender.personajes.Enemigo;
import com.studentdefender.personajes.Jugador;
import com.studentdefender.personajes.Personaje;

public class GameScreen implements Screen {
	final StudentDefender game;

	Array<Bala> balas;

	OrthographicCamera camara;
	Personaje jugador;
	Personaje enemigo;
	Personaje personajes[];

	public GameScreen(final StudentDefender game) {
		this.game = game;

		camara = new OrthographicCamera();
		camara.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		personajes = new Personaje[] {new Jugador(), new Enemigo()}; 
		
		balas = new Array<Bala>();

	}

	public void render(float delta) {

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camara.update();		

		game.batch.setProjectionMatrix(camara.combined);

		game.batch.begin();
		
		for (Bala bala : balas) {
			bala.actualizar(balas);
			bala.draw(game.batch);
		}
		for (Personaje personaje : personajes) {
			personaje.actualizar(camara, delta, balas);
			personaje.draw(game.batch);
		}
		game.batch.end();

	}

	public void resize(int width, int height) {
		camara.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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

	}

}
