package hundun.gdxgame.quizgame.core.screen;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import hundun.gdxgame.quizgame.core.domain.viewmodel.playscreen.TeamInfoBoardVM.SystemButtonType;
import hundun.gdxgame.quizgame.core.domain.viewmodel.playscreen.mask.QuestionResultAnimationVM;
import hundun.gdxgame.quizgame.core.domain.viewmodel.playscreen.mask.TeamSwitchAnimationVM;
import hundun.gdxgame.quizgame.core.domain.viewmodel.playscreen.mask.WaitConfirmFirstGetQuestionMaskBoardVM;
import hundun.gdxgame.quizgame.core.domain.viewmodel.playscreen.mask.WaitConfirmMatchSituationMaskBoardVM;
import hundun.gdxgame.quizgame.core.domain.viewmodel.playscreen.mask.WaitConfirmMatchFinishMaskBoardVM;
import hundun.gdxgame.share.base.BaseHundunScreen;
import hundun.gdxgame.share.base.LogicFrameHelper;
import hundun.gdxgame.share.base.util.DrawableFactory;
import hundun.gdxgame.share.base.util.JavaFeatureForGwt;
import hundun.quizlib.exception.QuizgameException;
import hundun.quizlib.model.domain.QuestionModel;
import hundun.quizlib.prototype.TeamPrototype;
import hundun.quizlib.prototype.event.AnswerResultEvent;
import hundun.quizlib.prototype.event.MatchFinishEvent;
import hundun.quizlib.prototype.event.StartMatchEvent;
import hundun.quizlib.prototype.event.SwitchQuestionEvent;
import hundun.quizlib.prototype.event.SwitchTeamEvent;
import hundun.quizlib.prototype.match.MatchConfig;
import hundun.quizlib.service.GameService;
import hundun.quizlib.view.match.MatchSituationView;

/**
 * @author hundun
 * Created on 2022/08/30
 */
