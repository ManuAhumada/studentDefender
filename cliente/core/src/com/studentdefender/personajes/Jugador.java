package com.studentdefender.personajes;

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
import com.studentdefender.utils.Global;

import box2dLight.PointLight;

public class Jugador extends Personaje {
	private int dinero;
	private boolean abatido;
	private boolean muerto;
	private Jugador jugadorReviviendo;
	private int momentoAbatido;
	private long tiempoReviviendo;
	private PointLight pointLight;
	private Arma arma;
	private Mejora[] mejoras;
	private long tiempoRevivir = 3000000000L;
	private long MAX_TIEMPO_ABATIDO = 30000000000L;
	private Profesores profesor;

	public Jugador(int x, int y, float radio, Profesores profesor) {
		super(x, y, radio, 100, 300, false);
		pointLight = new PointLight(GameScreen.rayHandler, 100, new Color(1f, 1f, 1f, .65f), 20, x, y);
		pointLight.setSoft(false);
		pointLight.attachToBody(body);
		pointLight.setIgnoreAttachedBody(false);
		pointLight.setContactFilter(Constants.BIT_LUZ, (short) 0,
				(short) (Constants.BIT_PARED | Constants.BIT_PUERTA_ENEMIGO));
		mejoras = new Mejora[Mejoras.values().length];
		this.profesor = profesor;
	}

	public void actualizar(float delta) {
		rotar();
		mover(delta);
		if (!abatido) {
			recargar();
			atacar();
			revivir();
			revisarMejoras();
		}
	}

	// TODO
	private void revisarMejoras() {
		for (int i = Keys.NUM_1; i <= Keys.NUM_7; i++) {
			if (Gdx.input.isKeyJustPressed(i)) {

			}
		}
	}

	// TODO
	protected void atacar() {
		if (Gdx.input.isButtonPressed(Buttons.LEFT) || Gdx.input.isButtonJustPressed(Buttons.LEFT)) {

		}
	}

	// TODO
	protected void rotar() {
		Vector3 mousePosition3D = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
		Global.camara.unproject(mousePosition3D);
		Vector2 mousePosition2D = new Vector2(mousePosition3D.x, mousePosition3D.y);
	}

	// TODO
	protected void mover(float delta) {
		if (Gdx.input.isKeyPressed(Keys.A)) {

		}
		if (Gdx.input.isKeyPressed(Keys.D)) {

		}
		if (Gdx.input.isKeyPressed(Keys.W)) {

		}
		if (Gdx.input.isKeyPressed(Keys.S)) {

		}
		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) {

		}
	}

	// TODO
	protected void recargar() {
		if (Gdx.input.isKeyJustPressed(Keys.R)) {

		}
	}

	public void dibujar() {
		super.dibujar();
		if (jugadorReviviendo != null) {
			Global.shapeRenderer.setColor(Color.WHITE);
			Global.shapeRenderer.begin(ShapeType.Filled);
			Global.shapeRenderer.arc(jugadorReviviendo.posicion.x * PPM, jugadorReviviendo.posicion.y * PPM + 45, 8, 90,
					((float) TimeUtils.timeSinceNanos(tiempoReviviendo) / tiempoRevivir) * 360);
			Global.shapeRenderer.end();
		}
	}

	// TODO
	public void revivir() {
		if (Gdx.input.isKeyPressed(Keys.E)) {

		}
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
}
