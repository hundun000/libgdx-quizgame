package hundun.gdxgame.quizgame.core.domain;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import hundun.gdxgame.quizgame.core.QuizGdxGame;
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
        
        screen = StarterMenuScreen.Factory.simpleBuild(
                game, 
                "Quiz", 
                game.getTextureConfig().getMenuTexture(), 
                new InputListener(){
                    @Override
                    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                        game.gameLoadOrNew(true);
                        game.setScreen(getScreen(TeamScreen.class));
                        //TextUmaGame.this.getAudioPlayManager().intoScreen(ScreenId.PLAY);
                    }
                    @Override
                    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
                },
                new InputListener(){
                    @Override
                    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                        game.gameLoadOrNew(false);
                        game.setScreen(getScreen(TeamScreen.class));
                        //TextUmaGame.this.getAudioPlayManager().intoScreen(ScreenId.PLAY);
                    }
                    @Override
                    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
                }
                );
        
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
