package com.studentdefender.personajes;

import static com.studentdefender.utils.Constants.PPM;

import java.text.DecimalFormat;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.utils.Location;
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

public abstract class Personaje implements Steerable<Vector2> {
	protected int vida;
	protected int vidaActual;
	protected float velocidad;
	protected Arma armas[];
	protected int armaSeleccionada;
	protected Body body;
	protected float maxLinearSpeed, maxLinearAcceleration, maxAngularSpeed, maxAngularAcceleration;
	protected boolean isTagged;

	public Personaje(int x, int y, float radio, int vida, int velocidad, boolean isEnemy) {
		body = createCircle(x, y, radio, isEnemy);
		this.vida = vida;
		vidaActual = vida;
		this.velocidad = velocidad;
		this.maxLinearSpeed = 500;
		this.maxLinearAcceleration = 5000;
		this.maxAngularSpeed = 30;
		this.maxAngularAcceleration = 5;
	}

	private Body createCircle(float x, float y, float radius, boolean isEnemy) {
		Body pBody;
		BodyDef def = new BodyDef();

		def.type = BodyDef.BodyType.DynamicBody;

		def.position.set(x / PPM, y / PPM);
		def.fixedRotation = true;
		pBody = GameScreen.world.createBody(def);

		CircleShape shape = new CircleShape();
		shape.setRadius(radius / PPM);

		FixtureDef fd = new FixtureDef();
		fd.filter.categoryBits = isEnemy ? Constants.BIT_ENEMIGO : Constants.BIT_JUGADOR;
		if (isEnemy) {
			fd.filter.maskBits = Constants.BIT_BALA | Constants.BIT_PARED | Constants.BIT_JUGADOR;
		} else {
			fd.filter.maskBits = Constants.BIT_BALA | Constants.BIT_PARED | Constants.BIT_JUGADOR | Constants.BIT_PUERTA_ENEMIGO;
		}
		
		fd.shape = shape;
		pBody.createFixture(fd).setUserData(this);
		shape.dispose();
		return pBody;
	}

	public abstract void actualizar(float delta);

	public void dibujar(SpriteBatch batch, BitmapFont font) {
		batch.begin();
		if (Constants.DEBUG) {
			DecimalFormat f = new DecimalFormat("#.##");
			font.draw(batch, "x: " + f.format(getPosition().x) + ", y: " + f.format(getPosition().y),
					getPosition().x * PPM - 55, getPosition().y * PPM - 10);
		}
		batch.end();
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

	public Body getBody() {
		return body;
	}

	@Override
	public Vector2 getPosition() {
		return body.getPosition();
	}

	@Override
	public float getOrientation() {
		return body.getAngle();
	}

	@Override
	public void setOrientation(float orientation) {
		body.setTransform(getPosition(), orientation);
	}

	@Override
	public float vectorToAngle(Vector2 vector) {
		return vector.angleRad();
	}

	@Override
	public Vector2 angleToVector(Vector2 outVector, float angle) {
		outVector.x = (float) -Math.sin(angle);
		outVector.y = (float) Math.cos(angle);
		return outVector;
	}

	@Override
	public Location<Vector2> newLocation() {
		return this;
	}

	@Override
	public float getZeroLinearSpeedThreshold() {
		return 0;
	}

	@Override
	public void setZeroLinearSpeedThreshold(float value) {
	}

	@Override
	public float getMaxLinearSpeed() {
		return maxLinearSpeed;
	}

	@Override
	public void setMaxLinearSpeed(float maxLinearSpeed) {
		this.maxLinearSpeed = maxLinearSpeed;
	}

	@Override
	public float getMaxLinearAcceleration() {
		return maxLinearAcceleration;
	}

	@Override
	public void setMaxLinearAcceleration(float maxLinearAcceleration) {
		this.maxLinearAcceleration = maxLinearAcceleration;
	}

	@Override
	public float getMaxAngularSpeed() {
		return maxAngularSpeed;
	}

	@Override
	public void setMaxAngularSpeed(float maxAngularSpeed) {
		this.maxAngularSpeed = maxAngularSpeed;
	}

	@Override
	public float getMaxAngularAcceleration() {
		return maxAngularAcceleration;
	}

	@Override
	public void setMaxAngularAcceleration(float maxAngularAcceleration) {
		this.maxAngularAcceleration = maxAngularAcceleration;
	}

	@Override
	public Vector2 getLinearVelocity() {
		return body.getLinearVelocity();
	}

	@Override
	public float getAngularVelocity() {
		return body.getAngularVelocity();
	}

	@Override
	public float getBoundingRadius() {
		return body.getFixtureList().first().getShape().getRadius();
	}

	@Override
	public boolean isTagged() {
		return isTagged;
	}

	@Override
	public void setTagged(boolean tagged) {
		isTagged = tagged;
	}
}
