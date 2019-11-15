package com.studentdefender.personajes;

import com.badlogic.gdx.math.Vector2;

public abstract class Personaje {
	protected Vector2 posicion;
	protected float radio;
	protected int vida;
	protected int vidaActual;

	public Personaje(int x, int y, float radio, int vida, int velocidad, boolean isEnemy) {
		this.vida = vida;
		vidaActual = vida;
	}

	public abstract void actualizar(float delta);

	public void dibujar() {

	}

	public Vector2 getPosicion() {
		return posicion;
	}
}
