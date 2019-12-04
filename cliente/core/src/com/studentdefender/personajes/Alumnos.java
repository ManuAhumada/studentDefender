package com.studentdefender.personajes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public enum Alumnos {
    ALUMNO1(new Sprite(new Texture("imagenes/alumno1.png"))), ALUMNO2(new Sprite(new Texture("imagenes/alumno2.png")));

    private final Sprite personaje;

    private Alumnos(Sprite personaje) {
        this.personaje = personaje;
    }

    public Sprite getPersonaje() {
        return personaje;
    }
}