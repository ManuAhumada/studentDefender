package com.studentdefender.mejoras;

import com.badlogic.gdx.graphics.Texture;

public class MejoraVida extends Mejora {
    public MejoraVida() {
        this.nombre = "Vida";
        this.precio = 100;
        this.nivelActual = 0;
        this.nivelMaximo = 10;
        this.porcentajeMejora = 1.10f;
        this.icono = new Texture("imagenes/vida.png");
    };
}