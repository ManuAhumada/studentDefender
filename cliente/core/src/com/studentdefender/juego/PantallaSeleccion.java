package com.studentdefender.juego;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.studentdefender.conexion.Mensaje;
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

        readData();
        draw();

        sendInput();
    }

    private void readData() {
        if (!Global.mensaje.isEmpty()) {
            Mensaje mensaje = Global.mensaje.get(Global.mensaje.size() - 1);
            Global.mensaje.clear();
            if (Global.mensaje != null && mensaje.mensaje instanceof ArrayList<?>) {
                jugadores = (ArrayList<SeleccionJugador>) mensaje.mensaje;
                Global.mensaje.clear();
            }
            if (Global.mensaje != null && mensaje.mensaje instanceof String
                    && ((String) mensaje.mensaje).equals("comenzar")) {
                game.setScreen(new GameScreen(game));
                dispose();
            }
        }
    }

    private void draw() {
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
        for (int i = 0; i < jugadores.size(); i++) {
            Global.shapeRenderer
                    .setColor(i == 0 ? Color.BLUE : i == 1 ? Color.RED : i == 2 ? Color.GREEN : Color.YELLOW);
            Global.shapeRenderer.box(posInicialX + ancho * jugadores.get(i).personajeSeleccionado
                    + margen * jugadores.get(i).personajeSeleccionado, 500, 0, ancho, ancho, 0);
        }
        Global.shapeRenderer.end();

    }

    private void sendInput() {
        ArrayList<Integer> keys = new ArrayList<>();

        if (Gdx.input.isKeyJustPressed(Keys.A)) {
            keys.add(Keys.A);
        }
        if (Gdx.input.isKeyJustPressed(Keys.D)) {
            keys.add(Keys.D);
        }
        if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
            keys.add(Keys.ENTER);
        }
        if (!keys.isEmpty())
            Global.servidor.enviarMensaje(keys);
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