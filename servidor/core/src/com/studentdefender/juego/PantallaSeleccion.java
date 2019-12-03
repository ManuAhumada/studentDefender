package com.studentdefender.juego;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.studentdefender.conexion.SeleccionJugador;
import com.studentdefender.personajes.Profesores;
import com.studentdefender.utils.Global;

public class PantallaSeleccion implements Screen {
    final StudentDefender game;
    ArrayList<SeleccionJugador> jugadores;

    public PantallaSeleccion(final StudentDefender game) {
        this.game = game;
        jugadores = new ArrayList<>();

        Global.camara.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Global.camara.update();
        Global.batch.setProjectionMatrix(Global.camara.combined);
        Global.shapeRenderer.setProjectionMatrix(Global.camara.combined);

        graficar();

        checkNewPlayers();
        checkInputs();
        sendData();
        checkStart();

    }

    private void checkStart() {

        boolean allReady = true;
        for (SeleccionJugador jugador : jugadores) {
            if (!jugador.preparado)
                allReady = false;
        }
        if (allReady) {
            Profesores[] profesoresSeleccionados = new Profesores[jugadores.size()];
            for (int i = 0; i < jugadores.size(); i++) {
                profesoresSeleccionados[i] = Profesores.values()[jugadores.get(i).personajeSeleccionado];
            }
            Global.servidor.enviarMensaje("comenzar");
            game.setScreen(new GameScreen(game, profesoresSeleccionados));
            dispose();
        }
    }

    private void sendData() {
        Global.servidor.enviarMensaje(jugadores);
    }

    private void checkNewPlayers() {
        if (Global.cantJugadores > jugadores.size()) {
            for (int i = 0; i < Global.cantJugadores - jugadores.size(); i++) {
                jugadores.add(new SeleccionJugador());
            }
        }
    }

    private void checkInputs() {
        for (int i = 0; i < jugadores.size(); i++) {
            if (!jugadores.get(i).preparado && Global.mensajesJugadores[i] != null
                    && Global.mensajesJugadores[i] instanceof ArrayList<?>) {

                ArrayList<Integer> inputs = (ArrayList<Integer>) Global.mensajesJugadores[i];
                SeleccionJugador jugador = jugadores.get(i);
                if (inputs.contains(Keys.A)) {
                    System.out.println("Izquierda");
                    if (jugador.personajeSeleccionado == 0)
                        jugador.personajeSeleccionado = Profesores.values().length - 1;
                    else
                        jugador.personajeSeleccionado--;
                }
                if (inputs.contains(Keys.D)) {
                    System.out.println("Derecha");
                    if (jugador.personajeSeleccionado == Profesores.values().length - 1)
                        jugador.personajeSeleccionado = 0;
                    else
                        jugador.personajeSeleccionado++;
                }
                if (inputs.contains(Keys.ENTER)) {
                    jugador.preparado = true;
                }
                Global.mensajesJugadores[i] = null;
            }
        }
    }

    private void graficar() {
        Global.batch.begin();
        Global.font.draw(Global.batch, "Seleccione su personaje", (Gdx.graphics.getWidth() / 2) - 100,
                Gdx.graphics.getHeight() - 100);
        Global.font.draw(Global.batch, "Presione enter para elegir", (Gdx.graphics.getWidth() / 2) - 100, 400);
        Global.batch.end();

        int posInicialX = 300, ancho = 200, margen = 20;
        for (int i = 0; i < Profesores.values().length; i++) {
            Global.batch.begin();
            Global.batch.draw(Profesores.values()[i].getImagen(), posInicialX + ancho * i + margen * i, 500, ancho,
                    ancho);
            Global.font.draw(Global.batch, Profesores.values()[i].getNombre(),
                    posInicialX + ancho * i + margen * i + ancho / 3, 490);
            Global.batch.end();
            Global.shapeRenderer.begin(ShapeType.Line);
            Global.shapeRenderer.setColor(Color.WHITE);
            Global.shapeRenderer.box(posInicialX + ancho * i + margen * i, 500, 0, ancho, ancho, 0);
            Global.shapeRenderer.end();
        }
        Global.shapeRenderer.begin(ShapeType.Line);
        Global.shapeRenderer.setColor(Color.BLUE);
        Global.shapeRenderer.end();
    }

    public void show() {
    }

    public void resize(int width, int height) {
        Global.camara.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Global.camara.update();
    }

    public void pause() {

    }

    public void resume() {

    }

    public void hide() {

    }

    public void dispose() {

    }
}