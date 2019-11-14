package com.studentdefender.mejoras;

public enum Mejoras {
    VIDA(new MejoraVida()),
    ATAQUE(new MejoraAtaque()),
    VELOCIDAD(new MejoraVelocidad()),
    MUNICION(new MejoraMunicion()),
    DINERO(new MejoraDinero()),
    REVIVIR(new MejoraRevivir()),
    AUTOMATICA(new MejoraAutomatica());

    private final Mejora mejora;

    Mejoras(Mejora mejora) {
        this.mejora = mejora;
    }

    public Mejora getMejora() {
        return mejora;
    }
}



