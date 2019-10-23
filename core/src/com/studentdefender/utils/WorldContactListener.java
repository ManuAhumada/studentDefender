package com.studentdefender.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.studentdefender.armas.Bala;
import com.studentdefender.personajes.Enemigo;
import com.studentdefender.personajes.Jugador;

public final class WorldContactListener implements ContactListener {

	public void beginContact(Contact contact) {
		Fixture a = contact.getFixtureA();
		Fixture b = contact.getFixtureB();

		if ((a == null) || (b == null)) {
			return;
		} else if ((a.getUserData() == null) || (b.getUserData() == null)) {
			return;
		} else if (a.getUserData() instanceof Bala || b.getUserData() instanceof Bala) {
			Bala bala = (Bala) ((a.getUserData() instanceof Bala) ? a.getUserData() : b.getUserData());
			bala.impactar((!(a.getUserData() instanceof Bala)) ? a.getUserData() : b.getUserData());
		}
	}

	public void endContact(Contact contact) {
	}

	public void preSolve(Contact contact, Manifold oldManifold) {
		Fixture a = contact.getFixtureA();
		Fixture b = contact.getFixtureB();

		if ((a == null) || (b == null)) {
			return;
		} else if ((a.getUserData() == null) || (b.getUserData() == null)) {
			return;
		} else if ((a.getUserData() instanceof Bala) || (b.getUserData() instanceof Bala)) {
			Bala bala = (Bala) ((a.getUserData() instanceof Bala) ? a.getUserData() : b.getUserData());
			bala.getBody().setLinearVelocity(0, 0);
		} else if (a.getUserData() instanceof Enemigo && b.getUserData() instanceof Jugador
				|| a.getUserData() instanceof Jugador && b.getUserData() instanceof Enemigo) {
			Jugador jugador = (Jugador) ((a.getUserData() instanceof Jugador) ? a.getUserData() : b.getUserData());
			Enemigo enemigo = (Enemigo) ((a.getUserData() instanceof Enemigo) ? a.getUserData() : b.getUserData());
			enemigo.atacar(jugador);
		}
	}

	public void postSolve(Contact contact, ContactImpulse impulse) {
	}
}
