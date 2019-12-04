package com.studentdefender.juego.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.studentdefender.juego.StudentDefender;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Student Defender - Cliente";
		config.width = 1360;
		config.height = 768;
		new LwjglApplication(new StudentDefender(), config);
	}
}
