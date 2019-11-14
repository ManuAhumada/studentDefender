package com.studentdefender.path_finder;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.studentdefender.juego.GameScreen;
import com.studentdefender.utils.Constants;
import com.studentdefender.utils.Global;

public class Node implements Location<Vector2> {
    private Body body;
    private boolean abajo;
    private int index;

    public Node(int x, int y, boolean abajo, int index) {
        body = createCircle(x, y);
        this.abajo = abajo;
        this.index = index;
    }

    public boolean isAbajo() {
        return abajo;
    }

    @Override
    public boolean equals(Object obj) {
        return getIndex() == (((Node) obj).getIndex());
    }

    public int getIndex() {
        return index;
    }

    @Override
    public Vector2 getPosition() {
        return body.getPosition();
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
        Global.shapeRenderer.setColor(Color.WHITE);
        Global.shapeRenderer.begin(ShapeType.Line);
        Global.shapeRenderer.circle(getPosition().x * Constants.PPM, getPosition().y * Constants.PPM, 14);
        Global.shapeRenderer.end();
        batch.begin();
        font.draw(batch, Integer.toString(getIndex()), getPosition().x * Constants.PPM - 3,
                getPosition().y * Constants.PPM + 5);
        font.draw(batch, "x: " + getPosition().x + ", y: " + getPosition().y, getPosition().x * Constants.PPM - 60,
                getPosition().y * Constants.PPM - 15);
        batch.end();
    }

    private Body createCircle(float x, float y) {
        Body pBody;
        BodyDef def = new BodyDef();

        def.type = BodyDef.BodyType.StaticBody;

        def.position.set(x / Constants.PPM, y / Constants.PPM);
        pBody = GameScreen.world.createBody(def);

        CircleShape shape = new CircleShape();
        shape.setRadius(1 / Constants.PPM);

        FixtureDef fd = new FixtureDef();
        fd.isSensor = true;
        fd.shape = shape;
        pBody.createFixture(fd).setUserData(this);
        shape.dispose();
        return pBody;
    }
}