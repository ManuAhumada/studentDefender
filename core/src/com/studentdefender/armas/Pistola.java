package com.studentdefender.armas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.studentdefender.juego.GameScreen;
import com.studentdefender.personajes.Personaje;

public class Pistola extends Arma {
	protected int municionTotal;
	protected int tama�oCartucho;
	protected int municionEnArma;
	
	public Pistola(long cadencia, int da�o, boolean automatica, int precio, int municionTotal,
			int tama�oCartucho, int municionEnArma) {
		super(cadencia, da�o, automatica, precio);
		this.municionTotal = municionTotal;
		this.tama�oCartucho = tama�oCartucho;
		this.municionEnArma = municionEnArma;
	}
	
	public Pistola() {
		super(250000000, 10, false, 100);
		this.municionTotal = 40;
		this.tama�oCartucho = 6;
		this.municionEnArma = 6;
	}
	
	public void atacar(Vector2 posicion, float angulo, Personaje atacante) {
		if ((TimeUtils.nanoTime() - ultimaVezUsada > cadencia)) {
			if (this.municionEnArma != 0) {
//				Gdx.app.log("Arma", "Bala disparada");
				this.municionEnArma--;
				this.ultimaVezUsada = TimeUtils.nanoTime();
				GameScreen.balaPool.obtain().init(posicion, angulo, da�o, atacante);
			} else {
//				Gdx.app.log("Arma", "No tienes balas");
			}
		} else {
//			Gdx.app.log("Arma", "No ha pasado el tiempo suficiente para poder usarla");
		}
		
	}
	
	public void recargar() {
		if (municionTotal != 0) {
			if (municionTotal > tama�oCartucho - municionEnArma) {
				municionTotal -= tama�oCartucho - municionEnArma;
				municionEnArma = tama�oCartucho;
			} else {
				municionEnArma = municionTotal;
				municionTotal = 0;
			}
			Gdx.app.log("Arma", "Se han cargado las balas.");
			Gdx.app.log("Arma", "Tienes " + municionEnArma + " balas en el arma y te quedan " + municionTotal + " balas.");
		} else {
			Gdx.app.log("Arma", "No tiene municion.");
		}
	}
	
}
