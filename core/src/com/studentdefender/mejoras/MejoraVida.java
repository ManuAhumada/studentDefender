package com.studentdefender.mejoras;

import com.badlogic.gdx.Gdx;
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

    public void subirNivel() {
        jugador.setVida((int) (jugador.getVida() * porcentajeMejora));
        Gdx.app.log("Mejora", "Mejora " + nombre + " aplicada");
    }
}