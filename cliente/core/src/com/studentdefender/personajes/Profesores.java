package com.studentdefender.personajes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public enum Profesores {
    SALGADO("Salgado", new Sprite(new Texture("imagenes/salgadoPersonaje.png")), new Texture("imagenes/salgado.png")),
    MUSCIO("Muscio", new Sprite(new Texture("imagenes/muscioPersonaje.png")), new Texture("imagenes/muscio.png")),
    ALAN("Alan", new Sprite(new Texture("imagenes/alanPersonaje.png")), new Texture("imagenes/alan2.png")),
    JASINSKI("Jasinski", new Sprite(new Texture("imagenes/jasinskyPersonaje.png")),
            new Texture("imagenes/jasinski.png")),
    SARDELLINI("Sardellini", new Sprite(new Texture("imagenes/sardelliniPersonaje.png")),
            new Texture("imagenes/sardellini.png"));

    private final Texture imagen;
    private final Sprite personaje;
    private final String nombre;

    private Profesores(String nombre, Sprite personaje, Texture imagen) {
        this.imagen = imagen;
        this.personaje = personaje;
        this.nombre = nombre;
    }

    public Texture getImagen() {
        return imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public Sprite getPersonaje() {
        return personaje;
    }
}