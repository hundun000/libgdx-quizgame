package hundun.gdxgame.quizgame.core.screen;

import java.util.List;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.gdxgame.quizgame.core.config.TextureAtlasKeys;
import hundun.gdxgame.quizgame.core.domain.QuizRootSaveData;
import hundun.gdxgame.quizgame.core.viewmodel.preparescreen.MatchStrategyInfoVM;
import hundun.gdxgame.quizgame.core.viewmodel.preparescreen.MatchStrategySelectVM;
import hundun.gdxgame.quizgame.core.viewmodel.preparescreen.TeamManageAreaVM;
import hundun.gdxgame.quizgame.core.viewmodel.preparescreen.TeamManageSlotVM;
import hundun.gdxgame.quizgame.core.viewmodel.preparescreen.ToPlayScreenButtonVM;
import hundun.gdxgame.quizgame.core.viewmodel.preparescreen.MatchStrategySelectVM.IMatchStrategyChangeListener;
import hundun.gdxgame.quizgame.core.viewmodel.preparescreen.popup.TagSelectPopoupVM;
import hundun.gdxgame.quizgame.core.viewmodel.preparescreen.popup.TeamSelectPopoupVM;
import hundun.gdxgame.corelib.base.BaseHundunScreen;
import hundun.quizlib.exception.QuizgameException;
import hundun.quizlib.prototype.TeamPrototype;
import hundun.quizlib.prototype.match.MatchConfig;
import hundun.quizlib.prototype.match.MatchStrategyType;
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
        TeamManageAreaVM.ICallerAndCallback,
        IMatchStrategyChangeListener
        {

    public static final int MATCHSTRATEGYINFOVM_WIDTH = 400;
    
    TeamService teamService;
    QuestionService questionService;
    
    MatchStrategyType currenType;
    int targetTeamNum;
    String currentQuestionPackageName;
    List<String> selectedTeamNames;
    
    TeamSelectPopoupVM teamSelectPopoupVM;
    TagSelectPopoupVM tagSelectPopoupVM;
    MatchStrategySelectVM matchStrategySelectVM;
    TeamManageAreaVM teamManageAreaVM;
    MatchStrategyInfoVM matchStrategyInfoVM;
    ToPlayScreenButtonVM toPlayScreenButtonVM;
    ToMenuScreenButtonVM toMenuScreenButtonVM;
    
    Image backImage;
    
    public PrepareScreen(QuizGdxGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        Gdx.input.setInputProcessor(uiStage);
        game.getBatch().setProjectionMatrix(uiStage.getViewport().getCamera().combined);
        
        
        this.teamSelectPopoupVM = new TeamSelectPopoupVM(game, this);
        this.tagSelectPopoupVM = new TagSelectPopoupVM(game, this);
        
        InputListener toPlayScreenButtonListener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                
                game.gameSaveCurrent();
                
                MatchConfig matchConfig = new MatchConfig();
                matchConfig.setTeamNames(selectedTeamNames);
                matchConfig.setQuestionPackageName(currentQuestionPackageName);
                matchConfig.setMatchStrategyType(currenType);
                
                game.getScreenManager().pushScreen(QuizPlayScreen.class.getSimpleName(), 
                        "blending_transition",
                        matchConfig
                        );
            }
        };
        
        this.teamManageAreaVM = new TeamManageAreaVM(game, this);
        this.matchStrategySelectVM = new MatchStrategySelectVM(game, this);
        this.toPlayScreenButtonVM = new ToPlayScreenButtonVM(game, toPlayScreenButtonListener);
        this.matchStrategyInfoVM = new MatchStrategyInfoVM(game);
        this.backImage = new Image(game.getTextureConfig().getPrepareScreenBackground());
        this.toMenuScreenButtonVM = new ToMenuScreenButtonVM(game);
        
        backUiStage.clear();
        uiRootTable.clear();
        popupRootTable.clear();
        
        backImage.setBounds(0, 0, game.getWidth(), game.getHeight());
        backUiStage.addActor(backImage);
        
        
