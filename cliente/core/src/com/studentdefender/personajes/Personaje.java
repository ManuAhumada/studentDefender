package com.studentdefender.personajes;

import com.badlogic.gdx.math.Vector2;

public abstract class Personaje {
	protected Vector2 posicion;
	protected float radio;
	protected int vida;
	protected int vidaActual;
	protected float orientacion;

	public Personaje(float x, float y, float radio, int vida, int vidaActual, float orientacion) {
		this.posicion = new Vector2(x, y);
		this.vida = vida;
		this.vidaActual = vidaActual;
		this.radio = radio;
		this.orientacion = orientacion;
	}

	public void dibujar() {
	}

	public Vector2 getPosicion() {
		return posicion;
	}
}
