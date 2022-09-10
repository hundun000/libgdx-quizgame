package hundun.gdxgame.quizgame.core.screen;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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
import hundun.gdxgame.quizgame.core.domain.viewmodel.playscreen.SystemBoardVM;
import hundun.gdxgame.quizgame.core.domain.viewmodel.playscreen.SystemBoardVM.SystemButtonType;
import hundun.gdxgame.quizgame.core.domain.viewmodel.playscreen.mask.QuestionResultAnimationVM;
import hundun.gdxgame.quizgame.core.domain.viewmodel.playscreen.mask.TeamSwitchAnimationVM;
import hundun.gdxgame.quizgame.core.domain.viewmodel.playscreen.mask.WaitConfirmFirstGetQuestionMaskBoardVM;
import hundun.gdxgame.quizgame.core.domain.viewmodel.playscreen.mask.WaitConfirmMatchSituationMaskBoardVM;
import hundun.gdxgame.quizgame.core.screen.HistoryScreen.MatchFinishHistory;
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
        SystemBoardVM.CallerAndCallback,
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
//    protected final Table uiRootTable1;
    
    List<Runnable> blockingAnimationTaskQueue = new LinkedList<>();
    Runnable afterAllAnimationDoneTask;
    Runnable afterComfirmTask;
    
    // ====== onShowLazyInit ======
    Image backImage;
    CountdownClockVM countdownClockVM;
    QuestionStemVM questionStemVM;
    TeamInfoBoardVM teamInfoBoardVM;
    SystemBoardVM systemBoardVM;
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
        
        
//        uiRootTable1 = new Table();
//        uiRootTable1.setFillParent(true);
//        uiStage.addActor(uiRootTable1);
        
        this.quizLib = game.getQuizLibBridge().getQuizComponentContext().getGameService();
        
        this.logicFrameHelper = new LogicFrameHelper(QuizGdxGame.LOGIC_FRAME_PER_SECOND);
    }

    @Override
    public void show() {
        super.show();
        game.getBatch().setProjectionMatrix(uiStage.getViewport().getCamera().combined);
        
        this.matchConfig = (MatchConfig) pushParams[0];
        Gdx.app.log(this.getClass().getSimpleName(), JavaFeatureForGwt.stringFormat(
                "pushParams by matchConfig = %s", 
                matchConfig.toString()
                ));
        
        rebuildUI();
        
        handleCreateAndStartMatch();
    }
    
    private void handleCreateAndStartMatch() {
        
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
        Gdx.app.log(this.getClass().getSimpleName(), "onConfirmed called");
        // --- ui ---
        popupRootTable.clear();
        // --- screen logic ---
        Gdx.input.setInputProcessor(uiStage);
        logicFrameHelper.setLogicFramePause(false);
        // --- quiz logic ---
        if (afterComfirmTask != null) {
            Gdx.app.log(this.getClass().getSimpleName(), "has afterComfirmTask");
            Runnable temp = afterComfirmTask;
            afterComfirmTask = null;
            temp.run();
        }
    }
    
    
    private void handleNewQuestion() {
        try {
            currentMatchSituationView = quizLib.nextQustion(currentMatchSituationView.getId());
        } catch (QuizgameException e) {
            Gdx.app.error(this.getClass().getSimpleName(), e.getClass().getSimpleName() + ": " + e.getMessage());
            return;
        }
        SwitchQuestionEvent switchQuestionEvent = JavaFeatureForGwt.requireNonNull(currentMatchSituationView.getSwitchQuestionEvent());
        
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
        logicFrameHelper.setLogicFramePause(true);
        // --- quiz logic ---
        afterComfirmTask = () -> {
            handleNewQuestion();
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
        logicFrameHelper.setLogicFramePause(true);
        // --- quiz logic ---
        afterComfirmTask = null;
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
            blockingAnimationTaskQueue.add(() -> callShowQuestionResultAnimation(answerResultEvent));
            
            if (matchFinishEvent != null) {
                blockingAnimationTaskQueue.add(() -> callShowMatchFinishConfirm());
                afterAllAnimationDoneTask = () -> {
                    handelExitAsFinishMatch(toHistory());
                };
            } else {
                if (switchTeamEvent != null) {
                    blockingAnimationTaskQueue.add(() -> callShowTeamSwitchAnimation(switchTeamEvent));
                }
                
                afterAllAnimationDoneTask = () -> {
                    // --- quiz logic ---
                    handleNewQuestion();
                };
            }
            checkBlockingAnimationTaskQueue();
            
            
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
    
    private void rebuildUI() {
        
        backUiStage.clear();
        uiRootTable.clear();
        popupRootTable.clear();
        
        backImage = new Image(game.getTextureConfig().getPlayScreenBackground());
        backImage.setBounds(0, 0, game.getWidth(), game.getHeight());
        backUiStage.addActor(backImage);
        
        
        countdownClockVM = new CountdownClockVM(
                game,
                this,
                this.logicFrameHelper,
                new TextureRegionDrawable(game.getTextureConfig().getCountdownClockTexture())
                );
        countdownClockVM.setBounds(10, 650, 150, 150);
        uiRootTable.addActor(countdownClockVM);
//        uiRootTable.add(countdownClockVM)
//                .center()
//                .colspan(1)
//                .expand()
//                ;
     
        questionStemVM = new QuestionStemVM(
                game,
                new TextureRegionDrawable(game.getTextureConfig().getQuestionStemBackground())
                );
        questionStemVM.setBounds(230, 600, 830, 280);
        uiRootTable.addActor(questionStemVM);
//        uiRootTable.add(questionStemVM)
//                .center()
//                .colspan(3)
//                .expand()
//                .width(730)
//                .height(300)
//                ;
        
        teamInfoBoardVM = new TeamInfoBoardVM(
                game,
                DrawableFactory.getSimpleBoardBackground()
                );
        teamInfoBoardVM.setBounds(1100, 600, 350, 280);
        uiRootTable.addActor(teamInfoBoardVM);
//        uiRootTable.add(teamInfoBoardVM)
//                .center()
//                .colspan(2)
//                .expand()
//                ;
        
        
        systemBoardVM = new SystemBoardVM(
                game,
                this,
                DrawableFactory.getSimpleBoardBackground()
                );
        systemBoardVM.setBounds(1500, 600, 100, 280);
        uiRootTable.addActor(systemBoardVM);

        
        imageAreaVM = new ImageAreaVM(
                game,
                DrawableFactory.getSimpleBoardBackground()
                );
        imageAreaVM.setBounds(0, 0, 400, 600);
        uiRootTable.addActor(imageAreaVM);
//        uiRootTable.add(imageAreaVM)
//                .center()
//                .colspan(2)
//                .expand()
//                ;
        
        questionOptionAreaVM = new QuestionOptionAreaVM(
                game, 
                this,
                DrawableFactory.getSimpleBoardBackground()
                );
        questionOptionAreaVM.setBounds(450, 50, 500, 500);
        uiRootTable.addActor(questionOptionAreaVM);
//        uiRootTable.add(questionOptionAreaVM)
//                .center()
//                .colspan(3)
//                .expand()
//                ;

        skillBoardVM = new SkillBoardVM(
                game, 
                this,
                DrawableFactory.getSimpleBoardBackground()
                );
        skillBoardVM.setBounds(1000, 50, 500, 500);
        uiRootTable.addActor(skillBoardVM);
//        uiRootTable.add(skillBoardVM)
//                .center()
//                .colspan(1)
//                .expand()
//                ;
        
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
                DrawableFactory.getSimpleBoardBackground((int) (game.getWidth() * maskBoardScale), (int) (game.getHeight() * maskBoardScale))
                );
        waitConfirmMatchSituationMaskBoardVM = new WaitConfirmMatchSituationMaskBoardVM(
                game, 
                this, 
                DrawableFactory.getSimpleBoardBackground((int) (game.getWidth() * maskBoardScale), (int) (game.getHeight() * maskBoardScale))
                );
        waitConfirmMatchFinishMaskBoardVM = new WaitConfirmMatchFinishMaskBoardVM(
                game, 
                this, 
                DrawableFactory.getSimpleBoardBackground((int) (game.getWidth() * maskBoardScale), (int) (game.getHeight() * maskBoardScale))
                );
        questionResultAnimationVM = new QuestionResultAnimationVM(game, this);
        teamSwitchAnimationVM = new TeamSwitchAnimationVM(game, this);
        
        if (game.debugMode) {
            uiStage.setDebugAll(true);
        }
        
    }


    private void checkBlockingAnimationTaskQueue() {
        if (blockingAnimationTaskQueue.size() > 0) {
            blockingAnimationTaskQueue.remove(0).run();
        } else if (afterAllAnimationDoneTask != null) {
            Runnable temp = afterAllAnimationDoneTask;
            afterAllAnimationDoneTask = null;
            temp.run();
        }
    }

    @Override
    public void onChooseSkill(int index) {
        Gdx.app.log(this.getClass().getSimpleName(), "onChooseSkill called");
        
    }

    @Override
    public void onChooseSystem(SystemButtonType type) {
        Gdx.app.log(this.getClass().getSimpleName(), "onChooseSystem called");
        switch (type) {
            case SHOW_MATCH_SITUATION:
                callShowMatchSituationConfirm();
                break;
            case EXIT_AS_DISCARD_MATCH:
                handelExitAsDiscardMatch();
                break;
            case EXIT_AS_FINISH_MATCH:
                callShowMatchFinishConfirm();
                afterAllAnimationDoneTask = () -> {
                    handelExitAsFinishMatch(toHistory());
                };
                break;
            default:
                break;
        }

        
    }
    
    private void exitClear() {
        matchConfig = null;
        currentMatchSituationView = null;
        blockingAnimationTaskQueue.clear();
        afterAllAnimationDoneTask = null;
        afterComfirmTask = null;
    }

    private void handelExitAsDiscardMatch() {
        exitClear();
        game.getScreenManager().pushScreen(TeamScreen.class.getSimpleName(), "blending_transition");
    }

    private void handelExitAsFinishMatch(MatchFinishHistory history) {
        exitClear();
        game.getScreenManager().pushScreen(HistoryScreen.class.getSimpleName(), 
                "blending_transition",
                history
                );
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
        
        checkBlockingAnimationTaskQueue();
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
        teamInfoBoardVM.updateTeam(currentTeamPrototype, teamPrototypes);
        skillBoardVM.updateRole(currentTeamPrototype.getRolePrototype(), currentMatchSituationView.getCurrentTeamRuntimeInfo().getRoleRuntimeInfo());
    }

    @Override
    public void callShowMatchFinishConfirm() {
        Gdx.app.log(this.getClass().getSimpleName(), "callShowMatchFinishConfirm called");
        // --- ui ---
        
        popupRootTable.add(waitConfirmMatchFinishMaskBoardVM);
        // --- quiz logic ---
        MatchFinishHistory history = toHistory();
        afterComfirmTask = () -> {
            handelExitAsFinishMatch(history);
        };
        
        // --- screen logic ---
        Gdx.input.setInputProcessor(popupUiStage);
        
        waitConfirmMatchFinishMaskBoardVM.onCallShow(history);
    }
    
    private MatchFinishHistory toHistory() {
        
        MatchFinishHistory history = new MatchFinishHistory();
        history.setData(currentMatchSituationView.getTeamRuntimeInfos().stream()
                .collect(Collectors.toMap(
                        teamRuntimeInfo -> teamRuntimeInfo.getName(), 
                        teamRuntimeInfo -> teamRuntimeInfo.getMatchScore()
                        ))
                );
        return history;
    }
    

}
