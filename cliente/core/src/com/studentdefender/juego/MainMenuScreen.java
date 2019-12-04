package com.studentdefender.juego;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.studentdefender.conexion.HiloCliente;
import com.studentdefender.conexion.Mensaje;
import com.studentdefender.utils.Global;

public class MainMenuScreen implements Screen {
	final StudentDefender game;
	boolean wasTouched;

	public MainMenuScreen(final StudentDefender game) {
		this.game = game;
		this.wasTouched = false;

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
		Global.font.draw(Global.batch, "Aprete en cualquier lado para comenzar", (Gdx.graphics.getWidth() / 2) - 100,
				Gdx.graphics.getHeight() / 2);
		Global.batch.end();

		if (Gdx.input.isTouched() && !wasTouched) {
			wasTouched = true;
			Global.servidor = new HiloCliente();
			Global.servidor.start();
		}
		if (wasTouched) {
			if (!Global.mensaje.isEmpty()) {
				Mensaje mensaje = Global.mensaje.get(0);
				System.out.println(mensaje.mensaje);
				if (Global.mensaje != null && mensaje.mensaje instanceof String
						&& ((String) mensaje.mensaje).equals("conectado")) {
					Global.jugador = mensaje.jugador;
					game.setScreen(new PantallaSeleccion(game));
					dispose();
				}
			}
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
