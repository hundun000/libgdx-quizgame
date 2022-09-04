package hundun.gdxgame.quizgame.core.screen;

import java.util.Arrays;

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
import hundun.quizlib.prototype.match.MatchConfig;
import hundun.quizlib.prototype.match.MatchStrategyType;
import hundun.quizlib.service.BuiltinDataConfiguration;
import hundun.quizlib.service.QuestionLoaderService;
import hundun.quizlib.service.TeamService;

/**
 * @author hundun
 * Created on 2022/08/30
 */
public class TeamScreen extends BaseHundunScreen<QuizGdxGame, QuizRootSaveData> {

    TeamService teamService;
    
    AllTeamManagerAreaVM allTeamManagerAreaVM;
    
    public TeamScreen(QuizGdxGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        Gdx.input.setInputProcessor(uiStage);
        game.getBatch().setProjectionMatrix(uiStage.getViewport().getCamera().combined);
        
        this.teamService = game.getQuizLibBridge().getQuizComponentContext().getTeamService();
    
        uiRootTable.clear();
        
        allTeamManagerAreaVM = new AllTeamManagerAreaVM(this);
        uiRootTable.add(allTeamManagerAreaVM);
        
        uiRootTable.row();
        uiRootTable.add(new ToPlayScreenButtonVM(game));
        
        if (game.debugMode) {
            uiRootTable.debugCell();
        }
        
        readAllTeamAndUpdateVM();
    }

    @Override
    public void dispose() {
    }

    
    private void readAllTeamAndUpdateVM() {
        allTeamManagerAreaVM.updateData(teamService.listTeams());
    }
    
    
    private static class ToPlayScreenButtonVM extends TextButton {

        public ToPlayScreenButtonVM(QuizGdxGame game) {
            super("Next", game.getMainSkin());
            
            this.addListener(
                    new InputListener(){
                        @Override
                        public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                            // TODO
                            MatchConfig matchConfig = new MatchConfig();
                            matchConfig.setTeamNames(Arrays.asList(
                                    BuiltinDataConfiguration.HAS_ROLE_TEAM_NAME_1,
                                    BuiltinDataConfiguration.HAS_ROLE_TEAM_NAME_2
                                    ));
                            matchConfig.setQuestionPackageName(QuestionLoaderService.PRELEASE_PACKAGE_NAME);
                            matchConfig.setMatchStrategyType(MatchStrategyType.MAIN);
                            
                            game.intoQuizPlayScreen(matchConfig);
                            //TextUmaGame.this.getAudioPlayManager().intoScreen(ScreenId.PLAY);
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

}
