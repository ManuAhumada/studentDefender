package com.studentdefender.utils;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public final class Global {
    public static SpriteBatch batch = new SpriteBatch();
    public static OrthographicCamera camara = new OrthographicCamera();
    public static BitmapFont font = new BitmapFont();
    public static ShapeRenderer shapeRenderer = new ShapeRenderer();
}