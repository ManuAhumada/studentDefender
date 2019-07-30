package com.studentdefender.armas;

import com.badlogic.gdx.math.Vector2;
import com.studentdefender.personajes.Personaje;

public abstract class Arma {
	protected int daño;
	protected long cadencia;
	protected long ultimaVezUsada;
	protected boolean automatica;
	protected int precio;	
	
	public Arma(long cadencia, int daño, long ultimaVezUsada, boolean automatica, int precio) {
		this.daño = daño;
		this.cadencia = cadencia;
		this.ultimaVezUsada = ultimaVezUsada;
		this.automatica = automatica;
		this.precio = precio;		
	}
	
	public abstract void atacar(Vector2 posicion, float angulo, Personaje atacante);
	
	public abstract void recargar();
	
	public boolean isAutomatica() {
		return automatica;
	}
}
