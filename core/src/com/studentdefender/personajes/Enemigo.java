package com.studentdefender.personajes;

import static com.studentdefender.utils.Constants.PPM;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.studentdefender.armas.Pistola;
import com.studentdefender.juego.GameScreen;

public class Enemigo extends Personaje implements Poolable {
	
	private boolean activo;

	public Enemigo() {
		super(0, 0, 0, 100, 10, 100);
		activo = false;
		body.setActive(false);
		armas[0] = new Pistola();
	}
	
	public void init(int x, int y, float radio) {
		activo = true;
		body.setActive(true);
		body.setTransform(x / PPM , y / PPM, 0);
		body.getFixtureList().first().getShape().setRadius(radio / PPM);
		GameScreen.enemigosActivos.add(this);
	}
	
	public void reset() {
		GameScreen.enemigosActivos.removeValue(this, true);
		body.setTransform(0, 0, 0);
		body.setLinearVelocity(0, 0);
		vidaActual = vida;
		body.setActive(false);
		armas[0] = new Pistola();
	}

	protected void rotar(OrthographicCamera camara) {
		Vector2 posicionEnemigo = GameScreen.jugador.getPosicion();
		Vector2 toTarget = posicionEnemigo.sub(body.getPosition()).nor();
		float angulo = MathUtils.degreesToRadians * toTarget.angle();
		body.setTransform(body.getPosition(), angulo);
	}

	protected void mover(float delta) {
	}

	protected void recargar() {
		Pistola pistola = (Pistola) armas[armaSeleccionada];
		if (pistola.getMunicionEnArma() == 0) {
			pistola.recargar();
		}
	}

	protected void atacar() {
		armas[armaSeleccionada].atacar(getPosicion(), body.getAngle(), this);	
	}
	
	public void quitarVida(int vidaQuitada) {
		super.quitarVida(vidaQuitada);
		body.setLinearVelocity(0, 0);
		if(vidaActual == 0) {
			activo = false;
		}
	}
	
	public boolean isActivo() {
		return activo;
	}

	public void dibujar(SpriteBatch batch, BitmapFont font) {
		font.draw(batch, vidaActual + "/" + vida, (getPosicion().x - getRadio() * 3) * PPM, getPosicion().y*PPM + 30);
	}
}
