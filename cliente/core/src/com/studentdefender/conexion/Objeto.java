package com.studentdefender.conexion;

import java.io.Serializable;

public class Objeto extends Mensaje implements Serializable {

	private static final long serialVersionUID = -1670224174042569283L;
	public String nombre;
	public int dinero;
	public boolean activado;

	public Objeto(String mensaje, String nombre, int dinero, boolean activado) {
		super(mensaje);
		this.nombre = nombre;
		this.dinero = dinero;
		this.activado = activado;
	}
}
