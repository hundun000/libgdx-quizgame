package hundun.gdxgame.quizgame.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.gdxgame.quizgame.core.domain.QuizRootSaveData;
import hundun.gdxgame.quizgame.core.domain.viewmodel.PlayCountdownClockVM;
import hundun.gdxgame.quizgame.core.domain.viewmodel.PlayQuestionVM;
import hundun.gdxgame.quizgame.core.domain.viewmodel.WaitConfirmMatchConfigMaskBoardVM;
import hundun.gdxgame.quizgame.core.domain.viewmodel.WaitConfirmMatchSituationMaskBoardVM;
import hundun.gdxgame.share.base.BaseHundunScreen;
import hundun.gdxgame.share.base.LogicFrameHelper;
import hundun.gdxgame.share.base.util.DrawableFactory;
import hundun.gdxgame.share.base.util.JavaFeatureForGwt;
import hundun.quizlib.context.QuizComponentContext;
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
        PlayCountdownClockVM.CallerAndCallback
{

    private final GameService quizLib;
    
    MatchConfig matchConfig;
    // --- quizLib quick access cache ---
    MatchSituationView currentMatchSituationView;
    // --- quiz vm data ---
    int currentCountdownFrame;
    boolean isCountdown;
    
    // ====== onShowLazyInit ======
    PlayCountdownClockVM countdownClockVM;
    PlayQuestionVM playQuestionVM;
    // --- lazy add to stage ---
    WaitConfirmMatchConfigMaskBoardVM waitConfirmMatchConfigMaskBoardVM;
    WaitConfirmMatchSituationMaskBoardVM waitConfirmMatchSituationMaskBoardVM;
    
    public QuizPlayScreen(QuizGdxGame game) {
        super(game);
        this.quizLib = game.getQuizLibBridge().getQuizComponentContext().getGameService();
        
        this.logicFrameHelper = new LogicFrameHelper(QuizGdxGame.LOGIC_FRAME_PER_SECOND);
    }

    @Override
    public void show() {
        game.getBatch().setProjectionMatrix(uiStage.getViewport().getCamera().combined);
        
        onShowLazyInit();
        
        intoPostShowState();
    }
    
    public void prepareShow(MatchConfig matchConfig) {
        this.matchConfig = matchConfig;
    }
    
    private void intoPostShowState() {
        onMatchConfigConfirmCallShow();
    }
    
    private void onShowLazyInit() {
        
        countdownClockVM = new PlayCountdownClockVM(
                game, 
                this,
                new TextureRegionDrawable(game.getTextureConfig().getCountdownClockTexture())
                );
        uiRootTable.add(countdownClockVM).left();
     
        playQuestionVM = new PlayQuestionVM(
                game, 
                DrawableFactory.getSimpleBoardBackground()
                );
        uiRootTable.row();
        uiRootTable.add(playQuestionVM).expand();

        
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
        uiRootTable.row();
        uiRootTable.add(showMatchSituationButton);
        
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

    private void renderAllPlay() {
        countdownClockVM.updateCoutdownSecond(logicFrameHelper.frameNumToSecond(currentCountdownFrame));
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
            
            isCountdown = true;
            currentCountdownFrame = logicFrameHelper.secondToFrameNum(switchQuestionEvent.getTime()) ;
            playQuestionVM.updateQuestion(currentMatchSituationView.getQuestion());
            
            renderAllPlay();
        } catch (QuizgameException e) {
            e.printStackTrace();
        }
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
    protected void create() {
        // TODO Auto-generated method stub
        
    }

}
