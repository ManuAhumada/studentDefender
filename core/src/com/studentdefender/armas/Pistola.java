package com.studentdefender.armas;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.studentdefender.juego.GameScreen;
import com.studentdefender.personajes.Personaje;

public class Pistola extends Arma {
	

	public Pistola(long cadencia, int daño, boolean automatica, int precio, int municionTotal, int tamañoCartucho,
			int municionEnArma) {
		super(cadencia, daño, automatica, precio, municionTotal, tamañoCartucho, municionEnArma);
	}

	public Pistola() {
		super(900000000, 10, false, 100, 40, 6, 6);
	}

	public void atacar(Vector2 posicion, float angulo, Personaje atacante) {
		if ((TimeUtils.timeSinceNanos(ultimaVezUsada) > cadencia)) {
			if (this.municionEnArma != 0) {
				this.municionEnArma--;
				this.ultimaVezUsada = TimeUtils.nanoTime();
				GameScreen.balaPool.obtain().init(posicion, angulo, daño, atacante);
			}
		}

	}

	public void recargar() {
		if (municionTotal != 0) {
			if (municionTotal > tamañoCartucho - municionEnArma) {
				municionTotal -= tamañoCartucho - municionEnArma;
				municionEnArma = tamañoCartucho;
			} else {
				municionEnArma = municionTotal;
				municionTotal = 0;
			}
		}
	}

	public int getMunicionEnArma() {
		return municionEnArma;
	}
}
