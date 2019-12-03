package com.studentdefender.personajes;

import static com.studentdefender.utils.Constants.PPM;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;
import com.studentdefender.armas.Arma;
import com.studentdefender.juego.GameScreen;
import com.studentdefender.mejoras.Mejora;
import com.studentdefender.mejoras.Mejoras;
import com.studentdefender.utils.Constants;
import com.studentdefender.utils.Global;

import box2dLight.PointLight;

public class Jugador extends Personaje {
	protected int dinero;
	protected float multiplicadorDinero;
	protected boolean abatido;
	protected boolean muerto;
	protected long momentoAbatido;
	protected Jugador jugadorReviviendo;
	protected long tiempoReviviendo;
	PointLight pointLight;
	protected Arma arma;
	protected Mejora[] mejoras;
	protected long tiempoRevivir = 3000000000L;
	public long maxTiempoAbatido = 30000000000L;
	protected Profesores profesor;

	public Jugador(int x, int y, float radio, Profesores profesor) {
		super(x, y, radio, 100, 300, false);
		pointLight = new PointLight(GameScreen.rayHandler, 100, new Color(1f, 1f, 1f, .65f), 20,
				GameScreen.indexedGraphImp.getNodes().get(0).getPosition().x,
				GameScreen.indexedGraphImp.getNodes().get(0).getPosition().y);
		pointLight.setSoft(false);
		pointLight.attachToBody(body);
		pointLight.setIgnoreAttachedBody(false);
		pointLight.setContactFilter(Constants.BIT_LUZ, (short) 0,
				(short) (Constants.BIT_PARED | Constants.BIT_PUERTA_ENEMIGO));
		reiniciar();
		mejoras = new Mejora[Mejoras.values().length];
		for (int i = 0; i < Mejoras.values().length; i++) {
			mejoras[i] = Mejoras.values()[i].getMejora();
			mejoras[i].setJugador(this);
		}
		multiplicadorDinero = 1;
		this.profesor = profesor;
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

	public void actualizar(float delta, ArrayList<Integer> inputs) {
		rotar(inputs);
		mover(delta, inputs);
		if (!isAbatido()) {
			recargar(inputs);
			atacar(inputs);
			revivir(inputs);
			revisarMejoras(inputs);
		} else {
			revisarAbatimiento();
		}
	}

	private void revisarMejoras(ArrayList<Integer> inputs) {
		for (int i = Keys.NUM_1; i <= Keys.NUM_7; i++) {
			if (inputs.contains(i)) {
				mejoras[i - Keys.NUM_1].seleccionarMejora();
			}
		}
	}

	private void revisarAbatimiento() {
		if (TimeUtils.timeSinceNanos(momentoAbatido) > maxTiempoAbatido) {
			morir();
		}
	}

	protected void atacar(ArrayList<Integer> inputs) {
		if ((arma.isAutomatica() && inputs.contains(Buttons.LEFT))
				|| (!arma.isAutomatica() && inputs.contains(Buttons.LEFT))) {
			arma.atacar(getPosition(), body.getAngle(), this);
		}
	}

	protected void rotar(ArrayList<Integer> inputs) {
		Vector2 mousePosition2D = new Vector2(inputs.get(0), inputs.get(1));
		Vector2 toTarget = mousePosition2D.sub(body.getPosition().scl(PPM)).nor();
		float angulo = vectorToAngle(toTarget);
		body.setTransform(getPosition(), angulo);
	}

	protected void mover(float delta, ArrayList<Integer> inputs) {
		Vector2 movement = new Vector2(0, 0);

		if (inputs.contains(Keys.A)) {
			movement.x--;
		}
		if (inputs.contains(Keys.D)) {
			movement.x++;
		}
		if (inputs.contains(Keys.W)) {
			movement.y++;
		}
		if (inputs.contains(Keys.S)) {
			movement.y--;
		}
		if (!isAbatido()) {
			if (inputs.contains(Keys.SHIFT_LEFT)) {
				movement.scl(1.5f);
			}
		} else {
			movement.scl(.25f);
		}

		movement.scl(velocidad * delta);
		body.setLinearVelocity(movement);
	}

	public void agregarDinero(int dinero) {
		this.dinero += dinero * multiplicadorDinero;
	}

	protected void recargar(ArrayList<Integer> inputs) {
		if (inputs.contains(Keys.R)) {
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

	public void dibujar() {
		super.dibujar();
		if (jugadorReviviendo != null) {
			Global.shapeRenderer.setColor(Color.WHITE);
			Global.shapeRenderer.begin(ShapeType.Filled);
			Global.shapeRenderer.arc(jugadorReviviendo.getPosition().x * PPM,
					jugadorReviviendo.getPosition().y * PPM + 45, 8, 90,
					((float) TimeUtils.timeSinceNanos(tiempoReviviendo) / tiempoRevivir) * 360);
			Global.shapeRenderer.end();
		}
	}

	public void revivir(ArrayList<Integer> inputs) {
		if (inputs.contains(Keys.E)) {
			if (jugadorReviviendo != null) {
				if (getPosition().dst(jugadorReviviendo
						.getPosition()) < (getBoundingRadius() + jugadorReviviendo.getBoundingRadius()) * 2.5) {
					if (TimeUtils.timeSinceNanos(tiempoReviviendo) > tiempoRevivir) {
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

	public void dibujarInterfaz(int posCuadrox) {
		int radioImagen = 30;
		Global.shapeRenderer.begin(ShapeType.Line);
		Global.shapeRenderer.setColor(Color.WHITE);
		Global.shapeRenderer.circle(
				Global.camara.position.x - Global.camara.viewportWidth / 2 + radioImagen + 10 + posCuadrox,
				Global.camara.position.y + Global.camara.viewportHeight / 2 - radioImagen - 10, radioImagen);
		int posMejoraX = 300, ancho = 40;
		for (Mejoras mejora : Mejoras.values()) {
			Global.shapeRenderer.box(Global.camara.position.x - Global.camara.viewportWidth / 2 + posMejoraX,
					Global.camara.position.y - Global.camara.viewportHeight / 2, 0, ancho, ancho, 0);
			posMejoraX += ancho;
		}
		Global.shapeRenderer.end();

		Global.batch.begin();
		posMejoraX = 300;

		for (Mejoras mejora : Mejoras.values()) {
			Global.font.draw(Global.batch, Integer.toString(mejora.ordinal() + 1),
					Global.camara.position.x - Global.camara.viewportWidth / 2 + posMejoraX + 2,
					Global.camara.position.y - Global.camara.viewportHeight / 2 + ancho);
			Global.font.draw(Global.batch, "$" + mejoras[mejora.ordinal()].getPrecio(),
					Global.camara.position.x - Global.camara.viewportWidth / 2 + posMejoraX + 2,
					Global.camara.position.y - Global.camara.viewportHeight / 2 + 12);
			if (mejora.ordinal() != mejoras.length - 1) {
				Global.batch.draw(mejora.getMejora().getIcono(),
						Global.camara.position.x - Global.camara.viewportWidth / 2 + posMejoraX + 8,
						Global.camara.position.y - Global.camara.viewportHeight / 2 + 10, 24, 24);
			} else {
				Global.font.draw(Global.batch, mejoras[mejora.ordinal()].getNombre(),
						Global.camara.position.x - Global.camara.viewportWidth / 2 + posMejoraX + 2,
						Global.camara.position.y - Global.camara.viewportHeight / 2 + 30);
			}
			posMejoraX += ancho;
		}
		Global.batch.draw(profesor.getImagen(),
				Global.camara.position.x - Global.camara.viewportWidth / 2 + radioImagen + 10 + posCuadrox - radioImagen
						+ 5,
				Global.camara.position.y + Global.camara.viewportHeight / 2 - radioImagen - 10 - radioImagen + 10,
				(float) (radioImagen * 1.5), (float) (radioImagen * 1.5));
		Global.font.draw(
				Global.batch, profesor.getNombre(), Global.camara.position.x - Global.camara.viewportWidth / 2
						+ radioImagen + 10 + posCuadrox - radioImagen + 5,
				Global.camara.position.y + Global.camara.viewportHeight / 2 - 70);
		Global.font.draw(Global.batch, "Vida: " + vidaActual + " / " + vida,
				Global.camara.position.x - Global.camara.viewportWidth / 2 + radioImagen * 2 + 10 + posCuadrox + 20,
				Global.camara.position.y + Global.camara.viewportHeight / 2 - 10);
		Global.font.draw(Global.batch, "Arma: " + arma.getMunicionEnArma() + " / " + arma.getTamañoCartucho(),
				Global.camara.position.x - Global.camara.viewportWidth / 2 + radioImagen * 2 + 10 + posCuadrox + 20,
				Global.camara.position.y + Global.camara.viewportHeight / 2 - 30);
		Global.font.draw(Global.batch, "Municion: " + arma.getMunicionTotal(),
				Global.camara.position.x - Global.camara.viewportWidth / 2 + radioImagen * 2 + 10 + posCuadrox + 20,
				Global.camara.position.y + Global.camara.viewportHeight / 2 - 50);
		Global.font.draw(Global.batch, "Plata: $" + this.dinero,
				Global.camara.position.x - Global.camara.viewportWidth / 2 + radioImagen * 2 + 10 + posCuadrox + 20,
				Global.camara.position.y + Global.camara.viewportHeight / 2 - 70);
		Global.batch.end();
	}

	public void setVida(int vida) {
		this.vida = vida;
	}

	public Arma getArma() {
		return arma;
	}

	public long getTiempoRevivir() {
		return tiempoRevivir;
	}

	public void setTiempoRevivir(long tiempoRevivir) {
		this.tiempoRevivir = tiempoRevivir;
	}

	public float getMultiplicadorDinero() {
		return multiplicadorDinero;
	}

	public void setMultiplicadorDinero(float multiplicadorDinero) {
		this.multiplicadorDinero = multiplicadorDinero;
	}

	public Mejora[] getMejoras() {
		return mejoras;
	}

	public int getProfesor() {
		return profesor.ordinal();
	}

	public int getDinero() {
		return dinero;
	}

	public long getMomentoAbatido() {
		return momentoAbatido;
	}

	public long getTiempoReviviendo() {
		return tiempoReviviendo;
	}

	public long getMaxTiempoAbatido() {
		return maxTiempoAbatido;
	}
}
