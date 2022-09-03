package hundun.gdxgame.quizgame.html.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.gdxgame.quizgame.html.GwtPreferencesSaveTool;



public class HtmlLauncher extends GwtApplication {

    QuizGdxGame game;
    
    public HtmlLauncher() {
        this.game = new QuizGdxGame(new GwtPreferencesSaveTool("demo-html-save"));
    }
    
    @Override
    public GwtApplicationConfiguration getConfig () {
        return new GwtApplicationConfiguration(game.LOGIC_WIDTH, game.LOGIC_HEIGHT);
    }

    @Override
    public ApplicationListener createApplicationListener() {
        return game;
    }
}