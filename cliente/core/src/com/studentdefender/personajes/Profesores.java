package com.studentdefender.personajes;

import com.badlogic.gdx.graphics.Texture;

public enum Profesores {
    SALGADO("Salgado", new Texture("imagenes/salgado.png")), MUSCIO("Muscio", new Texture("imagenes/muscio.png")),
    ALAN("Alan", new Texture("imagenes/alan2.png")), JASINSKI("Jasinski", new Texture("imagenes/jasinski.png")),
    ZARDELLINI("Zardellini", new Texture("imagenes/zardellini.png"));

    private final Texture imagen;
    private final String nombre;

    private Profesores(String nombre, Texture imagen) {
        this.imagen = imagen;
        this.nombre = nombre;
    }

    public Texture getImagen() {
        return imagen;
    }

    public String getNombre() {
        return nombre;
    }
}