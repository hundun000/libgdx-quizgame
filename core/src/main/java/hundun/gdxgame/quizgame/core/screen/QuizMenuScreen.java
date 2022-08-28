package hundun.gdxgame.quizgame.core.screen;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.gdxgame.quizgame.core.domain.QuizRootSaveData;
import hundun.gdxgame.share.starter.StarterMenuScreen;

/**
 * @author hundun
 * Created on 2022/09/01
 */
public class QuizMenuScreen extends StarterMenuScreen<QuizGdxGame, QuizRootSaveData> {

    public QuizMenuScreen(QuizGdxGame game) {
        super(game);
        
        StarterMenuScreen.Factory.simpleFill(this, game, 
                "Quiz", 
                game.getTextureConfig().getMenuTexture(), 
                new InputListener(){
                    @Override
                    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                        game.intoTeamScreen(true);
                    }
                    @Override
                    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
                },
                new InputListener(){
                    @Override
                    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                        game.intoTeamScreen(false);
                    }
                    @Override
                    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
                }
                );
    }

}
