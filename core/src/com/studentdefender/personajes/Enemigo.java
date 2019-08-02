package com.studentdefender.personajes;

import static com.studentdefender.utils.Constants.PPM;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.studentdefender.armas.Pistola;
import com.studentdefender.juego.GameScreen;

public class Enemigo extends Personaje implements Poolable {
	
	private boolean activo;

	public Enemigo() {
		super(0, 0, 0, 100, 10, 100);
		activo = false;
		body.setActive(false);
		armas[0] = new Pistola();
	}
	
	public void init(int x, int y, float radio) {
		activo = true;
		body.setActive(true);
		body.setTransform(x / PPM , y / PPM, 0);
		body.getFixtureList().first().getShape().setRadius(radio / PPM);
		GameScreen.enemigosActivos.add(this);
	}
	
	public void reset() {
		GameScreen.enemigosActivos.removeValue(this, true);
		body.setTransform(0, 0, 0);
		body.setLinearVelocity(0, 0);
		vidaActual = vida;
		body.setActive(false);
	}

	protected void rotar(OrthographicCamera camara) {
	}

	protected void mover(float delta) {
	}

	protected void recargar() {
	}

	protected void atacar() {
	}
	
	public void quitarVida(int vidaQuitada) {
		super.quitarVida(vidaQuitada);
		if(vidaActual == 0) {
			System.out.println("Morido");
			activo = false;
		}
	}
	
	public boolean isActivo() {
		return activo;
	}
}
