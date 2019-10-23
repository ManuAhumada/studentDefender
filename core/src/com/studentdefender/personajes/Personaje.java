package com.studentdefender.personajes;

import static com.studentdefender.utils.Constants.PPM;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.studentdefender.armas.Arma;
import com.studentdefender.juego.GameScreen;
import com.studentdefender.utils.Constants;

public abstract class Personaje {
	protected int vida;
	protected int vidaActual;
	protected int velocidad;
	protected Arma armas[];
	protected int armaSeleccionada;
	protected Body body;

	public Personaje(int x, int y, float radio, int vida, int velocidad) {
		body = createCircle(x, y, radio);
		this.vida = vida;
		vidaActual = vida;
		this.velocidad = velocidad;
	}

	private Body createCircle(float x, float y, float radius) {
		Body pBody;
		BodyDef def = new BodyDef();

		def.type = BodyDef.BodyType.DynamicBody;

		def.position.set(x / PPM, y / PPM);
		def.fixedRotation = true;
		pBody = GameScreen.world.createBody(def);

		CircleShape shape = new CircleShape();
		shape.setRadius(radius / PPM);

		FixtureDef fd = new FixtureDef();
		fd.filter.categoryBits = Constants.BIT_PERSONAJE;
		fd.filter.maskBits = Constants.BIT_BALA | Constants.BIT_PARED | Constants.BIT_PERSONAJE;
		fd.shape = shape;
		pBody.createFixture(fd).setUserData(this);
		shape.dispose();
		return pBody;
	}

	public abstract void actualizar(float delta);

	protected abstract void cambiarArma();

	protected abstract void rotar();

	protected abstract void mover(float delta);

	protected abstract void recargar();

	public abstract void dibujar(SpriteBatch batch, BitmapFont font);

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
		return body.getPosition();
	}

	public float getRadio() {
		return body.getFixtureList().first().getShape().getRadius();
	}

	public Body getBody() {
		return body;
	}
}
