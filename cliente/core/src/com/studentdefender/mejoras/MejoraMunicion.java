package com.studentdefender.mejoras;

import com.badlogic.gdx.graphics.Texture;

public class MejoraMunicion extends Mejora {
    public MejoraMunicion() {
        this.nombre = "Municion";
        this.precio = 100;
        this.nivelActual = 0;
        this.nivelMaximo = 10;
        this.porcentajeMejora = 1.25f;
        this.icono = new Texture("imagenes/municion.png");
    };
}