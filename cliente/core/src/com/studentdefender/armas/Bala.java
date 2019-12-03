package com.studentdefender.armas;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.studentdefender.objetos_red.CuerpoRed;
import com.studentdefender.utils.Global;;

public class Bala {

	private Vector2 posicion;
	private float radio;

	public Bala(CuerpoRed bala) {
		this.posicion = new Vector2(bala.x, bala.y);
		this.radio = bala.radio;
	}

	public void dibujar() {
		Global.shapeRenderer.begin(ShapeType.Filled);
		Global.shapeRenderer.circle(posicion.x, posicion.y, radio);
		Global.shapeRenderer.end();
	}
}
