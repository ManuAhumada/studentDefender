package com.studentdefender.juego;

import com.badlogic.gdx.Game;
import com.studentdefender.conexion.HiloServidor;
import com.studentdefender.utils.Global;

public class StudentDefender extends Game {

	public void create() {
		Global.servidor = new HiloServidor();
		Global.servidor.start();
		setScreen(new PantallaSeleccion(this));
	}

	public void render() {
		super.render();
	}

	public void dispose() {
		Global.batch.dispose();
		Global.font.dispose();
		Global.shapeRenderer.dispose();
	}
}
