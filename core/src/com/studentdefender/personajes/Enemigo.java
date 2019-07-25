package com.studentdefender.personajes;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;
import com.studentdefender.armas.Bala;

public class Enemigo extends Personaje {

	protected void rotar(OrthographicCamera camara) {
		rotate(10);
	}

	protected void mover(float delta) {

	}

	protected void atacar(OrthographicCamera camara, Array<Bala> balas) {

	}

	protected void recargar() {

	}

}
