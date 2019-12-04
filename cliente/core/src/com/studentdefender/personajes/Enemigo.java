package com.studentdefender.personajes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.studentdefender.objetos_red.PersonajeRed;
import com.studentdefender.utils.Global;

public class Enemigo extends Personaje {

	Alumnos alumno;

	public Enemigo(PersonajeRed personajeRed) {
		super(personajeRed.x, personajeRed.y, personajeRed.radio, personajeRed.vida, personajeRed.vidaActual,
				personajeRed.orientacion);
		this.alumno = Alumnos.values()[personajeRed.personaje];
	}

	public void dibujar() {
		super.dibujar();
		Global.batch.begin();
		alumno.getPersonaje().setRotation(orientacion);
		alumno.getPersonaje().setPosition(posicion.x - radio, posicion.y - radio);
		alumno.getPersonaje().draw(Global.batch);
		Global.batch.end();
		Global.shapeRenderer.begin(ShapeType.Filled);
		Global.shapeRenderer.setColor(Color.GREEN);
		Global.shapeRenderer.rect((posicion.x - radio), (posicion.y + radio) + 5, radio * 2 * vidaActual / vida, 5);
		Global.shapeRenderer.setColor(Color.RED);
		Global.shapeRenderer.rect((posicion.x - radio) + radio * 2 * vidaActual / vida, (posicion.y + radio) + 5,
				radio * 2 * (vida - vidaActual) / vida, 5);
		Global.shapeRenderer.end();
	}
}
