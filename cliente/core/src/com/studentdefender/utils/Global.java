package com.studentdefender.utils;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.studentdefender.conexion.HiloCliente;
import com.studentdefender.conexion.Mensaje;

public final class Global {
    public static SpriteBatch batch = new SpriteBatch();
    public static OrthographicCamera camara = new OrthographicCamera();
    public static BitmapFont font = new BitmapFont();
    public static ShapeRenderer shapeRenderer = new ShapeRenderer();
    public static HiloCliente servidor;
    public static int jugador = -1;
    public static ArrayList<Mensaje> mensaje = new ArrayList<>();

    public static final short BIT_LUZ = 2;
    public static final short BIT_PARED = 4;
}