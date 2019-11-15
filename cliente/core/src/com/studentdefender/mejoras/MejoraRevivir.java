package com.studentdefender.mejoras;

import com.badlogic.gdx.graphics.Texture;

public class MejoraRevivir extends Mejora {
    public MejoraRevivir() {
        this.nombre = "Revivir";
        this.precio = 100;
        this.nivelActual = 0;
        this.nivelMaximo = 10;
        this.porcentajeMejora = .8f;
        this.icono = new Texture("imagenes/revivir.png");
    };
}