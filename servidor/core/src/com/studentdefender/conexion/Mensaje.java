package com.studentdefender.conexion;

import java.io.Serializable;

public class Mensaje implements Serializable {

	private static final long serialVersionUID = 6386823121022759770L;
	public int jugador;
	public Object mensaje;

	public Mensaje(int jugador, Object mensaje) {
		this.jugador = jugador;
		this.mensaje = mensaje;
	}

	public Mensaje(Object mensaje) {
		this.mensaje = mensaje;
	}
}
