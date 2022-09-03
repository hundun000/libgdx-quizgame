package hundun.gdxgame.quizgame.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.gdxgame.quizgame.core.domain.QuizRootSaveData;
import hundun.gdxgame.quizgame.core.domain.viewmodel.playscreen.CountdownClockVM;
import hundun.gdxgame.quizgame.core.domain.viewmodel.playscreen.ImageAreaVM;
import hundun.gdxgame.quizgame.core.domain.viewmodel.playscreen.QuestionOptionAreaVM;
import hundun.gdxgame.quizgame.core.domain.viewmodel.playscreen.QuestionStemVM;
import hundun.gdxgame.quizgame.core.domain.viewmodel.playscreen.SkillBoardVM;
import hundun.gdxgame.quizgame.core.domain.viewmodel.playscreen.TeamInfoBoardVM;
import hundun.gdxgame.quizgame.core.domain.viewmodel.playscreen.mask.WaitConfirmMatchConfigMaskBoardVM;
import hundun.gdxgame.quizgame.core.domain.viewmodel.playscreen.mask.WaitConfirmMatchSituationMaskBoardVM;
import hundun.gdxgame.share.base.BaseHundunScreen;
import hundun.gdxgame.share.base.LogicFrameHelper;
import hundun.gdxgame.share.base.util.DrawableFactory;
import hundun.gdxgame.share.base.util.JavaFeatureForGwt;
import hundun.quizlib.exception.QuizgameException;
import hundun.quizlib.prototype.event.StartMatchEvent;
import hundun.quizlib.prototype.event.SwitchQuestionEvent;
import hundun.quizlib.prototype.match.MatchConfig;
import hundun.quizlib.service.GameService;
import hundun.quizlib.view.match.MatchSituationView;

/**
 * @author hundun
 * Created on 2022/08/30
 */
