package com.studentdefender.juego;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.studentdefender.utils.Global;


public class MainMenuScreen implements Screen {
	final StudentDefender game;

	public MainMenuScreen(final StudentDefender game) {
		this.game = game;

		Global.camara.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		Global.camara.update();
		Global.batch.setProjectionMatrix(Global.camara.combined);

		Global.batch.begin();
		Global.font.draw(Global.batch, "Student Defender", (Gdx.graphics.getWidth() / 2) - 100,
				Gdx.graphics.getHeight() - 100);
		Global.font.draw(Global.batch, "Tap anywhere to begin!", (Gdx.graphics.getWidth() / 2) - 100,
				Gdx.graphics.getHeight() / 2);
		Global.batch.end();

		if (Gdx.input.isTouched()) {
			game.setScreen(new PantallaSeleccion(game));
			dispose();
		}
	}

	public void show() {

	}

	public void resize(int width, int height) {
		Global.camara.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Global.camara.update();
	}

	public void pause() {

	}

	public void resume() {

	}

	public void hide() {

	}

	public void dispose() {

	}
}
