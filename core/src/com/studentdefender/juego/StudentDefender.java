package com.studentdefender.juego;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input.Keys;
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
		if (Gdx.input.isKeyJustPressed(Keys.F11)) {
			Graphics.DisplayMode currentMode = Gdx.graphics.getDisplayMode();
			if (Gdx.graphics.isFullscreen()) Gdx.graphics.setWindowedMode(currentMode.width, currentMode.height);
			else Gdx.graphics.setFullscreenMode(currentMode);
		}
	}

	public void dispose() {
		batch.dispose();
		font.dispose();
	}
}