public class QuizPlayScreen extends BaseHundunScreen<QuizGdxGame, QuizRootSaveData> 
implements WaitConfirmMatchConfigMaskBoardVM.CallerAndCallback,
        WaitConfirmMatchSituationMaskBoardVM.CallerAndCallback, 
        CountdownClockVM.CallerAndCallback, 
        QuestionOptionAreaVM.CallerAndCallback, 
        TeamInfoBoardVM.CallerAndCallback,
        SkillBoardVM.CallerAndCallback
{
    

    private final GameService quizLib;
    
    MatchConfig matchConfig;
    // --- quizLib quick access cache ---
    MatchSituationView currentMatchSituationView;
    // --- quiz vm data ---
    int currentCountdownFrame;
    boolean isCountdown;
    // --- UI ---
    protected final Table uiRootTable1;
    
    
    // ====== onShowLazyInit ======
    CountdownClockVM countdownClockVM;
    QuestionStemVM questionStemVM;
    TeamInfoBoardVM teamInfoBoardVM;
    ImageAreaVM imageAreaVM;
    QuestionOptionAreaVM questionOptionAreaVM;
    SkillBoardVM skillBoardVM;
    // --- lazy add to stage ---
    WaitConfirmMatchConfigMaskBoardVM waitConfirmMatchConfigMaskBoardVM;
    WaitConfirmMatchSituationMaskBoardVM waitConfirmMatchSituationMaskBoardVM;
    
    public QuizPlayScreen(QuizGdxGame game) {
        super(game);
        
        
        uiRootTable1 = new Table();
        uiRootTable1.setFillParent(true);
        uiStage.addActor(uiRootTable1);
        
        this.quizLib = game.getQuizLibBridge().getQuizComponentContext().getGameService();
        
        this.logicFrameHelper = new LogicFrameHelper(QuizGdxGame.LOGIC_FRAME_PER_SECOND);
    }

    @Override
    public void show() {
        game.getBatch().setProjectionMatrix(uiStage.getViewport().getCamera().combined);
        
        initUI();
        
        intoPostShowState();
    }
    
    public void prepareShow(MatchConfig matchConfig) {
        this.matchConfig = matchConfig;
    }
    
    private void intoPostShowState() {
        onMatchConfigConfirmCallShow();
    }



    @Override
    public void dispose() {
    }
    
    @Override
    public void onLogicFrame() {
        if (isCountdown) {
            currentCountdownFrame--;
            countdownClockVM.updateCoutdownSecond(logicFrameHelper.frameNumToSecond(currentCountdownFrame));
        }
    }


    @Override
    public void onMatchConfigConfirmed() {
        Gdx.app.log(this.getClass().getSimpleName(), "onMatchConfigConfirmed called");
        // --- ui ---
        popupRootTable.clear();
        // --- screen logic ---
        Gdx.input.setInputProcessor(uiStage);
        logicFrameHelper.setLogicFramePause(false);
        // --- play logic ---
        try {
            currentMatchSituationView = quizLib.createMatch(matchConfig);
            currentMatchSituationView = quizLib.startMatch(currentMatchSituationView.getId());
            StartMatchEvent startMatchEvent = JavaFeatureForGwt.requireNonNull(currentMatchSituationView.getStartMatchEvent());
            Gdx.app.log(this.getClass().getSimpleName(), JavaFeatureForGwt.stringFormat(
                    "startMatch by QuestionIds = %s", 
                    startMatchEvent.getQuestionIds()
                    ));
            
            currentMatchSituationView = quizLib.nextQustion(currentMatchSituationView.getId());
            SwitchQuestionEvent switchQuestionEvent = JavaFeatureForGwt.requireNonNull(currentMatchSituationView.getSwitchQuestionEvent());
            Gdx.app.log(this.getClass().getSimpleName(), JavaFeatureForGwt.stringFormat(
                    "switchQuestion by time second = %s, Question = %s", 
                    switchQuestionEvent.getTime(),
                    currentMatchSituationView.getQuestion()
                    ));
            
            updateQuestion(switchQuestionEvent);
            
            

        } catch (QuizgameException e) {
            e.printStackTrace();
        }
    }
    
    private void updateQuestion(SwitchQuestionEvent switchQuestionEvent) {
        isCountdown = true;
        currentCountdownFrame = logicFrameHelper.secondToFrameNum(switchQuestionEvent.getTime()) ;
        countdownClockVM.updateCoutdownSecond(logicFrameHelper.frameNumToSecond(currentCountdownFrame));
        
        questionOptionAreaVM.updateQuestion(currentMatchSituationView.getQuestion());
        questionStemVM.updateQuestion(currentMatchSituationView.getQuestion());
    }

    @Override
    public void onMatchConfigConfirmCallShow() {
        Gdx.app.log(this.getClass().getSimpleName(), "onMatchConfigConfirmCallShow called");
        // --- ui ---
        
        popupRootTable.add(waitConfirmMatchConfigMaskBoardVM);
        // --- logic ---
        waitConfirmMatchConfigMaskBoardVM.onCallShow(matchConfig);
        Gdx.input.setInputProcessor(popupUiStage);
        logicFrameHelper.setLogicFramePause(true);
    }

    @Override
    public void onMatchSituationConfirmed() {
        Gdx.app.log(this.getClass().getSimpleName(), "onMatchSituationConfirmed called");
        // --- ui ---
        popupRootTable.clear();
        
        // --- logic ---
        Gdx.input.setInputProcessor(uiStage);
        logicFrameHelper.setLogicFramePause(false);
    }

    @Override
    public void onMatchSituationConfirmCallShow() {
        Gdx.app.log(this.getClass().getSimpleName(), "onMatchSituationConfirmCallShow called");
        // --- ui ---
        
        popupRootTable.add(waitConfirmMatchSituationMaskBoardVM);
        // --- logic ---
        waitConfirmMatchSituationMaskBoardVM.onCallShow(currentMatchSituationView);
        Gdx.input.setInputProcessor(popupUiStage);
        logicFrameHelper.setLogicFramePause(true);
    }

    @Override
    public void onCountdownZero() {
        Gdx.app.log(this.getClass().getSimpleName(), "onCountdownZero called");
        // --- ui ---
        
        
        // --- logic ---
        isCountdown = false;
    }
    
    @Override
    public void onChooseOption(int index) {
        Gdx.app.log(this.getClass().getSimpleName(), "onChooseOption called");
        String ans;
        switch (index) {
            default:
            case 0:
                ans = "A";
                break;
            case 1:
                ans = "B";
                break;
            case 2:
                ans = "C";
                break;
            case 3:
                ans = "D";
                break;
        }
        
        try {
            currentMatchSituationView = quizLib.teamAnswer(currentMatchSituationView.getId(), ans);
        } catch (QuizgameException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void create() {
    
    }
    
    protected void initUI() {
        countdownClockVM = new CountdownClockVM(
                game,
                this,
                new TextureRegionDrawable(game.getTextureConfig().getCountdownClockTexture())
                );
        uiRootTable.add(countdownClockVM)
                .center()
                .colspan(2)
                .expand()
                ;
     
        questionStemVM = new QuestionStemVM(
                game,
                DrawableFactory.getSimpleBoardBackground()
                );
        uiRootTable.add(questionStemVM)
                .center()
                .colspan(6)
                .expand()
                //.height((float) (game.LOGIC_HEIGHT * 0.4))
                //.width((float) (game.LOGIC_WIDTH * 0.4))
                ;
        
        teamInfoBoardVM = new TeamInfoBoardVM(
                game,
                this,
                DrawableFactory.getSimpleBoardBackground()
                );
        uiRootTable.add(teamInfoBoardVM)
                .center()
                .colspan(4)
                .expand()
                ;
        
        
        uiRootTable.row();
        
        
        imageAreaVM = new ImageAreaVM(
                game,
                DrawableFactory.getSimpleBoardBackground()
                );
        uiRootTable.add(imageAreaVM)
                .center()
                .colspan(3)
                .expand()
                ;
        
        questionOptionAreaVM = new QuestionOptionAreaVM(
                game, 
                this,
                DrawableFactory.getSimpleBoardBackground()
                );
        uiRootTable.add(questionOptionAreaVM)
                .center()
                .colspan(6)
                .expand()
                ;

        skillBoardVM = new SkillBoardVM(
                game, 
                this,
                DrawableFactory.getSimpleBoardBackground()
                );
        uiRootTable.add(skillBoardVM)
                .center()
                .colspan(3)
                .expand()
                ;
        
        Button showMatchSituationButton = new TextButton("showMatchSituation", game.getMainSkin());
        showMatchSituationButton.addListener(
                new InputListener(){
                    @Override
                    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                        onMatchSituationConfirmCallShow();
                    }
                    @Override
                    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
                }
        );
        uiRootTable1.add(showMatchSituationButton).expand().left().top();
        
        // --- lazy add to stage ---
        double maskBoardScale = 0.8;
        
        waitConfirmMatchConfigMaskBoardVM = new WaitConfirmMatchConfigMaskBoardVM(
                game, 
                this, 
                DrawableFactory.getSimpleBoardBackground((int) (game.LOGIC_WIDTH * maskBoardScale), (int) (game.LOGIC_HEIGHT * maskBoardScale))
                );
        waitConfirmMatchSituationMaskBoardVM = new WaitConfirmMatchSituationMaskBoardVM(
                game, 
                this, 
                DrawableFactory.getSimpleBoardBackground((int) (game.LOGIC_WIDTH * maskBoardScale), (int) (game.LOGIC_HEIGHT * maskBoardScale))
                );
        
        if (game.debugMode) {
            uiRootTable.debugAll();
        }
        
    }

    @Override
    public void onChooseSystem(int index) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onChooseSkill(int index) {
        // TODO Auto-generated method stub
        
    }

    

}
