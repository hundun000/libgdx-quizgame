package hundun.gdxgame.quizgame.html.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.gdxgame.quizgame.html.GwtPreferencesSaveTool;



public class HtmlLauncher extends GwtApplication {

    QuizGdxGame game;
    
    public HtmlLauncher() {
        this.game = new QuizGdxGame(new GwtPreferencesSaveTool("quizgame-html-save"));
    }
    
    @Override
    public GwtApplicationConfiguration getConfig () {
        return new GwtApplicationConfiguration(game.getWidth(), game.getHeight());
    }

    @Override
    public ApplicationListener createApplicationListener() {
        return game;
    }
}