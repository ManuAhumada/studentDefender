package com.studentdefender.juego;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class StudentDefender extends Game {
	SpriteBatch batch;
	BitmapFont font;

	public void create() {
		batch = new SpriteBatch();
		font = new BitmapFont();
		setScreen(new MainMenuScreen(this));
	}

	public void render() {
		super.render();
	}

	public void dispose() {
		batch.dispose();
		font.dispose();
	}
}