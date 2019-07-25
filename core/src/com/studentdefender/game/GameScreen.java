package com.studentdefender.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;
import com.studentdefender.armas.Bala;
import com.studentdefender.personajes.Personaje;

public class GameScreen implements Screen {
	final StudentDefender game;

	Array<Bala> balas;
	
	OrthographicCamera camara;
	Personaje jugador;

	public GameScreen(final StudentDefender game) {
		this.game = game;

		camara = new OrthographicCamera();
		camara.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		jugador = new Personaje();
		balas = new Array<Bala>();

	}

	public void render(float delta) {

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camara.update();
		
		jugador.actualizar(camara, delta, balas);

		game.batch.setProjectionMatrix(camara.combined);

		game.batch.begin();
		jugador.draw(game.batch);
		for (Bala bala : balas) {
			bala.actualizar(balas);
			bala.draw(game.batch);
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
