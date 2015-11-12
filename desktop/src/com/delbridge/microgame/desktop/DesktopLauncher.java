package com.delbridge.microgame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.delbridge.microgame.MicroGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = MicroGame.WIDTH;
		config.height = MicroGame.HEIGHT;
		config.title = "Micro";
		new LwjglApplication(new MicroGame(), config);
	}
}
