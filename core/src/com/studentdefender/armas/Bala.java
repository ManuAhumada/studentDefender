package com.studentdefender.armas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.studentdefender.personajes.Personaje;

public class Bala extends Sprite {
	private int daño;
	private int velocidad;
	private Vector2 direccion;

	public Bala(Vector2 posicion, Vector2 objetivo, int daño) {
		super(new Texture(Gdx.files.internal("bullet.png")));
		setPosition(posicion.x, posicion.y);
		velocidad = 10;
		direccion = objetivo.sub(getPosicion()).nor();
		direccion.x *= velocidad;
		direccion.y *= velocidad;
		this.daño = daño;
		setRotation(direccion.angle() - 90); // Le resto 90 porque Sprite toma el angulo 0 arriba y la
												// funcion angle() desde la derecha
		Gdx.app.log("Bala", "Bala creada en la posicion " + getPosicion() + " con direccion " + direccion
				+ " y orientacion " + getRotation());
	}

	public void actualizar(Array<Bala> balas) {
		mover();
		if (fueraDeLaPantalla()) {
			Gdx.app.log("Bala", "Bala eliminada");
			balas.removeValue(this, true);
		}
	}

	private boolean fueraDeLaPantalla() {
		return (getX() < 0) || (getX() > (Gdx.graphics.getWidth() - getWidth())) || (getY() < 0)
				|| (getY() > (Gdx.graphics.getHeight() - getHeight()));
	}

	public void mover() {
		Vector2 nuevaPosicion = getPosicion().add(direccion);
		setPosition(nuevaPosicion.x, nuevaPosicion.y);
	}

	public void impactar(Personaje enemigo) {
		enemigo.quitarVida(daño);
		System.out.println("La bala a impactado, ha quitado " + daño + " de daño.");
	}

	public Vector2 getPosicion() {
		return new Vector2(getX(), getY());
	}
}
