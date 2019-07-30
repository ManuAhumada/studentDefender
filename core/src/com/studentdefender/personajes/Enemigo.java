package com.studentdefender.personajes;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.studentdefender.armas.Pistola;

public class Enemigo extends Personaje {

	public Enemigo(int x, int y, float radio) {
		super(x, y, radio, 100, 10, 100);
		armas[0] = new Pistola();
	}

	protected void rotar(OrthographicCamera camara) {
	}

	protected void mover(float delta) {
	}

	protected void recargar() {
	}

	protected void atacar() {
	}

}
