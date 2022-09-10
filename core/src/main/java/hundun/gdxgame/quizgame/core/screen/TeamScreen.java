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
import hundun.gdxgame.quizgame.core.domain.viewmodel.teamscreen.MatchStrategySelectVM;
import hundun.gdxgame.quizgame.core.domain.viewmodel.teamscreen.MatchStrategySelectVM.ICallerAndCallback;
import hundun.gdxgame.quizgame.core.domain.viewmodel.teamscreen.TeamNodeVM;
import hundun.gdxgame.quizgame.core.domain.viewmodel.teamscreen.TeamSlotVM;
import hundun.gdxgame.quizgame.core.domain.viewmodel.teamscreen.popup.TeamSelectPopoupVM;
import hundun.gdxgame.quizgame.core.domain.viewmodel.teamscreen.popup.TeamSelectPopoupVM.IWaitTeamSelectCallback;
import hundun.gdxgame.share.base.BaseHundunScreen;
import hundun.quizlib.prototype.TeamPrototype;
import hundun.quizlib.prototype.match.MatchConfig;
import hundun.quizlib.prototype.match.MatchStrategyType;
import hundun.quizlib.service.BuiltinDataConfiguration;
import hundun.quizlib.service.QuestionLoaderService;
import hundun.quizlib.service.TeamService;

/**
 * @author hundun
 * Created on 2022/08/30
 */
public class TeamScreen extends BaseHundunScreen<QuizGdxGame, QuizRootSaveData> implements 
        IWaitTeamSelectCallback, 
        MatchStrategySelectVM.ICallerAndCallback 
        {

    TeamService teamService;
    
    TeamSelectPopoupVM teamSelectPopoupVM;
    MatchStrategySelectVM matchStrategySelectVM;
    
    TeamSlotVM waitChangeTeamSlotVM;
    
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
        
        this.teamSelectPopoupVM = TeamSelectPopoupVM.Factory.build(game, this);
//        uiRootTable.add(teamSelectPopoupVM);
        
        matchStrategySelectVM = new MatchStrategySelectVM(game, this);
        uiRootTable.row();
        uiRootTable.add(matchStrategySelectVM);
        
        uiRootTable.row();
        uiRootTable.add(new ToPlayScreenButtonVM(game));
        
        if (game.debugMode) {
            uiRootTable.debugCell();
        }

    }

    @Override
    public void dispose() {
    }

    
    
    private class ToPlayScreenButtonVM extends TextButton {

        public ToPlayScreenButtonVM(QuizGdxGame game) {
            super("Next", game.getMainSkin());
            
            this.addListener(
                    new InputListener(){
                        @Override
                        public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                            MatchConfig matchConfig = new MatchConfig();
                            matchConfig.setTeamNames(matchStrategySelectVM.getSelectedTeamNames());
                            matchConfig.setQuestionPackageName(QuestionLoaderService.PRELEASE_PACKAGE_NAME);
                            matchConfig.setMatchStrategyType(matchStrategySelectVM.getCurrenType());
                            
                            game.getScreenManager().pushScreen(QuizPlayScreen.class.getSimpleName(), 
                                    "blending_transition",
                                    matchConfig
                                    );
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

    @Override
    public void onTeamSelected(TeamPrototype teamPrototype) {
        Gdx.app.log(this.getClass().getSimpleName(), "onTeamSelected called");
        // --- ui ---
        popupRootTable.clear();
        Gdx.input.setInputProcessor(uiStage);
        // --- logic ---
        waitChangeTeamSlotVM.updateData(teamPrototype);
        this.waitChangeTeamSlotVM = null;
    }

    @Override
    public void onSlotWantChange(TeamSlotVM teamSlotVM) {
        this.waitChangeTeamSlotVM = teamSlotVM;
        callShowTeamSelectPopoup();
    }

    @Override
    public void callShowTeamSelectPopoup() {
        Gdx.app.log(this.getClass().getSimpleName(), "callShowTeamSelectPopoup called");
        // --- ui ---
        popupRootTable.add(teamSelectPopoupVM);
        Gdx.input.setInputProcessor(popupUiStage);
        // --- logic ---
        teamSelectPopoupVM.callShow(teamService.listTeams());
    }

}
