package com.studentdefender.personajes;

import static com.studentdefender.utils.Constants.PPM;

import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.ai.steer.behaviors.LookWhereYouAreGoing;
import com.badlogic.gdx.ai.steer.behaviors.PrioritySteering;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.badlogic.gdx.utils.TimeUtils;
import com.studentdefender.juego.GameScreen;

public class Enemigo extends Personaje implements Poolable {

	private boolean activo;
	private int fuerza;
	private long ultimoAtaque;
	private Seek<Vector2> seekBehavior;
	private BlendedSteering<Vector2> steeringBehavior;
	private SteeringAcceleration<Vector2> steeringOutput;

	public Enemigo() {
		super(0, 0, 0, 100, 100);
		fuerza = 10;
		activo = false;
		body.setActive(false);
		steeringOutput = new SteeringAcceleration<Vector2>(new Vector2());
		PrioritySteering<Vector2> prioritySteering = new PrioritySteering<>(this);
		seekBehavior = new Seek<Vector2>(this, GameScreen.jugadores.first());
		prioritySteering.add(seekBehavior);

		steeringBehavior = new BlendedSteering<Vector2>(this);
		steeringBehavior.add(prioritySteering, 1);
		steeringBehavior.add(new LookWhereYouAreGoing<>(this), 1);
	}

	public void init(int x, int y, float radio) {
		activo = true;
		ultimoAtaque = 0;
		body.setActive(true);
		body.setTransform(x / PPM, y / PPM, 0);
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

	public void actualizar(float delta) {
		encontrarEnemigoMasProximo();
		steeringBehavior.calculateSteering(steeringOutput);
		applySteering(steeringOutput, delta);
	}

	private void applySteering(SteeringAcceleration<Vector2> steering, float delta) {
		body.setLinearVelocity(steering.linear.nor().scl(delta * velocidad));
		body.setAngularVelocity(steering.angular);
	}

	private void encontrarEnemigoMasProximo() {
		Jugador jugadorCercano = null;
		float distanciaMinima = 0;
		for (Jugador jugador : GameScreen.jugadores) {
			float distancia = this.body.getPosition().dst2(jugador.getPosition());
			if (distancia < distanciaMinima || jugadorCercano == null) {
				jugadorCercano = jugador;
				distanciaMinima = distancia;
			}
		}
		seekBehavior.setTarget(jugadorCercano);
	}

	public void atacar(Jugador jugador) {
		if (TimeUtils.nanoTime() - ultimoAtaque > 1300000000) {
			ultimoAtaque = TimeUtils.nanoTime();
			jugador.quitarVida(fuerza);
		}
	}

	public void quitarVida(int vidaQuitada) {
		super.quitarVida(vidaQuitada);
		body.setLinearVelocity(0, 0);
		if (vidaActual == 0) {
			activo = false;
		}
	}

	public boolean isActivo() {
		return activo;
	}

	public void dibujar(SpriteBatch batch, BitmapFont font) {
		font.draw(batch, vidaActual + "/" + vida, (getPosition().x - getBoundingRadius() * 3) * PPM,
				getPosition().y * PPM + 30);
	}
}
