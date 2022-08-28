package hundun.gdxgame.quizgame.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import hundun.gdxgame.quizgame.core.QuizGdxGame;



public class DesktopLauncher {
	public static void main (String[] arg) {
	    Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		new Lwjgl3Application(new QuizGdxGame(new DesktopPreferencesSaveTool("demo-desktop-save")), config);
	}
}