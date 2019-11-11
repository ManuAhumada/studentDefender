package com.studentdefender.personajes;

import static com.studentdefender.utils.Constants.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;
import com.studentdefender.armas.Arma;
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
	protected Arma arma;

	public static final long TIEMPO_REVIVIR = 3000000000L;
	public static final long MAX_TIEMPO_ABATIDO = 30000000000L;

	public Jugador(int x, int y, float radio) {
		super(x, y, radio, 100, 300, false);
		pointLight = new PointLight(GameScreen.rayHandler, 100, new Color(1f, 1f, 1f, .65f), 20,
				GameScreen.indexedGraphImp.getNodes().get(0).getPosition().x,
				GameScreen.indexedGraphImp.getNodes().get(0).getPosition().y);
		pointLight.setSoft(false);
		pointLight.attachToBody(body);
		pointLight.setIgnoreAttachedBody(false);
		pointLight.setContactFilter(Constants.BIT_LUZ, (short) 0, (short) (Constants.BIT_PARED | Constants.BIT_PUERTA_ENEMIGO));
		reiniciar();
	}

	public void reiniciar() {
		arma = new Arma(500000000, 10, false, 100, 100, 10, 10);
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
		if ((arma.isAutomatica() && Gdx.input.isButtonPressed(Buttons.LEFT))
				|| (!arma.isAutomatica() && Gdx.input.isButtonJustPressed(Buttons.LEFT))) {
			arma.atacar(getPosition(), body.getAngle(), this);
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
			arma.recargar();
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

	public void quitarVida(int vidaQuitada, Personaje atacante) {
		super.quitarVida(vidaQuitada, atacante);
		if (vidaActual == 0 && !abatido) {
			momentoAbatido = TimeUtils.nanoTime();
			abatido = true;
		}
	}

	public void dibujar(SpriteBatch batch, BitmapFont font) {
		super.dibujar(batch, font);
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

	public void agregarBalas(int balas) {
		arma.agregarBalas(balas);
	}

	public void dibujarInterfaz(OrthographicCamera camara, SpriteBatch batch, BitmapFont font, ShapeRenderer shapeRenderer, int posCuadrox) {
		int radioImagen = 30;
		GameScreen.shapeRenderer.begin(ShapeType.Line);
		GameScreen.shapeRenderer.setColor(Color.WHITE);
		GameScreen.shapeRenderer.circle(GameScreen.camara.position.x - GameScreen.camara.viewportWidth/2 + radioImagen + 10 + posCuadrox, GameScreen.camara.position.y + GameScreen.camara.viewportHeight/2 - radioImagen - 10, radioImagen);
		GameScreen.shapeRenderer.end();
		batch.begin();
		font.draw(batch, "Vida: " + vidaActual + " / " + vida, GameScreen.camara.position.x - GameScreen.camara.viewportWidth/2 + radioImagen * 2 + 10 + posCuadrox + 20, GameScreen.camara.position.y + GameScreen.camara.viewportHeight/2 - 10);
		font.draw(batch, "Arma: " + arma.getMunicionEnArma() + " / " + arma.getTamañoCartucho(), GameScreen.camara.position.x - GameScreen.camara.viewportWidth/2 + radioImagen * 2 + 10 + posCuadrox + 20, GameScreen.camara.position.y + GameScreen.camara.viewportHeight/2 - 30);
		font.draw(batch, "Municion: " + arma.getMunicionTotal(), GameScreen.camara.position.x - GameScreen.camara.viewportWidth/2 + radioImagen * 2 + 10 + posCuadrox + 20, GameScreen.camara.position.y + GameScreen.camara.viewportHeight/2 - 50);
		font.draw(batch, "Plata: $" + this.dinero, GameScreen.camara.position.x - GameScreen.camara.viewportWidth/2 + radioImagen * 2 + 10 + posCuadrox + 20, GameScreen.camara.position.y + GameScreen.camara.viewportHeight/2 - 70);
		batch.end();
	}
}
