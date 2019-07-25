package com.studentdefender.personajes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.studentdefender.armas.Bala;

public class Jugador extends Personaje {
	protected int dinero;

	public Jugador() {
		dinero = 0;
	}

	protected void rotar(OrthographicCamera camara) {
		Vector3 mouse3D = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
		camara.unproject(mouse3D);
		Vector2 mouse2D = new Vector2(mouse3D.x, mouse3D.y);
		setRotation(mouse2D.sub(getPosicion()).angle() - 90); // Le resto 90 porque Sprite toma el angulo 0 arriba y la
																// funcion angle() desde la derecha
	}

	protected void mover(float delta) {
		Vector2 movimiento = new Vector2();

		if (Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A)) {
			movimiento.x -= velocidad;
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.D)) {
			movimiento.x += velocidad;
		}
		if (Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.W)) {
			movimiento.y += velocidad;
		}
		if (Gdx.input.isKeyPressed(Keys.DOWN) || Gdx.input.isKeyPressed(Keys.S)) {
			movimiento.y -= velocidad;
		}
		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) {
			movimiento.x *= 1.5;
			movimiento.y *= 1.5;
		}

		setPosition(getX() + (movimiento.x * delta * 30), getY() + (movimiento.y * delta * 30));
	}

	protected void atacar(OrthographicCamera camara, Array<Bala> balas) {
		if ((armas[armaSeleccionada].isAutomatica() && Gdx.input.isKeyPressed(Keys.E))
				|| (!armas[armaSeleccionada].isAutomatica() && Gdx.input.isKeyJustPressed(Keys.E))) {
			Gdx.app.log("Personaje", "Atacando");
			Vector3 objetivo = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
			camara.unproject(objetivo);
			armas[armaSeleccionada].atacar(getPosicion(), new Vector2(objetivo.x, objetivo.y), balas);
		}
	}

	protected void recargar() {
		if (Gdx.input.isKeyJustPressed(Keys.R)) {
			Gdx.app.log("Personaje", "Recargando");
			armas[armaSeleccionada].recargar();
		}
	}

	public void agregarDinero(int dinero) {
		this.dinero += dinero;
	}

	public boolean quitarDinero(int dinero) {
		if (this.dinero >= dinero) {
			this.dinero -= dinero;
			return true;
		} else {
			return false;
		}
	}
}
