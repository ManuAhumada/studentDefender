package com.studentdefender.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.studentdefender.personajes.Personaje;

public class GameScreen implements Screen {
	final StudentDefender game;

	OrthographicCamera camera;
	Personaje jugador;

	public GameScreen(final StudentDefender game) {
		this.game = game;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		jugador = new Personaje();

	}

	public void render(float delta) {

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();

		jugador.actualizar(camera, delta);

		game.batch.setProjectionMatrix(camera.combined);

		game.batch.begin();
		jugador.draw(game.batch);
		game.batch.end();

	}

	public void resize(int width, int height) {
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.update();
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
