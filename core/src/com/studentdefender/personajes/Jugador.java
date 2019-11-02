package com.studentdefender.personajes;

import static com.studentdefender.utils.Constants.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;
import com.studentdefender.armas.Arma;
import com.studentdefender.armas.Pistola;
import com.studentdefender.juego.GameScreen;
import com.studentdefender.utils.Constants;

import box2dLight.PointLight;

public class Jugador extends Personaje {
	protected int dinero;
	protected boolean abatido;
	protected boolean muerto;
	protected long momentoAbatido;
	protected Jugador jugadorReviviendo;
	protected long tiempoReviviendo;
	PointLight pointLight;

	public static final long TIEMPO_REVIVIR = 3000000000L;
	public static final long MAX_TIEMPO_ABATIDO = 30000000000L;

	public Jugador(int x, int y, float radio) {
		super(x, y, radio, 100, 100, false);
		pointLight = new PointLight(GameScreen.rayHandler, 100, new Color(1f, 1f, 1f, .65f), 10,
				GameScreen.indexedGraphImp.getNodes().get(0).getPosition().x,
				GameScreen.indexedGraphImp.getNodes().get(0).getPosition().y);
		pointLight.setSoft(false);
		pointLight.attachToBody(body);
		pointLight.setIgnoreAttachedBody(false);
		pointLight.setContactFilter(Constants.BIT_LUZ, (short) 0, (short) (Constants.BIT_PARED | Constants.BIT_PUERTA_ENEMIGO));
		reiniciar();
	}

	public void reiniciar() {
		armas = new Arma[2];
		armas[0] = new Pistola(250000000, 10, true, 100, 100, 10, 10);
		armas[1] = new Pistola(500000000, 20, true, 100, 100, 10, 10);
		dinero = 0;
		abatido = false;
		muerto = false;
		vidaActual = vida;
		momentoAbatido = 0;
		body.setActive(true);
		jugadorReviviendo = null;
		tiempoReviviendo = 0;
		pointLight.setActive(true);
	}

	public void actualizar(float delta) {
		rotar();
		mover(delta);
		if (!isAbatido()) {
			recargar();
			atacar();
			cambiarArma();
			revivir();
		} else {
			revisarAbatimiento();
		}
	}

	private void revisarAbatimiento() {
		if (TimeUtils.timeSinceNanos(momentoAbatido) > MAX_TIEMPO_ABATIDO) {
			morir();
		}
	}

	protected void atacar() {
		if ((armas[armaSeleccionada].isAutomatica() && Gdx.input.isButtonPressed(Buttons.LEFT))
				|| (!armas[armaSeleccionada].isAutomatica() && Gdx.input.isButtonJustPressed(Buttons.LEFT))) {
			armas[armaSeleccionada].atacar(getPosition(), body.getAngle(), this);
		}
	}

	protected void rotar() {
		Vector3 mousePosition3D = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
		GameScreen.camara.unproject(mousePosition3D);
		Vector2 mousePosition2D = new Vector2(mousePosition3D.x, mousePosition3D.y);
		Vector2 toTarget = mousePosition2D.sub(body.getPosition().scl(PPM)).nor();
		float angulo = vectorToAngle(toTarget);
		body.setTransform(getPosition(), angulo);
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
		if (!isAbatido()) {
			if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) {
				movement.scl(1.5f);
			}
		} else {
			movement.scl(.25f);
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

	public void quitarVida(int vidaQuitada) {
		super.quitarVida(vidaQuitada);
		if (vidaActual == 0 && !abatido) {
			momentoAbatido = TimeUtils.nanoTime();
			abatido = true;
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
		super.dibujar(batch, font);
		batch.begin();
		if (!isAbatido()) {
			font.draw(batch, vidaActual + "/" + vida, (getPosition().x - getBoundingRadius() * 3) * PPM,
					getPosition().y * PPM + 30);
		} else {
			font.draw(batch, "ABATIDO", (getPosition().x - getBoundingRadius() * 3) * PPM, getPosition().y * PPM + 30);
		}
		batch.end();
		if (jugadorReviviendo != null) {
			GameScreen.shapeRenderer.setColor(Color.WHITE);
			GameScreen.shapeRenderer.begin(ShapeType.Filled);
			GameScreen.shapeRenderer.arc(jugadorReviviendo.getPosition().x * PPM,
					jugadorReviviendo.getPosition().y * PPM + 45, 8, 90,
					((float) TimeUtils.timeSinceNanos(tiempoReviviendo) / TIEMPO_REVIVIR) * 360);
			GameScreen.shapeRenderer.end();
		}
	}

	public void revivir() {
		if (Gdx.input.isKeyPressed(Keys.E)) {
			if (jugadorReviviendo != null) {
				if (getPosition().dst(jugadorReviviendo
						.getPosition()) < (getBoundingRadius() + jugadorReviviendo.getBoundingRadius()) * 2.5) {
					if (TimeUtils.timeSinceNanos(tiempoReviviendo) > TIEMPO_REVIVIR) {
						jugadorReviviendo.agregarVida(50);
						jugadorReviviendo.abatido = false;
						jugadorReviviendo = null;
					}
				} else {
					jugadorReviviendo = null;
				}
			} else {
				Jugador jugador;
				for (int i = 0; i < GameScreen.jugadores.size; i++) {
					jugador = GameScreen.jugadores.get(i);
					if (!jugador.equals(this) && !jugador.isMuerto() && jugador.isAbatido() && getPosition()
							.dst(jugador.getPosition()) < (getBoundingRadius() + jugador.getBoundingRadius()) * 2.5) {
						jugadorReviviendo = jugador;
						tiempoReviviendo = TimeUtils.nanoTime();
					}
				}
			}
		} else {
			jugadorReviviendo = null;
		}
	}

	public boolean isMuerto() {
		return muerto;
	}

	public boolean isAbatido() {
		return abatido;
	}

	private void morir() {
		muerto = true;
		pointLight.setActive(false);
	}
}
