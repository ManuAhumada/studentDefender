package com.studentdefender.armas;

public class Arma {
	protected int municionTotal;
	protected int tamañoCartucho;
	protected int municionEnArma;

	public Arma(int municionTotal, int tamañoCartucho, int municionEnArma) {
		this.municionEnArma = municionEnArma;
		this.municionTotal = municionTotal;
		this.tamañoCartucho = tamañoCartucho;
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
