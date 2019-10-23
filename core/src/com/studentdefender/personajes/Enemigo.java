package com.studentdefender.personajes;

import static com.studentdefender.utils.Constants.PPM;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.badlogic.gdx.utils.TimeUtils;
import com.studentdefender.juego.GameScreen;

public class Enemigo extends Personaje implements Poolable {

	private boolean activo;
	private int fuerza;
	private long ultimoAtaque;

	public Enemigo() {
		super(0, 0, 0, 100, 100);
		fuerza = 10;
		activo = false;
		body.setActive(false);
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
		rotar();
		mover(delta);
	}

	protected void rotar() {
		Jugador jugadorCercano = encontrarEnemigoCercano();
		Vector2 posicionEnemigo = jugadorCercano.getPosicion();
		Vector2 toTarget = posicionEnemigo.sub(body.getPosition()).nor();
		float angulo = MathUtils.degreesToRadians * toTarget.angle();
		body.setTransform(body.getPosition(), angulo);
	}

	private Jugador encontrarEnemigoCercano() {
		Jugador jugadorCercano = null;
		float distanciaMinima = 0;
		for (Jugador jugador : GameScreen.jugadores) {
			float distancia = this.body.getPosition().dst2(jugador.getPosicion());
			if (distancia < distanciaMinima || jugadorCercano == null) {
				jugadorCercano = jugador;
				distanciaMinima = distancia;
			}
		}
		return jugadorCercano;
	}

	protected void mover(float delta) {
		Jugador jugadorCercano = encontrarEnemigoCercano();
		Vector2 posicionEnemigo = jugadorCercano.getPosicion();
		Vector2 toTarget = posicionEnemigo.sub(body.getPosition()).nor();
		body.setLinearVelocity(toTarget.scl(velocidad * delta));
	}

	protected void recargar() {
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
		font.draw(batch, vidaActual + "/" + vida, (getPosicion().x - getRadio() * 3) * PPM, getPosicion().y * PPM + 30);
	}

	protected void cambiarArma() {
	}
}
