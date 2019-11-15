package com.studentdefender.mejoras;

public class MejoraAutomatica extends Mejora {
    public MejoraAutomatica() {
        this.nombre = "Automatica";
        this.precio = 1000;
        this.nivelActual = 0;
        this.nivelMaximo = 1;
    };

    public void subirNivel() {
        jugador.getArma().setAutomatica(true);
    }
}