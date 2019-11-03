package com.studentdefender.armas;

import com.badlogic.gdx.math.Vector2;
import com.studentdefender.personajes.Personaje;

public abstract class Arma {
	protected int daño;
	protected long cadencia;
	protected long ultimaVezUsada;
	protected boolean automatica;
	protected int precio;	
	protected int municionTotal;
	protected int tamañoCartucho;
	protected int municionEnArma;
	
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
	
	public abstract void atacar(Vector2 posicion, float angulo, Personaje atacante);
	
	public abstract void recargar();
	
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
}
