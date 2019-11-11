package com.studentdefender.armas;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.studentdefender.juego.GameScreen;
import com.studentdefender.personajes.Jugador;

public class Arma {
	protected int daño;
	protected long cadencia;
	protected long ultimaVezUsada;
	protected boolean automatica;
	protected int precio;	
	protected int municionTotal;
	protected int tamañoCartucho;
	protected int municionEnArma;
	
	public Arma() {
		this(900000000, 10, false, 100, 40, 6, 6);
	}

	public Arma(long cadencia, int daño, boolean automatica, int precio, int municionTotal, int tamañoCartucho,
	int municionEnArma) {
		this.daño = daño;
		this.cadencia = cadencia;
		this.ultimaVezUsada = 0;
		this.automatica = automatica;
		this.precio = precio;		
		this.municionTotal = municionTotal;
		this.tamañoCartucho = tamañoCartucho;
		this.municionEnArma = municionEnArma;
	}
	
	public void atacar(Vector2 posicion, float angulo, Jugador atacante) {
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
	
	public boolean isAutomatica() {
		return automatica;
	}

	public int getMunicionTotal() {
		return municionTotal;
	}

	public int getTamañoCartucho() {
		return tamañoCartucho;
	}

	public int getMunicionEnArma() {
		return municionEnArma;
	}

	public void agregarBalas(int balas) {
		municionTotal += balas;
	}
}
