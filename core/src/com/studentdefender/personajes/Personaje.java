package com.studentdefender.personajes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.studentdefender.armas.Arma;
import com.studentdefender.armas.Bala;
import com.studentdefender.armas.Pistola;

public class Personaje extends Sprite {
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

	public void actualizar(OrthographicCamera camera, float delta, Array<Bala> balas) {
		rotar(camera);
		mover(delta);
		revisarLimites();
		recargar();
		atacar(camera, balas);
	}

	private void rotar(OrthographicCamera camara) {
		Vector3 mouse3D = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
		camara.unproject(mouse3D);
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

	private void atacar(OrthographicCamera camara, Array<Bala> balas) {
		if ((armas[armaSeleccionada].isAutomatica() && Gdx.input.isKeyPressed(Keys.E))
				|| (!armas[armaSeleccionada].isAutomatica() && Gdx.input.isKeyJustPressed(Keys.E))) {
			Gdx.app.log("Personaje", "Atacando");
			Vector3 objetivo = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
			camara.unproject(objetivo);
			armas[armaSeleccionada].atacar(getPosicion(), new Vector2(objetivo.x, objetivo.y), balas);
		}
	}

	private void recargar() {
		if (Gdx.input.isKeyJustPressed(Keys.R)) {
			Gdx.app.log("Personaje", "Recargando");
			armas[armaSeleccionada].recargar();
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
