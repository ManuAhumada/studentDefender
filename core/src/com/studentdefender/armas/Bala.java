package com.studentdefender.armas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.studentdefender.juego.GameScreen;
import com.studentdefender.personajes.Personaje;

public class Bala extends Sprite {
	private Personaje disparador;
	private Body body; 
	private float radio;
	private int daño;
	private int velocidad;

	public Bala(Vector2 posicion, float angulo, int daño, Personaje atacante) {
		velocidad = 10;
		radio = .5f;
		this.daño = daño;
		posicion.add(new Vector2(MathUtils.cos(angulo), MathUtils.sin(angulo)).scl(radio + radio/2 + 16));
		body = createCircle(posicion, radio, angulo);
		Gdx.app.log("Bala", "Bala creada en la posicion " + getPosicion() + " con un angulo de " + body.getAngle());
	}

	public void impactar(Object objeto) {
		if (!objeto.equals(disparador)) {
			if (objeto instanceof Personaje) {
				Personaje enemigo = (Personaje) objeto;
				enemigo.quitarVida(daño);
			}
			eliminar();
		}
	}
	
	public void eliminar() {
		GameScreen.bodysToEliminate.add(body);
	}

	public Vector2 getPosicion() {
		return new Vector2(getX(), getY());
	}
	
	private Body createCircle(Vector2 posicion, float radius, float angle) {
        Body pBody;
        BodyDef def = new BodyDef();

        def.type = BodyDef.BodyType.DynamicBody;

        def.position.set(posicion);
        def.fixedRotation = true;
        def.angle = angle;
        def.bullet = true;
        def.fixedRotation = true;
        
        def.linearVelocity.set(new Vector2(MathUtils.cos(angle), MathUtils.sin(angle)).scl(velocidad * 1000000));
        pBody = GameScreen.world.createBody(def);

        CircleShape shape = new CircleShape();
        shape.setRadius(radius);

        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
//        fd.filter.categoryBits = Constants.BIT_PLAYER;
//        fd.filter.maskBits = Constants.BIT_WALL | Constants.BIT_SENSOR;
//        fd.filter.groupIndex = 0;
        pBody.createFixture(fd).setUserData(this);
        shape.dispose();
        return pBody;	
    }
}
