package hundun.gdxgame.quizgame.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import hundun.gdxgame.quizgame.core.QuizGdxGame;



public class DesktopLauncher {
    public static double WINDOW_SCALE = 0.5;
    
	public static void main (String[] args) {
	    QuizGdxGame game = new QuizGdxGame(new DesktopPreferencesSaveTool("quizgame-desktop-save.xml"));
	    Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
	    config.setWindowedMode((int) (QuizGdxGame.constMainViewportWidth * WINDOW_SCALE), (int) (QuizGdxGame.constMainViewportHeight * WINDOW_SCALE));
		new Lwjgl3Application(game, config);
	}
}
