package com.studentdefender.path_finder;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.studentdefender.juego.GameScreen;
import com.studentdefender.utils.Constants;

public class Node implements Location<Vector2> {
    private Vector2 posicion;
    private boolean abajo;
    private int index;

    public Node(int x, int y, boolean abajo, int index) {
        posicion = new Vector2(x, y);
        this.abajo = abajo;
        this.index = index;
    }

    public boolean isAbajo() {
        return abajo;
    }

    @Override
    public boolean equals(Object obj) {
        return posicion.epsilonEquals(((Node) obj).getPosicion());
    }

    public int getIndex() {
        return index;
    }

    public Vector2 getPosicion() {
        return posicion;
    }

    @Override
    public Vector2 getPosition() {
        return posicion.cpy().scl(1 / Constants.PPM);
    }

    @Override
    public float getOrientation() {
        return 0;
    }

    @Override
    public void setOrientation(float orientation) {
    }

    @Override
    public float vectorToAngle(Vector2 vector) {
        return vector.angleRad();
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        outVector.x = (float) -Math.sin(angle);
        outVector.y = (float) Math.cos(angle);
        return outVector;
    }

    @Override
    public Location<Vector2> newLocation() {
        return this;
    }

    public void dibujar(SpriteBatch batch, BitmapFont font) {
        GameScreen.shapeRenderer.setColor(Color.WHITE);
        GameScreen.shapeRenderer.begin(ShapeType.Line);
        GameScreen.shapeRenderer.circle(getPosicion().x, getPosicion().y, 10);
        GameScreen.shapeRenderer.end();
        batch.begin();
        font.draw(batch, Integer.toString(getIndex()), getPosicion().x - 3, getPosicion().y + 5);
        font.draw(batch, "x: " + getPosition().x + ", y: " + getPosition().y, getPosicion().x - 60,
                getPosicion().y - 15);
        batch.end();
    }
}