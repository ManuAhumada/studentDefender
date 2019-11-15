package com.studentdefender.armas;

public class Arma {
	protected int daño;
	protected long cadencia;
	protected boolean automatica;
	protected int precio;
	protected int municionTotal;
	protected int tamañoCartucho;
	protected int municionEnArma;

	public int getMunicionTotal() {
		return municionTotal;
	}

	public int getTamañoCartucho() {
		return tamañoCartucho;
	}

	public int getMunicionEnArma() {
		return municionEnArma;
	}

	public int getDaño() {
		return daño;
	}

	public long getCadencia() {
		return cadencia;
	}
}
