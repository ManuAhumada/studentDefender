package com.studentdefender.mejoras;

import com.badlogic.gdx.graphics.Texture;
import com.studentdefender.personajes.Jugador;

public abstract class Mejora {
    String nombre;
    int nivelMaximo;
    int nivelActual;
    Jugador jugador;
    int precio;
    float porcentajeMejora;
    Texture icono;

    public String getNombre() {
        return nombre;
    }

    public int getPrecio() {
        return precio;
    }

    public Texture getIcono() {
        return icono;
    }
}