//        uiRootTable.add(teamSelectPopoupVM);
        teamManageAreaVM.setBounds(50, 300, 900, 500);
        uiRootTable.addActor(teamManageAreaVM);
        
        matchStrategySelectVM.setBounds(50, 50, 900, 200);
        uiRootTable.addActor(matchStrategySelectVM);
        
        matchStrategyInfoVM.setBounds(1100, 500, MATCHSTRATEGYINFOVM_WIDTH, 350);
        uiRootTable.addActor(matchStrategyInfoVM);
        
        toPlayScreenButtonVM.setBounds(1100, 50, 350, 350);
        uiRootTable.addActor(toPlayScreenButtonVM);
        
        toMenuScreenButtonVM.setBounds(1450, 50, 100, 100);
        uiRootTable.addActor(toMenuScreenButtonVM);
        
        if (game.debugMode) {
            uiRootTable.debugAll();
        }

        // ------ post vm init ------ 
        matchStrategySelectVM.checkSlotNum(MatchStrategyType.PRE);
        validateMatchConfig();
    }

    @Override
    public void dispose() {
    }

    private void validateMatchConfig() {
        if (selectedTeamNames != null && selectedTeamNames.size() == targetTeamNum) {
            toPlayScreenButtonVM.setTouchable(Touchable.enabled);
        } else {
            toPlayScreenButtonVM.setTouchable(Touchable.disabled);
        }
    }
    
    private class ToMenuScreenButtonVM extends Image {

        public ToMenuScreenButtonVM(QuizGdxGame game) {
            
            this.setDrawable(new TextureRegionDrawable(
                    game.getTextureConfig().getPlayScreenUITextureAtlas().findRegion(
                            TextureAtlasKeys.PLAYSCREEN_EXITBUTTON
                            )
                    ));
            this.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    game.gameSaveCurrent();
                    game.getScreenManager().pushScreen(QuizMenuScreen.class.getSimpleName(), 
                            "blending_transition"
                            );
                }
            });
        }
    }



    @Override
    protected void create() {
        this.teamService = game.getQuizLibBridge().getQuizComponentContext().getTeamService();
        this.questionService = game.getQuizLibBridge().getQuizComponentContext().getQuestionService();
        
        this.currentQuestionPackageName = QuestionLoaderService.RELEASE_PACKAGE_NAME;
    }

    @Override
    public void onTeamSelectDone(TeamPrototype teamPrototype) {
        Gdx.app.log(this.getClass().getSimpleName(), "onTeamSelected called");
        // --- ui ---
        popupRootTable.clear();
        Gdx.input.setInputProcessor(uiStage);
        // --- logic ---
        if (teamPrototype != null) {
            selectedTeamNames = teamManageAreaVM.updateWaitChangeDone(teamPrototype);
            validateMatchConfig();
        }
    }

    @Override
    public void onTeamWantChange(TeamManageSlotVM teamSlotVM) {
        teamManageAreaVM.onTeamWantChangeOrModify(teamSlotVM);
        callShowTeamSelectPopoup();
    }
    @Override
    public void onTeamWantModify(TeamManageSlotVM teamSlotVM) {
        try {
            teamManageAreaVM.onTeamWantChangeOrModify(teamSlotVM);
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
        teamSelectPopoupVM.callShow(teamService.listTeams(), teamManageAreaVM.getSelectedTeamNames());
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
    public void onTagSelectDone(TeamPrototype currenTeamPrototype) {
        Gdx.app.log(this.getClass().getSimpleName(), "onTagSelectDone called");
        // --- ui ---
        popupRootTable.clear();
        Gdx.input.setInputProcessor(uiStage);
        // --- logic ---
        // do nothing
        teamManageAreaVM.updateWaitChangeDone(currenTeamPrototype);
    }

    @Override
    public void onMatchStrategyChange(MatchStrategyType currenType) {
        this.currenType = currenType;
        
        if (currenType == MatchStrategyType.PRE) {
            targetTeamNum = 1;
        } else if (currenType == MatchStrategyType.MAIN) {
            targetTeamNum = 2;
        } else {
            Gdx.app.error(this.getClass().getSimpleName(), "onChange cannot handle type: " + currenType);
        }
        
        matchStrategyInfoVM.updateStrategy(currenType);
        teamManageAreaVM.updateSlotNum(targetTeamNum);
    }



    

}
