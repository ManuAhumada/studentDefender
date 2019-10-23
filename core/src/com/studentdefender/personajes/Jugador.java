package com.studentdefender.personajes;

import static com.studentdefender.utils.Constants.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.studentdefender.armas.Arma;
import com.studentdefender.armas.Pistola;
import com.studentdefender.juego.GameScreen;

public class Jugador extends Personaje {
	protected int dinero;

	public Jugador(int x, int y, float radio) {
		super(x, y, radio, 100, 200);
		body.getFixtureList().first().setDensity(1);
		armas = new Arma[2];
		armas[0] = new Pistola(250000000, 10, true, 100, 100, 10, 10);
		armas[1] = new Pistola(500000000, 20, true, 100, 100, 10, 10);
		dinero = 0;
	}

	public void actualizar(float delta) {
		rotar();
		mover(delta);
		recargar();
		atacar();
		cambiarArma();
	}

	protected void atacar() {
		if ((armas[armaSeleccionada].isAutomatica() && Gdx.input.isKeyPressed(Keys.E))
				|| (!armas[armaSeleccionada].isAutomatica() && Gdx.input.isKeyJustPressed(Keys.E))) {
			armas[armaSeleccionada].atacar(getPosicion(), body.getAngle(), this);
		}
	}

	protected void rotar() {
		Vector3 mousePosition3D = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
		GameScreen.camara.unproject(mousePosition3D);
		Vector2 mousePosition2D = new Vector2(mousePosition3D.x, mousePosition3D.y);
		Vector2 toTarget = mousePosition2D.sub(body.getPosition().scl(PPM)).nor();
		float angulo = MathUtils.degreesToRadians * toTarget.angle();
		body.setTransform(body.getPosition(), angulo);
	}

	protected void mover(float delta) {
		Vector2 movement = new Vector2(0, 0);

		if (Gdx.input.isKeyPressed(Keys.A)) {
			movement.x--;
		}
		if (Gdx.input.isKeyPressed(Keys.D)) {
			movement.x++;
		}
		if (Gdx.input.isKeyPressed(Keys.W)) {
			movement.y++;
		}
		if (Gdx.input.isKeyPressed(Keys.S)) {
			movement.y--;
		}
		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) {
			movement.scl(1.5f);
		}
		movement.scl(velocidad * delta);
		body.setLinearVelocity(movement);
	}

	public void agregarDinero(int dinero) {
		this.dinero += dinero;
	}

	protected void recargar() {
		if (Gdx.input.isKeyJustPressed(Keys.R)) {
			armas[armaSeleccionada].recargar();
		}
	}

	public boolean quitarDinero(int dinero) {
		if (this.dinero >= dinero) {
			this.dinero -= dinero;
			return true;
		} else {
			return false;
		}
	}

	protected void cambiarArma() {
		if (Gdx.input.isKeyJustPressed(Keys.Q)) {
			armaSeleccionada++;
			if (armaSeleccionada == armas.length)
				armaSeleccionada = 0;
		}
	}

	public void dibujar(SpriteBatch batch, BitmapFont font) {
		font.draw(batch, vidaActual + "/" + vida, (getPosicion().x - getRadio() * 3) * PPM, getPosicion().y * PPM + 30);
	}
}
