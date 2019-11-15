package com.studentdefender.mejoras;

import com.badlogic.gdx.graphics.Texture;

public class MejoraDinero extends Mejora {
    public MejoraDinero() {
        this.nombre = "Dinero";
        this.precio = 100;
        this.nivelActual = 0;
        this.nivelMaximo = 10;
        this.porcentajeMejora = 1.5f;
        this.icono = new Texture("imagenes/dinero.png");
    };

    public void subirNivel() {
        jugador.setMultiplicadorDinero(jugador.getMultiplicadorDinero() * porcentajeMejora);
    }
}