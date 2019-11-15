package com.studentdefender.mejoras;

import com.badlogic.gdx.graphics.Texture;

public class MejoraVelocidad extends Mejora {
    public MejoraVelocidad() {
        this.nombre = "Velocidad";
        this.precio = 100;
        this.nivelActual = 0;
        this.nivelMaximo = 10;
        this.porcentajeMejora = .8f;
        this.icono = new Texture("imagenes/velocidad.png");
    };
}