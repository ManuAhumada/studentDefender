package com.studentdefender.personajes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Personaje extends Sprite {
	protected int vida;
	protected int vidaActual;
	protected int defensa;
	protected int velocidad;
	// protected Arma armas[];

	public Personaje() {
		super(new Texture(Gdx.files.internal("circle.png")));
		vida = 100;
		vidaActual = vida;
		setPosition(0, 0);
		setRotation(180);
		defensa = 10;
		velocidad = 10;
	}

	public Personaje(Sprite imagen, int vida, int defensa, int velocidad) {
		this.vida = vida;
		vidaActual = vida;
		setRotation(180);
		this.defensa = defensa;
		this.velocidad = velocidad;
	}

	public void actualizar(OrthographicCamera camera, float delta) {
		rotar(camera);
		mover(delta);
		revisarLimites();
	}

	private void rotar(OrthographicCamera camera) {
		Vector3 mouse3D = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
		camera.unproject(mouse3D);
		Vector2 mouse2D = new Vector2(mouse3D.x, mouse3D.y);
		setRotation(mouse2D.sub(getPosicion()).angle() - 90); // Le resto 90 porque Sprite toma el angulo 0 arriba y la
																// funcion angle() desde la derecha

	}

	private void mover(float delta) {
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

	public Vector2 getPosicion() {
		return new Vector2(getX(), getY());
	}
}
