package com.studentdefender.armas;

import static com.studentdefender.utils.Constants.PPM;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.studentdefender.juego.GameScreen;
import com.studentdefender.personajes.Personaje;
import com.studentdefender.utils.Constants;;

public class Bala implements Poolable{
	private Personaje disparador;
	private Body body;
	private int daño;
	private int velocidad;
	private boolean activo;
	
	public Bala() {
		activo = false;
		velocidad = 50;
		body = createCircle(1f / PPM);
	}
	
	public void init(Vector2 posicion, float angulo, int daño, Personaje disparador) {
		activo = true;
		body.setActive(true);
		this.daño = daño;
		this.disparador = disparador;
		body.setTransform(posicion.add(new Vector2(MathUtils.cos(angulo), MathUtils.sin(angulo)).scl((this.getRadio() + disparador.getRadio() + .01f))), angulo);
		body.setLinearVelocity(new Vector2(MathUtils.cos(angulo), MathUtils.sin(angulo)).scl(velocidad));
		GameScreen.balasActivas.add(this);
	}
	
	public void reset() {
		GameScreen.balasActivas.removeValue(this, true);
		this.daño = 0;
		this.disparador = null;
		body.setTransform(0, 0, 0);
		body.setLinearVelocity(0, 0);
		body.setActive(false);
	}

	public void impactar(Object objeto) {
		if (objeto instanceof Personaje) {
			Personaje enemigo = (Personaje) objeto;
			enemigo.quitarVida(daño);
		}
		activo = false;
	}
	
	private Body createCircle(float radius) {
        Body pBody;
        BodyDef def = new BodyDef();

        def.type = BodyDef.BodyType.DynamicBody;

        def.fixedRotation = true;
        def.bullet = true;
        def.fixedRotation = true;
        def.active = false;
        
        pBody = GameScreen.world.createBody(def);

        CircleShape shape = new CircleShape();
        shape.setRadius(radius);

        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.filter.categoryBits = Constants.BIT_BALA;
        fd.filter.maskBits = ~Constants.BIT_BALA;
        pBody.createFixture(fd).setUserData(this);
        shape.dispose();
        return pBody;	
    }
	
	public boolean isActivo() {
		return activo;
	}
	
	private Vector2 getPosicion() {
		return body.getPosition();
	}
	
	private float getRadio() {
		return body.getFixtureList().first().getShape().getRadius();
	}

	public Body getBody() {
		return body;
	}
}
