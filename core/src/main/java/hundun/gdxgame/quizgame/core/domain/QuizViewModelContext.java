package hundun.gdxgame.quizgame.core.domain;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

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
public class QuizViewModelContext extends BaseViewModelContext<QuizRootSaveData> {

    @Getter
    private QuizRootSaveData rootSaveData;

    QuizGdxGame game;
    
    public QuizViewModelContext(QuizGdxGame game) {
        this.game = game;
        
    }

    private void contextFirstLazyInit() {
        
        BaseHundunScreen<?, ?> screen;
        
        screen = new QuizMenuScreen(game);
        screenMap.put(screen.getClass(), screen);
        
        screen = new TeamScreen(game);
        screenMap.put(screen.getClass(), screen);
        
        screen = new QuizPlayScreen(game);
        screenMap.put(screen.getClass(), screen);
    }

    @Override
    protected void lazyInitOnGameCreate() {
        contextFirstLazyInit();
        
    }

    @Override
    protected void disposeAll() {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void applySaveData(QuizRootSaveData rootSaveData) {
        this.rootSaveData = rootSaveData;
    }
    
    @Override
    protected QuizRootSaveData currentSituationToSaveData() {
        return rootSaveData;
    }
    
    @Override
    protected QuizRootSaveData genereateNewGameSaveData() {
        QuizRootSaveData rootSaveData = QuizRootSaveData.Factory.newGame();
        rootSaveData.getData().setValue("new Hello world");
        return rootSaveData;
    }


}
