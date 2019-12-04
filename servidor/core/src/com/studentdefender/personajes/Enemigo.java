package com.studentdefender.personajes;

import static com.studentdefender.utils.Constants.PPM;

import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.ai.steer.behaviors.LookWhereYouAreGoing;
import com.badlogic.gdx.ai.steer.behaviors.PrioritySteering;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.badlogic.gdx.utils.TimeUtils;
import com.studentdefender.juego.GameScreen;
import com.studentdefender.path_finder.GraphPathImp;
import com.studentdefender.path_finder.HeuristicImp;
import com.studentdefender.path_finder.Node;
import com.studentdefender.utils.Constants;
import com.studentdefender.utils.Global;

public class Enemigo extends Personaje implements Poolable {

	private boolean activo;
	private int fuerza;
	private long ultimoAtaque;
	private Alumnos alumno;
	private Seek<Vector2> seekBehavior;
	private BlendedSteering<Vector2> steeringBehavior;
	private SteeringAcceleration<Vector2> steeringOutput;

	private GraphPathImp graphPath;

	public Enemigo() {
		super(0, 0, 0, 100, 300, true);
		fuerza = 10;
		activo = false;
		body.setActive(false);
		steeringOutput = new SteeringAcceleration<Vector2>(new Vector2());
		PrioritySteering<Vector2> prioritySteering = new PrioritySteering<>(this);
		seekBehavior = new Seek<Vector2>(this, GameScreen.jugadores.first());
		prioritySteering.add(seekBehavior);
		graphPath = new GraphPathImp();
		steeringBehavior = new BlendedSteering<Vector2>(this);
		steeringBehavior.add(prioritySteering, 1);
		steeringBehavior.add(new LookWhereYouAreGoing<>(this), 1);
	}

	public void init(int x, int y, float radio, int vida) {
		alumno = Alumnos.values()[MathUtils.random(Alumnos.values().length - 1)];
		activo = true;
		ultimoAtaque = 0;
		this.vida = vida;
		this.vidaActual = vida;
		body.setActive(true);
		body.setTransform(x / PPM, y / PPM, 0);
		body.getFixtureList().first().getShape().setRadius(radio / PPM);
		GameScreen.enemigosActivos.add(this);
	}

	public void reset() {
		GameScreen.enemigosActivos.removeValue(this, true);
		body.setTransform(0, 0, 0);
		body.setLinearVelocity(0, 0);
		vidaActual = vida;
		body.setActive(false);
	}

	public void actualizar(float delta) {
		definirObjetivo();
		steeringBehavior.calculateSteering(steeringOutput);
		applySteering(steeringOutput, delta);
	}

	private void definirObjetivo() {
		Location<Vector2> objetivo = this; // Si no encuentra nada que se quede en el lugar
		Jugador jugadorCercano = encontrarJugadorMasCercano();
		GameScreen.world.rayCast(GameScreen.rayCastCallback, getPosition(), jugadorCercano.getPosition());
		if (GameScreen.rayCastCallback.isHit()) {
			objetivo = jugadorCercano;
			graphPath.clear();
		} else {
			Node nodoCercano = GameScreen.indexedGraphImp.getCloserNode(getPosition());
			Node nodoCercanoEnemigo = GameScreen.indexedGraphImp.getCloserNode(jugadorCercano.getPosition());
			graphPath.clear();
			GameScreen.indexedAStarPathFinder.searchNodePath(nodoCercano, nodoCercanoEnemigo, new HeuristicImp(),
					graphPath);
			if (!(graphPath.getCount() == 0)) {
				if (graphPath.getCount() == 1) {
					objetivo = graphPath.get(0);
				} else {
					GameScreen.world.rayCast(GameScreen.rayCastCallback, getPosition(), graphPath.get(1).getPosition());
					if (GameScreen.rayCastCallback.isHit()) {
						objetivo = graphPath.get(1);
					} else {
						objetivo = graphPath.get(0);
					}
				}
			}
		}
		seekBehavior.setTarget(objetivo);
	}

	private Jugador encontrarJugadorMasCercano() {
		Jugador jugadorCercano = null;
		float distanciaMinima = 0;
		for (Jugador jugador : GameScreen.jugadores) {
			if (!jugador.isAbatido()) {
				float distancia = this.body.getPosition().dst2(jugador.getPosition());
				if (distancia < distanciaMinima || jugadorCercano == null) {
					jugadorCercano = jugador;
					distanciaMinima = distancia;
				}
			}
		}
		return jugadorCercano;
	}

	private void applySteering(SteeringAcceleration<Vector2> steering, float delta) {
		body.setLinearVelocity(steering.linear.nor().scl(delta * velocidad));
		body.setAngularVelocity(steering.angular);
	}

	public void atacar(Jugador jugador) {
		if (TimeUtils.timeSinceNanos(ultimoAtaque) > 1300000000) {
			ultimoAtaque = TimeUtils.nanoTime();
			jugador.quitarVida(fuerza, this);
		}
	}

	public void quitarVida(int vidaQuitada, Personaje atacante) {
		super.quitarVida(vidaQuitada, atacante);
		body.setLinearVelocity(0, 0);
		if (vidaActual == 0) {
			activo = false;
			((Jugador) atacante).agregarDinero(5);
			((Jugador) atacante).agregarBalas(10);
		}
	}

	public boolean isActivo() {
		return activo;
	}

	public void dibujar() {
		super.dibujar();
		Global.shapeRenderer.begin(ShapeType.Filled);
		Global.shapeRenderer.setColor(Color.GREEN);
		Global.shapeRenderer.rect((getPosition().x - getBoundingRadius()) * PPM,
				(getPosition().y + getBoundingRadius()) * PPM + 5, getBoundingRadius() * 2 * PPM * vidaActual / vida,
				5);
		Global.shapeRenderer.setColor(Color.RED);
		Global.shapeRenderer.rect(
				(getPosition().x - getBoundingRadius()) * PPM + getBoundingRadius() * 2 * PPM * vidaActual / vida,
				(getPosition().y + getBoundingRadius()) * PPM + 5,
				getBoundingRadius() * 2 * PPM * (vida - vidaActual) / vida, 5);
		Global.shapeRenderer.end();
		if (Constants.DEBUG) {
			Global.shapeRenderer.begin(ShapeType.Line);
			Global.shapeRenderer.setColor(Color.RED);
			Global.shapeRenderer.line(getPosition().cpy().scl(PPM),
					encontrarJugadorMasCercano().getPosition().cpy().scl(PPM));
			if (graphPath.getCount() > 1) {
				Global.shapeRenderer.line(getPosition().cpy().scl(PPM), graphPath.get(1).getPosition().cpy().scl(PPM));
			}
			Global.shapeRenderer.setColor(Color.WHITE);
			Global.shapeRenderer.end();
			Global.shapeRenderer.begin(ShapeType.Filled);
			Global.shapeRenderer.circle(GameScreen.rayCastCallback.getImpactPoint().cpy().scl(PPM).x,
					GameScreen.rayCastCallback.getImpactPoint().cpy().scl(PPM).y, 2);
			Global.shapeRenderer.end();
		}
	}

	public Alumnos getAlumno() {
		return alumno;
	}
}
