package hundun.gdxgame.quizgame.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.gdxgame.quizgame.core.domain.QuizRootSaveData;
import hundun.gdxgame.share.base.BaseHundunScreen;
import hundun.gdxgame.share.base.LogicFrameHelper;
import hundun.gdxgame.share.base.util.JavaFeatureForGwt;

/**
 * @author hundun
 * Created on 2022/08/30
 */
public class QuizPlayScreen extends BaseHundunScreen<QuizGdxGame, QuizRootSaveData> {

    Label frameCountLable;
    
    public QuizPlayScreen(QuizGdxGame game) {
        super(game);
        
        setLogicFrameHelper(new LogicFrameHelper(QuizGdxGame.LOGIC_FRAME_PER_SECOND));
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(uiStage);
        game.getBatch().setProjectionMatrix(uiStage.getViewport().getCamera().combined);
        
        frameCountLable = new Label("TEMP", game.getMainSkin());
        
        uiRootTable.add(frameCountLable);
    }


    @Override
    public void dispose() {
    }
    
    @Override
    public void onLogicFrame() {
        String text = JavaFeatureForGwt.stringFormat("Frame: %s", getLogicFrameHelper().getClockCount()); 
        frameCountLable.setText(text);
    }

}
