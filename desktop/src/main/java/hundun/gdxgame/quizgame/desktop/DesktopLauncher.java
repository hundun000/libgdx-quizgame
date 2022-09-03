package hundun.gdxgame.quizgame.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import hundun.gdxgame.quizgame.core.QuizGdxGame;



public class DesktopLauncher {
	public static void main (String[] arg) {
	    QuizGdxGame game = new QuizGdxGame(new DesktopPreferencesSaveTool("demo-desktop-save"));
	    Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
	    config.setWindowedMode((int) (game.LOGIC_WIDTH * game.WINDOW_SCALE), (int) (game.LOGIC_HEIGHT* game.WINDOW_SCALE));
		new Lwjgl3Application(game, config);
	}
}
