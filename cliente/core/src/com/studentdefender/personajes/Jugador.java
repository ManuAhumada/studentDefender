package com.studentdefender.personajes;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;
import com.studentdefender.armas.Arma;
import com.studentdefender.juego.GameScreen;
import com.studentdefender.mejoras.Mejoras;
import com.studentdefender.objetos_red.JugadorRed;
import com.studentdefender.objetos_red.MejoraRed;
import com.studentdefender.utils.Global;

import box2dLight.PointLight;

public class Jugador extends Personaje {
	private int dinero;
	private boolean abatido;
	private boolean muerto;
	private long momentoAbatido;
	private long tiempoReviviendo;
	private PointLight pointLight;
	private MejoraRed[] mejoras;
	private long tiempoRevivir;
	private long maxTiempoAbatido;
	private Profesores profesor;
	private Arma arma;
	private boolean reviviendo;

	public Jugador(JugadorRed jugador) {
		super(jugador.x, jugador.y, jugador.radio, jugador.vida, jugador.vidaActual, jugador.orientacion);
		pointLight = new PointLight(GameScreen.rayHandler, 100, new Color(1f, 1f, 1f, .65f), 400, jugador.x, jugador.y);
		pointLight.setSoft(false);
		pointLight.setContactFilter(Global.BIT_LUZ, (short) 0, (short) (Global.BIT_PARED));
		this.dinero = jugador.dinero;
		this.abatido = jugador.abatido;
		this.muerto = jugador.muerto;
		this.momentoAbatido = jugador.momentoAbatido;
		this.tiempoReviviendo = jugador.tiempoReviviendo;
		this.mejoras = jugador.mejoras;
		this.tiempoRevivir = jugador.tiempoRevivir;
		this.maxTiempoAbatido = jugador.maxTiempoAbatido;
		this.profesor = Profesores.values()[jugador.personaje];
		this.arma = new Arma(jugador.municionTotal, jugador.tamañoCartucho, jugador.municionEnArma);
		this.reviviendo = jugador.reviviendo;
	}

	public void sendInput() {
		ArrayList<Integer> keys = new ArrayList<>();

		Vector3 mousePosition3D = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
		Global.camara.unproject(mousePosition3D);
		keys.add((int) mousePosition3D.x);
		keys.add((int) mousePosition3D.y);
		if (Gdx.input.isKeyPressed(Keys.A)) {
			keys.add(Keys.A);
		}
		if (Gdx.input.isKeyPressed(Keys.D)) {
			keys.add(Keys.D);
		}
		if (Gdx.input.isKeyPressed(Keys.W)) {
			keys.add(Keys.W);
		}
		if (Gdx.input.isKeyPressed(Keys.S)) {
			keys.add(Keys.S);
		}
		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) {
			keys.add(Keys.SHIFT_LEFT);
		}
		if (Gdx.input.isKeyJustPressed(Keys.R)) {
			keys.add(Keys.R);
		}
		if (Gdx.input.isKeyPressed(Keys.E)) {
			keys.add(Keys.E);
		}
		for (int i = Keys.NUM_1; i <= Keys.NUM_6; i++) {
			if (Gdx.input.isKeyJustPressed(i)) {
				keys.add(i);
			}
		}
		if (Gdx.input.isButtonPressed(Buttons.LEFT) || Gdx.input.isButtonJustPressed(Buttons.LEFT)) {
			keys.add(Buttons.LEFT);
		}
		Global.servidor.enviarMensaje(keys);
	}

	public void dibujar() {
		if (!muerto) {
			super.dibujar();
			Global.batch.begin();
			profesor.getPersonaje().setRotation(orientacion);
			profesor.getPersonaje().setPosition(posicion.x - radio, posicion.y - radio);
			profesor.getPersonaje().draw(Global.batch);
			Global.batch.end();
			if (reviviendo) {
				Global.shapeRenderer.setColor(Color.WHITE);
				Global.shapeRenderer.begin(ShapeType.Filled);
				Global.shapeRenderer.arc(this.posicion.x, this.posicion.y + 45, 8, 90,
						((float) TimeUtils.timeSinceNanos(tiempoReviviendo) / tiempoRevivir) * 360);
				Global.shapeRenderer.end();
			}
		}
	}

	public void dibujarInterfaz(int posCuadrox) {
		int radioImagen = 30;
		Global.batch.begin();
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

	public void dibujarMejoras() {
		int posMejoraX = 300, ancho = 40;
		Global.shapeRenderer.begin(ShapeType.Line);
		Global.shapeRenderer.setColor(Color.WHITE);
		for (Mejoras mejora : Mejoras.values()) {
			Global.shapeRenderer.box(Global.camara.position.x - Global.camara.viewportWidth / 2 + posMejoraX,
					Global.camara.position.y - Global.camara.viewportHeight / 2, 0, ancho, ancho, 0);
			posMejoraX += ancho;
		}
		Global.shapeRenderer.end();
		posMejoraX = 300;
		Global.batch.begin();
		for (Mejoras mejora : Mejoras.values()) {
			Global.font.draw(Global.batch, Integer.toString(mejora.ordinal() + 1),
					Global.camara.position.x - Global.camara.viewportWidth / 2 + posMejoraX + 2,
					Global.camara.position.y - Global.camara.viewportHeight / 2 + ancho);
			Global.font.draw(Global.batch,
					mejoras[mejora.ordinal()].nivelActual == mejoras[mejora.ordinal()].nivelMaximo ? "MAX"
							: "$" + mejoras[mejora.ordinal()].precio,
					Global.camara.position.x - Global.camara.viewportWidth / 2 + posMejoraX + 2,
					Global.camara.position.y - Global.camara.viewportHeight / 2 + 12);
			Global.batch.draw(mejora.getMejora().getIcono(),
					Global.camara.position.x - Global.camara.viewportWidth / 2 + posMejoraX + 8,
					Global.camara.position.y - Global.camara.viewportHeight / 2 + 10, 24, 24);

			posMejoraX += ancho;
		}
		Global.batch.end();
	}

	public void actualizar(JugadorRed jugador) {
		pointLight.setPosition(jugador.x, jugador.y);
		this.vida = jugador.vida;
		this.vidaActual = jugador.vidaActual;
		this.posicion.x = jugador.x;
		this.posicion.y = jugador.y;
		this.orientacion = jugador.orientacion;
		this.dinero = jugador.dinero;
		this.abatido = jugador.abatido;
		this.muerto = jugador.muerto;
		this.momentoAbatido = jugador.momentoAbatido;
		this.tiempoReviviendo = jugador.tiempoReviviendo;
		this.mejoras = jugador.mejoras;
		this.tiempoRevivir = jugador.tiempoRevivir;
		this.arma = new Arma(jugador.municionTotal, jugador.tamañoCartucho, jugador.municionEnArma);
		this.reviviendo = jugador.reviviendo;
	}
}
