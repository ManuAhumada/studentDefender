package com.studentdefender.conexion;

import java.io.Serializable;

public class SeleccionJugador implements Serializable {

    private static final long serialVersionUID = -7214805021434214422L;
    public int personajeSeleccionado;
    public boolean preparado;

    public SeleccionJugador() {
        personajeSeleccionado = 0;
        preparado = false;
    }
}