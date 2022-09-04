package hundun.gdxgame.quizgame.core.domain;

import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.gdxgame.quizgame.core.domain.QuizRootSaveData.MyGameSaveData;
import hundun.gdxgame.quizgame.core.domain.QuizRootSaveData.SystemSetting;
import hundun.gdxgame.share.base.AbstractSaveHandler;

/**
 * @author hundun
 * Created on 2022/09/09
 */
public class QuizSaveHandler extends AbstractSaveHandler<QuizRootSaveData> {
    private final QuizGdxGame game;
     
    public QuizSaveHandler(QuizGdxGame game) {
        this.game = game;
    }

    @Override
    protected void applySystemSetting(QuizRootSaveData rootSaveData) {
        if (rootSaveData.getSystemSetting() != null) {
            game.getTextureConfig().setCurrentEnv(rootSaveData.getSystemSetting().getEnv());
        }
    }
    
    @Override
    protected QuizRootSaveData currentSituationToSaveData() {
        return new QuizRootSaveData(
                new MyGameSaveData("from current"),
                new SystemSetting(game.getTextureConfig().getCurrentEnv()) 
                );
    }
    
    @Override
    protected QuizRootSaveData genereateNewGameSaveData() {
        QuizRootSaveData rootSaveData = QuizRootSaveData.Factory.newGame();
        return rootSaveData;
    }

    @Override
    protected void applyGameSaveData(QuizRootSaveData saveData) {
        // TODO Auto-generated method stub
    }

}
