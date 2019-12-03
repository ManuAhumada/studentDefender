package com.studentdefender.juego;

import com.badlogic.gdx.Game;
import com.studentdefender.utils.Global;

public class StudentDefender extends Game {

	public void create() {
		setScreen(new MainMenuScreen(this));
	}

	public void render() {
		super.render();
	}

	public void dispose() {
		Global.batch.dispose();
		Global.font.dispose();
		Global.shapeRenderer.dispose();
	}
}
