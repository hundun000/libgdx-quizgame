package hundun.gdxgame.quizgame.core.domain;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import de.eskalon.commons.screen.transition.impl.BlendingTransition;
import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.gdxgame.quizgame.core.screen.QuizMenuScreen;
import hundun.gdxgame.quizgame.core.screen.QuizPlayScreen;
import hundun.gdxgame.quizgame.core.screen.TeamScreen;
import hundun.gdxgame.share.base.BaseHundunGame;
import hundun.gdxgame.share.base.BaseHundunScreen;
import hundun.gdxgame.share.base.BaseViewModelContext;
import hundun.gdxgame.share.starter.StarterMenuScreen;
import lombok.Getter;

/**
 * @author hundun
 * Created on 2022/08/30
 */
public class QuizViewModelContext extends BaseViewModelContext {

    QuizGdxGame game;
    
    public QuizViewModelContext(QuizGdxGame game) {
        this.game = game;
        
    }

    private void contextFirstLazyInit() {
        
        BaseHundunScreen<?, ?> screen;
        
        screen = new QuizMenuScreen(game);
        game.getScreenManager().addScreen(screen.getClass().getSimpleName(), screen);
        
        screen = new TeamScreen(game);
        game.getScreenManager().addScreen(screen.getClass().getSimpleName(), screen);
        
        screen = new QuizPlayScreen(game);
        game.getScreenManager().addScreen(screen.getClass().getSimpleName(), screen);
        
        BlendingTransition blendingTransition = new BlendingTransition(game.getBatch(), 1F);
        game.getScreenManager().addScreenTransition("blending_transition", blendingTransition);
    }

    @Override
    protected void lazyInitOnGameCreate() {
        contextFirstLazyInit();
        
    }

    @Override
    protected void disposeAll() {
        // TODO Auto-generated method stub
        
    }

    


}
