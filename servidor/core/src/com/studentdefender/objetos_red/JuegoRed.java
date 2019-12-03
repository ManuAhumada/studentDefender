package com.studentdefender.objetos_red;

import java.io.Serializable;

public class JuegoRed implements Serializable {

    private static final long serialVersionUID = 8346421763393525344L;
    public CuerpoRed[] balas;
    public PersonajeRed[] enemigos;
    public JugadorRed[] jugadores;
    public int ronda;
}