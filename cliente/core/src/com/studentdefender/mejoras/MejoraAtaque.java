package com.studentdefender.mejoras;

import com.badlogic.gdx.graphics.Texture;

public class MejoraAtaque extends Mejora {
    public MejoraAtaque() {
        this.nombre = "Ataque";
        this.precio = 100;
        this.nivelActual = 0;
        this.nivelMaximo = 10;
        this.porcentajeMejora = 1.2f;
        this.icono = new Texture("imagenes/ataque.png");
    };
}