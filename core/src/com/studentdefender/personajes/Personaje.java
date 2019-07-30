package com.studentdefender.personajes;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.studentdefender.armas.Arma;
import com.studentdefender.juego.GameScreen;

public abstract class Personaje {
	protected int vida;
	protected int vidaActual;
	protected int defensa;
	protected int velocidad;
	protected Arma armas[];
	protected int armaSeleccionada;
	protected Body body;

	public Personaje(int x, int y, float radio, int vida, int defensa, int velocidad) {
		body = createCircle(x, y, radio);
		this.vida = vida;
		vidaActual = vida;
		this.defensa = defensa;
		this.velocidad = velocidad;
		this.armas = new Arma[2];
	}
	
	private Body createCircle(float x, float y, float radius) {
        Body pBody;
        BodyDef def = new BodyDef();

        def.type = BodyDef.BodyType.DynamicBody;

        def.position.set(x, y);
        def.fixedRotation = true;
        pBody = GameScreen.world.createBody(def);

        CircleShape shape = new CircleShape();
        shape.setRadius(radius);

        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.density = 1;
//        fd.filter.categoryBits = Constants.BIT_PLAYER;
//        fd.filter.maskBits = Constants.BIT_WALL | Constants.BIT_SENSOR;
//        fd.filter.groupIndex = 0;
        pBody.createFixture(fd).setUserData(this);
        shape.dispose();
        return pBody;
    }

	protected abstract void rotar(OrthographicCamera camara);

	protected abstract void mover(float delta);

	protected abstract void atacar();

	protected abstract void recargar();

	public void actualizar(OrthographicCamera camera, float delta) {
		rotar(camera);
		mover(delta);
		recargar();
		atacar();
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
		return body.getPosition();
	}
}
