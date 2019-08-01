package com.studentdefender.armas;

import static com.studentdefender.utils.Constants.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.studentdefender.juego.GameScreen;
import com.studentdefender.personajes.Personaje;
import com.studentdefender.utils.Constants;;

public class Bala {
	private Personaje disparador;
	private Body body; 
	private float radio;
	private int daño;
	private int velocidad;
	private Vector2 direccion;
	private boolean activa;

	public Bala(Vector2 posicion, float angulo, int daño, Personaje atacante) {
		activa = true;
		velocidad = 50;
		radio = 1f / PPM;
		this.daño = daño;
		direccion = new Vector2(MathUtils.cos(angulo), MathUtils.sin(angulo));
		posicion.add(new Vector2(MathUtils.cos(angulo), MathUtils.sin(angulo)).scl((radio + atacante.getRadio() + .01f)));
		body = createCircle(posicion, radio, angulo);
		GameScreen.activeBullets.add(this);
		Gdx.app.log("Bala", "Bala creada en la posicion " + getPosicion() + " con un angulo de " + body.getAngle() + 
				" y una direccion de " + direccion);
	}

	public void impactar(Object objeto) {
		if (objeto instanceof Personaje) {
			Personaje enemigo = (Personaje) objeto;
			enemigo.quitarVida(daño);
		}
		activa = false;
	}

	public void eliminar() {
		GameScreen.activeBullets.removeValue(this, true);	
		GameScreen.world.destroyBody(body);
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
        
        def.linearVelocity.set(new Vector2(MathUtils.cos(angle), MathUtils.sin(angle)).scl(velocidad));
        pBody = GameScreen.world.createBody(def);

        CircleShape shape = new CircleShape();
        shape.setRadius(radius);

        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        pBody.createFixture(fd).setUserData(this);
        shape.dispose();
        return pBody;	
    }
	
	public boolean isActiva() {
		return activa;
	}
	
	public Vector2 getPosicion() {
		return body.getPosition();
	}
}
