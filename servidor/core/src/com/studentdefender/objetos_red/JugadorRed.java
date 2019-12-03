package com.studentdefender.objetos_red;

import java.io.Serializable;

public class JugadorRed extends PersonajeRed implements Serializable {

    private static final long serialVersionUID = -5808019027198373814L;
    public int profesor;
    public int municionTotal;
    public int tamañoCartucho;
    public int municionEnArma;
    public MejoraRed[] mejoras;
    public int dinero;
    public boolean abatido;
    public boolean muerto;
    public long momentoAbatido;
    public long tiempoReviviendo;
    public long tiempoRevivir;
    public long maxTiempoAbatido;
    public boolean reviviendo;
}