public class QuizPlayScreen extends BaseHundunScreen<QuizGdxGame, QuizRootSaveData> 
implements WaitConfirmFirstGetQuestionMaskBoardVM.CallerAndCallback,
        WaitConfirmMatchSituationMaskBoardVM.CallerAndCallback, 
        WaitConfirmMatchFinishMaskBoardVM.CallerAndCallback,
        CountdownClockVM.CallerAndCallback, 
        QuestionOptionAreaVM.CallerAndCallback, 
        TeamInfoBoardVM.CallerAndCallback,
        SkillBoardVM.CallerAndCallback,
        QuestionResultAnimationVM.CallerAndCallback,
        TeamSwitchAnimationVM.CallerAndCallback
{
    

    private final GameService quizLib;
    
    MatchConfig matchConfig;
    // --- quizLib quick access cache ---
    MatchSituationView currentMatchSituationView;
    List<TeamPrototype> teamPrototypes;
    // --- UI ---
    protected final Table uiRootTable1;
    
    List<Runnable> blockingAnimationTasks = new LinkedList<>();
    Runnable afterAllAnimationDoneTask;
    Runnable afterComfirmTask;
    
    // ====== onShowLazyInit ======
    CountdownClockVM countdownClockVM;
    QuestionStemVM questionStemVM;
    TeamInfoBoardVM teamInfoBoardVM;
    ImageAreaVM imageAreaVM;
    QuestionOptionAreaVM questionOptionAreaVM;
    SkillBoardVM skillBoardVM;
    
    // --- lazy add to stage ---
    WaitConfirmFirstGetQuestionMaskBoardVM waitConfirmFirstGetQuestionMaskBoardVM;
    WaitConfirmMatchSituationMaskBoardVM waitConfirmMatchSituationMaskBoardVM;
    WaitConfirmMatchFinishMaskBoardVM waitConfirmMatchFinishMaskBoardVM;
    QuestionResultAnimationVM questionResultAnimationVM;
    TeamSwitchAnimationVM teamSwitchAnimationVM;
    
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
        Gdx.app.log(this.getClass().getSimpleName(), JavaFeatureForGwt.stringFormat(
                "prepareShow by matchConfig = %s", 
                matchConfig.toString()
                ));
    }
    
    private void intoPostShowState() {
        
        try {
            currentMatchSituationView = quizLib.createMatch(matchConfig);
            currentMatchSituationView = quizLib.startMatch(currentMatchSituationView.getId());
            StartMatchEvent startMatchEvent = JavaFeatureForGwt.requireNonNull(currentMatchSituationView.getStartMatchEvent());
            Gdx.app.log(this.getClass().getSimpleName(), JavaFeatureForGwt.stringFormat(
                    "startMatch by QuestionIds = %s", 
                    startMatchEvent.getQuestionIds()
                    ));
            // optional more startMatchEvent handle
            teamPrototypes = startMatchEvent.getTeamPrototypes();
            
            handleCurrentTeam();
            
        } catch (QuizgameException e) {
            Gdx.app.error(this.getClass().getSimpleName(), e.getClass().getSimpleName() + ": " + e.getMessage());
        }
        
        
        callShowFirstGetQuestionConfirm();
    }



    @Override
    public void dispose() {
    }
    
    @Override
    protected void onLogicFrame() {
        if (countdownClockVM.isCountdownState()) {
            countdownClockVM.updateCoutdownSecond(-1);
        }
    }

    @Override
    public void onConfirmed() {
        Gdx.app.log(this.getClass().getSimpleName(), "onMatchConfigConfirmed called");
        // --- ui ---
        popupRootTable.clear();
        // --- screen logic ---
        Gdx.input.setInputProcessor(uiStage);
        //logicFrameHelper.setLogicFramePause(false);
        // --- quiz logic ---
        if (afterComfirmTask != null) {
            Runnable temp = afterComfirmTask;
            afterComfirmTask = null;
            temp.run();
        }
    }
    
    
    private void handleNewQuestion(SwitchQuestionEvent switchQuestionEvent) {
        Gdx.app.log(this.getClass().getSimpleName(), JavaFeatureForGwt.stringFormat(
                "switchQuestion by time second = %s, Question = %s", 
                switchQuestionEvent.getTime(),
                currentMatchSituationView.getQuestion()
                ));
        
        countdownClockVM.resetCountdown(switchQuestionEvent.getTime());
        questionOptionAreaVM.updateQuestion(currentMatchSituationView.getQuestion());
        questionStemVM.updateQuestion(currentMatchSituationView.getQuestion());
    }

    @Override
    public void callShowFirstGetQuestionConfirm() {
        Gdx.app.log(this.getClass().getSimpleName(), "callShowFirstGetQuestionConfirm called");
        // --- ui ---
        
        popupRootTable.add(waitConfirmFirstGetQuestionMaskBoardVM);
        // --- quiz logic ---
        afterComfirmTask = () -> {
            try { 
                currentMatchSituationView = quizLib.nextQustion(currentMatchSituationView.getId());
                SwitchQuestionEvent switchQuestionEvent = JavaFeatureForGwt.requireNonNull(currentMatchSituationView.getSwitchQuestionEvent());

                handleNewQuestion(switchQuestionEvent);
            } catch (QuizgameException e) {
                Gdx.app.error(this.getClass().getSimpleName(), e.getClass().getSimpleName() + ": " + e.getMessage());
            }
        };
        
        // --- screen logic ---
        Gdx.input.setInputProcessor(popupUiStage);
        waitConfirmFirstGetQuestionMaskBoardVM.onCallShow(matchConfig);

    }


    @Override
    public void callShowMatchSituationConfirm() {
        Gdx.app.log(this.getClass().getSimpleName(), "callShowMatchSituationConfirm called");
        // --- ui ---
        
        popupRootTable.add(waitConfirmMatchSituationMaskBoardVM);
        // --- screen logic ---
        Gdx.input.setInputProcessor(popupUiStage);
        waitConfirmMatchSituationMaskBoardVM.onCallShow(currentMatchSituationView);

    }

    @Override
    public void onCountdownZero() {
        Gdx.app.log(this.getClass().getSimpleName(), "onCountdownZero called");
        onChooseOptionOrCountdownZero(QuestionModel.TIMEOUT_ANSWER_TEXT);
    }

    /**
     * @param 一般情况取值ABCD；作为超时传QuestionModel.TIMEOUT_ANSWER_TEXT；作为跳过时传QuestionModel.SKIP_ANSWER_TEXT
     */
    private void onChooseOptionOrCountdownZero(String ansOrControl) {
        try {
            // --- call lib ---
            currentMatchSituationView = quizLib.teamAnswer(currentMatchSituationView.getId(), ansOrControl);
            AnswerResultEvent answerResultEvent = JavaFeatureForGwt.requireNonNull(currentMatchSituationView.getAnswerResultEvent());
            Gdx.app.log(this.getClass().getSimpleName(), JavaFeatureForGwt.stringFormat(
                    "answerResultEvent by Result = %s", 
                    answerResultEvent.getResult()
                    ));
            
            SwitchTeamEvent switchTeamEvent = currentMatchSituationView.getSwitchTeamEvent();
            MatchFinishEvent matchFinishEvent = currentMatchSituationView.getFinishEvent();
            // --- post ---
            countdownClockVM.clearCountdown();
            blockingAnimationTasks.add(() -> callShowQuestionResultAnimation(answerResultEvent));
            
            if (matchFinishEvent != null) {
                blockingAnimationTasks.add(() -> callShowMatchFinishConfirm(matchFinishEvent));
            } else {
                if (switchTeamEvent != null) {
                    blockingAnimationTasks.add(() -> callShowTeamSwitchAnimation(switchTeamEvent));
                }
                
                afterAllAnimationDoneTask = () -> {
                    // --- quiz logic ---
                    try {
                        currentMatchSituationView = quizLib.nextQustion(currentMatchSituationView.getId());
                        SwitchQuestionEvent switchQuestionEvent = JavaFeatureForGwt.requireNonNull(currentMatchSituationView.getSwitchQuestionEvent());
                        
                        handleNewQuestion(switchQuestionEvent);
                    } catch (QuizgameException e) {
                        Gdx.app.error(this.getClass().getSimpleName(), e.getClass().getSimpleName() + ": " + e.getMessage());
                    }
                };
            }
            checkBlockingAnimationTasks();
            
            
        } catch (QuizgameException e) {
            Gdx.app.error(this.getClass().getSimpleName(), e.getClass().getSimpleName() + ": " + e.getMessage());
        }
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
        onChooseOptionOrCountdownZero(ans);
    }


    @Override
    protected void create() {
    
    }
    
    protected void initUI() {
        countdownClockVM = new CountdownClockVM(
                game,
                this,
                this.logicFrameHelper,
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
        
//        Button showMatchSituationButton = new TextButton("showMatchSituation", game.getMainSkin());
//        showMatchSituationButton.addListener(
//                new InputListener(){
//                    @Override
//                    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
//                        callShowMatchSituationConfirm();
//                    }
//                    @Override
//                    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
//                        return true;
//                    }
//                }
//        );
//        uiRootTable1.add(showMatchSituationButton).expand().left().top();
        
        // --- lazy add to stage ---
        double maskBoardScale = 0.8;
        
        waitConfirmFirstGetQuestionMaskBoardVM = new WaitConfirmFirstGetQuestionMaskBoardVM(
                game, 
                this, 
                DrawableFactory.getSimpleBoardBackground((int) (game.LOGIC_WIDTH * maskBoardScale), (int) (game.LOGIC_HEIGHT * maskBoardScale))
                );
        waitConfirmMatchSituationMaskBoardVM = new WaitConfirmMatchSituationMaskBoardVM(
                game, 
                this, 
                DrawableFactory.getSimpleBoardBackground((int) (game.LOGIC_WIDTH * maskBoardScale), (int) (game.LOGIC_HEIGHT * maskBoardScale))
                );
        waitConfirmMatchFinishMaskBoardVM = new WaitConfirmMatchFinishMaskBoardVM(
                game, 
                this, 
                DrawableFactory.getSimpleBoardBackground((int) (game.LOGIC_WIDTH * maskBoardScale), (int) (game.LOGIC_HEIGHT * maskBoardScale))
                );
        questionResultAnimationVM = new QuestionResultAnimationVM(game, this);
        teamSwitchAnimationVM = new TeamSwitchAnimationVM(game, this);
        
        if (game.debugMode) {
            uiRootTable.debugAll();
        }
        
    }


    public void checkBlockingAnimationTasks() {
        if (blockingAnimationTasks.size() > 0) {
            blockingAnimationTasks.remove(0).run();
        } else if (afterAllAnimationDoneTask != null) {
            Runnable temp = afterAllAnimationDoneTask;
            afterAllAnimationDoneTask = null;
            temp.run();
        }
    }

    @Override
    public void onChooseSkill(int index) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onChooseSystem(SystemButtonType type) {
        
        switch (type) {
            case SHOW_MATCH_SITUATION:
                callShowMatchSituationConfirm();
                break;
            default:
                break;
        }

        
    }

    @Override
    protected void renderPopupAnimations(float delta, SpriteBatch spriteBatch) {
        if (questionResultAnimationVM.isRunningState()) {
            questionResultAnimationVM.updateBackground(delta, spriteBatch);
        }
        
        if (teamSwitchAnimationVM.isRunningState()) {
            teamSwitchAnimationVM.updateBackground(delta, spriteBatch);
        }
    }

    @Override
    public void callShowQuestionResultAnimation(AnswerResultEvent answerResultEvent) {
        Gdx.app.log(this.getClass().getSimpleName(), "callShowQuestionResultAnimation called");
        // --- ui ---
        popupRootTable.add(questionResultAnimationVM);
        // --- logic ---
        questionResultAnimationVM.callShow(answerResultEvent);
        Gdx.input.setInputProcessor(popupUiStage);
        //logicFrameHelper.setLogicFramePause(true);
    }

    @Override
    public void onAnimationDone() {
        Gdx.app.log(this.getClass().getSimpleName(), "onAnimationDone called");
        // --- ui ---
        popupRootTable.clear();
        // --- logic ---
        Gdx.input.setInputProcessor(uiStage);
        
        checkBlockingAnimationTasks();
    }

    @Override
    public void callShowTeamSwitchAnimation(SwitchTeamEvent switchTeamEvent) {
        Gdx.app.log(this.getClass().getSimpleName(), "callShowTeamSwitchAnimation called");
        // --- ui ---
        
        popupRootTable.add(teamSwitchAnimationVM);
        // --- quiz logic ---
        handleCurrentTeam();
        
        // --- screen logic ---
        teamSwitchAnimationVM.callShow(switchTeamEvent);
        Gdx.input.setInputProcessor(popupUiStage);
    }
    
    
    private void handleCurrentTeam() {
        TeamPrototype currentTeamPrototype = teamPrototypes.get(currentMatchSituationView.getCurrentTeamIndex());
        teamInfoBoardVM.updateTeam(currentTeamPrototype);
        skillBoardVM.updateRole(currentTeamPrototype.getRolePrototype(), currentMatchSituationView.getCurrentTeamRuntimeInfo().getRoleRuntimeInfo());
    }

    @Override
    public void callShowMatchFinishConfirm(MatchFinishEvent matchFinishEvent) {
        Gdx.app.log(this.getClass().getSimpleName(), "callShowMatchFinishConfirm called");
        // --- ui ---
        
        popupRootTable.add(waitConfirmMatchFinishMaskBoardVM);
        // --- quiz logic ---
        afterComfirmTask = () -> {
            game.intoTeamScreen(false);
        };
        
        // --- screen logic ---
        Gdx.input.setInputProcessor(popupUiStage);
        waitConfirmMatchFinishMaskBoardVM.onCallShow(matchFinishEvent);
    }
    

}
