package com.studentdefender.mejoras;

import com.badlogic.gdx.Gdx;
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

    public void seleccionarMejora() {
        if (nivelActual == nivelMaximo) {
            return;
        }
        if (!jugador.quitarDinero(precio)) {
            Gdx.app.log("Mejora", "Dinero insuficiente para " + nombre);
            return;
        }
        nivelActual++;
        precio *= 1.5;
        subirNivel();
    }

    public abstract void subirNivel();

    public String getNombre() {
        return nombre;
    }

    public int getPrecio() {
        return precio;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public Texture getIcono() {
        return icono;
    }

    public int getNivelMaximo() {
        return nivelMaximo;
    }

    public int getNivelActual() {
        return nivelActual;
    }
}