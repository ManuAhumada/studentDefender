package com.studentdefender.personajes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.studentdefender.utils.Global;

public abstract class Personaje {
	protected Vector2 posicion;
	protected float radio;
	protected int vida;
	protected int vidaActual;

	public Personaje(float x, float y, float radio, int vida) {
		this.posicion = new Vector2(x, y);
		this.vida = vida;
		vidaActual = vida;
	}

	public void dibujar() {
		Global.shapeRenderer.begin(ShapeType.Line);
		Global.shapeRenderer.setColor(Color.WHITE);
		Global.shapeRenderer.circle(posicion.x, posicion.y, radio);
		Global.shapeRenderer.end();
	}

	public Vector2 getPosicion() {
		return posicion;
	}
}
