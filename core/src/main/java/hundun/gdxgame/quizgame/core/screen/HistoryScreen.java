package hundun.gdxgame.quizgame.core.screen;

import java.util.Arrays;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.gdxgame.quizgame.core.domain.QuizRootSaveData;
import hundun.gdxgame.quizgame.core.domain.viewmodel.teamscreen.AllTeamManagerAreaVM;
import hundun.gdxgame.quizgame.core.domain.viewmodel.teamscreen.TeamManagerVM;
import hundun.gdxgame.share.base.BaseHundunScreen;
import hundun.quizlib.prototype.event.MatchFinishEvent;
import hundun.quizlib.prototype.match.MatchConfig;
import hundun.quizlib.prototype.match.MatchStrategyType;
import hundun.quizlib.service.BuiltinDataConfiguration;
import hundun.quizlib.service.QuestionLoaderService;
import hundun.quizlib.service.TeamService;
import lombok.Data;

/**
 * @author hundun
 * Created on 2022/08/30
 */
public class HistoryScreen extends BaseHundunScreen<QuizGdxGame, QuizRootSaveData> {

    public HistoryScreen(QuizGdxGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        Gdx.input.setInputProcessor(uiStage);
        game.getBatch().setProjectionMatrix(uiStage.getViewport().getCamera().combined);
        
        uiRootTable.clear();

        uiRootTable.add(new ToNextScreenButtonVM(game));
        
        if (game.debugMode) {
            uiRootTable.debugCell();
        }

    }

    @Override
    public void dispose() {
    }

    
    
    private static class ToNextScreenButtonVM extends TextButton {

        public ToNextScreenButtonVM(QuizGdxGame game) {
            super("Next", game.getMainSkin());
            
            this.addListener(
                    new InputListener(){
                        @Override
                        public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

                            game.intoTeamScreen();
                            
                        }
                        @Override
                        public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                            return true;
                        }
                    }
            );
        }
        
        
        
    }


    @Override
    protected void create() {
        // TODO Auto-generated method stub
        
    }

    @Data
    public static class MatchFinishHistory {
        Map<String, Integer> data;
    }
    
    public void prepareShow(MatchFinishHistory history) {
        // TODO Auto-generated method stub
        
    }

}
