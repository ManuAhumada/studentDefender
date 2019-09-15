package com.studentdefender.utils;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.studentdefender.armas.Bala;
import com.studentdefender.personajes.Personaje;

public final class WorldContactListener implements ContactListener {

	public void beginContact(Contact contact) {
		Fixture a = contact.getFixtureA();
		Fixture b = contact.getFixtureB();

		if ((a == null) || (b == null)) {
			return;
		}
		if ((a.getUserData() == null) || (b.getUserData() == null)) {
			return;
		}

		if ((a.getUserData() instanceof Bala) || (b.getUserData() instanceof Bala)) {
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
		}
		if ((a.getUserData() == null) || (b.getUserData() == null)) {
			return;
		}

		if ((a.getUserData() instanceof Bala) || (b.getUserData() instanceof Bala)) {
			Bala bala = (Bala) ((a.getUserData() instanceof Bala) ? a.getUserData() : b.getUserData());
			bala.getBody().setLinearVelocity(0, 0);;
		}
		
		if ((a.getUserData() instanceof Personaje) && (b.getUserData() instanceof Personaje)) {
			Personaje pa = (Personaje) a.getUserData();
			pa.getBody().setLinearVelocity(0, 0);
			Personaje pb = (Personaje) a.getUserData();
			pb.getBody().setLinearVelocity(0, 0);
		}
	}

	public void postSolve(Contact contact, ContactImpulse impulse) {
	}
}
