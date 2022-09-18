package hundun.gdxgame.quizgame.core.screen;

import java.util.Arrays;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.gdxgame.quizgame.core.domain.QuizRootSaveData;
import hundun.gdxgame.quizgame.core.viewmodel.preparescreen.MatchStrategySelectVM;
import hundun.gdxgame.quizgame.core.viewmodel.preparescreen.TeamManageAreaVM;
import hundun.gdxgame.quizgame.core.viewmodel.preparescreen.TeamManageSlotVM;
import hundun.gdxgame.quizgame.core.viewmodel.preparescreen.TeamNodeVM;
import hundun.gdxgame.quizgame.core.viewmodel.preparescreen.popup.TagSelectPopoupVM;
import hundun.gdxgame.quizgame.core.viewmodel.preparescreen.popup.TeamSelectPopoupVM;
import hundun.gdxgame.quizgame.core.viewmodel.preparescreen.popup.TeamSelectPopoupVM.IWaitTeamSelectCallback;
import hundun.gdxgame.share.base.BaseHundunScreen;
import hundun.quizlib.exception.QuizgameException;
import hundun.quizlib.prototype.TeamPrototype;
import hundun.quizlib.prototype.match.MatchConfig;
import hundun.quizlib.prototype.match.MatchStrategyType;
import hundun.quizlib.service.BuiltinDataConfiguration;
import hundun.quizlib.service.QuestionLoaderService;
import hundun.quizlib.service.QuestionService;
import hundun.quizlib.service.TeamService;

/**
 * @author hundun
 * Created on 2022/08/30
 */
public class PrepareScreen extends BaseHundunScreen<QuizGdxGame, QuizRootSaveData> implements 
        TeamSelectPopoupVM.IWaitTeamSelectCallback, 
        TagSelectPopoupVM.IWaitTagSelectCallback, 
        TeamManageAreaVM.ICallerAndCallback
        {

    TeamService teamService;
    QuestionService questionService;
    
    TeamSelectPopoupVM teamSelectPopoupVM;
    TagSelectPopoupVM tagSelectPopoupVM;
    MatchStrategySelectVM matchStrategySelectVM;
    TeamManageAreaVM teamManageAreaVM;
    
    String currentQuestionPackageName;
    TeamManageSlotVM waiChangeDoneTeamManageSlotVM;
    
    public PrepareScreen(QuizGdxGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        Gdx.input.setInputProcessor(uiStage);
        game.getBatch().setProjectionMatrix(uiStage.getViewport().getCamera().combined);
        
        
        this.teamSelectPopoupVM = TeamSelectPopoupVM.Factory.build(game, this);
        this.tagSelectPopoupVM = TagSelectPopoupVM.Factory.build(game, this);
        this.teamManageAreaVM = new TeamManageAreaVM(game, this);
        this.matchStrategySelectVM = new MatchStrategySelectVM(game, teamManageAreaVM);
        
        uiRootTable.clear();
        
        
//        uiRootTable.add(teamSelectPopoupVM);
        uiRootTable.add(teamManageAreaVM);
        
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
                            matchConfig.setTeamNames(teamManageAreaVM.getSelectedTeamNames());
                            matchConfig.setQuestionPackageName(currentQuestionPackageName);
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
        this.teamService = game.getQuizLibBridge().getQuizComponentContext().getTeamService();
        this.questionService = game.getQuizLibBridge().getQuizComponentContext().getQuestionService();
        
        this.currentQuestionPackageName = QuestionLoaderService.PRELEASE_PACKAGE_NAME;
    }

    @Override
    public void onTeamSelectDone(TeamPrototype teamPrototype) {
        Gdx.app.log(this.getClass().getSimpleName(), "onTeamSelected called");
        // --- ui ---
        popupRootTable.clear();
        Gdx.input.setInputProcessor(uiStage);
        // --- logic ---
        waiChangeDoneTeamManageSlotVM.updateData(teamPrototype);
        this.waiChangeDoneTeamManageSlotVM = null;
    }

    @Override
    public void onTeamWantChange(TeamManageSlotVM teamSlotVM) {
        this.waiChangeDoneTeamManageSlotVM = teamSlotVM;
        callShowTeamSelectPopoup();
    }
    @Override
    public void onTeamWantModify(TeamManageSlotVM teamSlotVM) {
        try {
            callShowTagSelectPopoup(teamSlotVM.getData(), questionService.getTags(currentQuestionPackageName));
        } catch (QuizgameException e) {
            Gdx.app.error(this.getClass().getSimpleName(), e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    @Override
    public void callShowTeamSelectPopoup() {
        Gdx.app.log(this.getClass().getSimpleName(), "callShowTeamSelectPopoup called");
        // --- ui ---
        popupRootTable.add(teamSelectPopoupVM);
        popupUiStage.setScrollFocus(teamSelectPopoupVM.getScrollPane());
        Gdx.input.setInputProcessor(popupUiStage);
        // --- logic ---
        teamSelectPopoupVM.callShow(teamService.listTeams());
    }

    @Override
    public void callShowTagSelectPopoup(TeamPrototype currenTeamPrototype, Set<String> allTags) {
        Gdx.app.log(this.getClass().getSimpleName(), "callShowTagSelectPopoup called");
        // --- ui ---
        popupRootTable.add(tagSelectPopoupVM);
        popupUiStage.setScrollFocus(tagSelectPopoupVM.getScrollPane());
        Gdx.input.setInputProcessor(popupUiStage);
        // --- logic ---
        tagSelectPopoupVM.callShow(currenTeamPrototype, allTags);
    }

    @Override
    public void onTagSelectDone() {
        Gdx.app.log(this.getClass().getSimpleName(), "onTagSelectDone called");
        // --- ui ---
        popupRootTable.clear();
        Gdx.input.setInputProcessor(uiStage);
        // --- logic ---
        // do nothing
    }



    

}
