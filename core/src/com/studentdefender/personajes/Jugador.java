package com.studentdefender.personajes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.studentdefender.armas.Pistola;
import static com.studentdefender.utils.Constants.PPM;

public class Jugador extends Personaje {
	protected int dinero;
	
	public Jugador(int x, int y, float radio) {
		super(x, y, radio, 100, 10, 200);
		armas[0] = new Pistola();
		dinero = 0;
	}

	protected void atacar() {
		if ((armas[armaSeleccionada].isAutomatica() && Gdx.input.isKeyPressed(Keys.E))
				|| (!armas[armaSeleccionada].isAutomatica() && Gdx.input.isKeyJustPressed(Keys.E))) {
			Gdx.app.log("Personaje", "Atacando");
			armas[armaSeleccionada].atacar(getPosicion(), body.getAngle(), this);
		}	
	}
	
	protected void rotar(OrthographicCamera camara) {
		Vector3 mousePosition3D = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
		camara.unproject(mousePosition3D);
		Vector2 mousePosition2D =  new Vector2(mousePosition3D.x, mousePosition3D.y);
		Vector2 toTarget = mousePosition2D.sub(body.getPosition().scl(PPM)).nor();
		float desiredAngle = MathUtils.degreesToRadians * toTarget.angle();
		body.setTransform(body.getPosition(), desiredAngle);
	}

	protected void mover(float delta) {
		Vector2 movement = new Vector2(0, 0);
		
		if (Gdx.input.isKeyPressed(Keys.A)) {
			movement.x--;
		}
		if (Gdx.input.isKeyPressed(Keys.D)) {
			movement.x++;
		}
		if (Gdx.input.isKeyPressed(Keys.W)) {
			movement.y++;
		}
		if (Gdx.input.isKeyPressed(Keys.S)) {
			movement.y--;
		}
		movement.scl(velocidad * delta);
		body.setLinearVelocity(movement);
	}
	
	public void agregarDinero(int dinero) {
		this.dinero += dinero;
	}
	
	protected void recargar() {
		if (Gdx.input.isKeyJustPressed(Keys.R)) {
			Gdx.app.log("Personaje", "Recargando");
			armas[armaSeleccionada].recargar();
		}
	}

	public boolean quitarDinero(int dinero) {
		if (this.dinero >= dinero) {
			this.dinero -= dinero;
			return true;
		} else {
			return false;
		}
	}
}
