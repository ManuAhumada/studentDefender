package com.studentdefender.personajes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.studentdefender.armas.Arma;
import com.studentdefender.armas.Bala;
import com.studentdefender.armas.Pistola;

public abstract class Personaje extends Sprite {
	protected int vida;
	protected int vidaActual;
	protected int defensa;
	protected int velocidad;
	protected Arma armas[];
	protected int armaSeleccionada;

	public Personaje() {
		super(new Texture(Gdx.files.internal("circle.png")));
		vida = 100;
		vidaActual = vida;
		setPosition(0, 0);
		setRotation(180);
		defensa = 10;
		velocidad = 10;
		armas = new Arma[] { new Pistola() };
	}

	public Personaje(Sprite imagen, int vida, int defensa, int velocidad, Arma armas[]) {
		this.vida = vida;
		vidaActual = vida;
		setRotation(180);
		this.defensa = defensa;
		this.velocidad = velocidad;
		this.armas = armas;
	}

	protected abstract void rotar(OrthographicCamera camara);

	protected abstract void mover(float delta);

	protected abstract void atacar(OrthographicCamera camara, Array<Bala> balas);

	protected abstract void recargar();

	public void actualizar(OrthographicCamera camera, float delta, Array<Bala> balas) {
		rotar(camera);
		mover(delta);
		revisarLimites();
		recargar();
		atacar(camera, balas);
	}

	private void revisarLimites() {
		if (getX() < 0) {
			setX(0);
		}
		if (getX() > (Gdx.graphics.getWidth() - getWidth())) {
			setX(Gdx.graphics.getWidth() - getWidth());
		}
		if (getY() < 0) {
			setY(0);
		}
		if (getY() > (Gdx.graphics.getHeight() - getHeight())) {
			setY(Gdx.graphics.getHeight() - getHeight());
		}
	}

	public void quitarVida(int vidaQuitada) {
		vidaActual -= vidaQuitada;
		if (vidaActual < 0) {
			vidaActual = 0;
		}
	}

	public void agregarVida(int vidaAgregada) {
		vidaActual += vidaAgregada;
		if (vidaActual > vida) {
			vidaActual = vida;
		}
	}

	public Vector2 getPosicion() {
		return new Vector2(getX(), getY());
	}
}
