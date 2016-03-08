package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.MyGdxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
                //System.setProperty("org.lwjgl.opengl.Display.allowSoftwareOpenGL", "true");                
                config.title = "Sifter";
                config.height = 660;
                config.width = 660;              
		new LwjglApplication(new MyGdxGame(), config);                
	}
}
