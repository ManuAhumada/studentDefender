package com.studentdefender.personajes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.studentdefender.utils.Global;

public class Enemigo extends Personaje {

	public Enemigo() {
		super(0, 0, 0, 100, 300, true);
	}

	// TODO
	public void actualizar(float delta) {

	}

	public void dibujar() {
		super.dibujar();
		Global.shapeRenderer.begin(ShapeType.Filled);
		Global.shapeRenderer.setColor(Color.GREEN);
		Global.shapeRenderer.rect((posicion.x - radio), (posicion.y + radio) + 5, radio * 2 * vidaActual / vida, 5);
		Global.shapeRenderer.setColor(Color.RED);
		Global.shapeRenderer.rect((posicion.x - radio) + radio * 2 * vidaActual / vida, (posicion.y + radio) + 5,
				radio * 2 * (vida - vidaActual) / vida, 5);
		Global.shapeRenderer.end();
	}
}
