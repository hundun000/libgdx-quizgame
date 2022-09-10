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
import hundun.gdxgame.quizgame.core.domain.viewmodel.playscreen.QuestionResourceAreaVM;
import hundun.gdxgame.quizgame.core.domain.viewmodel.playscreen.QuestionResourceAreaVM.IAudioCallback;
import hundun.gdxgame.quizgame.core.domain.viewmodel.playscreen.QuestionOptionAreaVM;
import hundun.gdxgame.quizgame.core.domain.viewmodel.playscreen.QuestionStemVM;
import hundun.gdxgame.quizgame.core.domain.viewmodel.playscreen.SkillBoardVM;
import hundun.gdxgame.quizgame.core.domain.viewmodel.playscreen.TeamInfoBoardVM;
import hundun.gdxgame.quizgame.core.domain.viewmodel.playscreen.SystemBoardVM;
import hundun.gdxgame.quizgame.core.domain.viewmodel.playscreen.SystemBoardVM.SystemButtonType;
import hundun.gdxgame.quizgame.core.domain.viewmodel.playscreen.popup.AbstractAnimationVM;
import hundun.gdxgame.quizgame.core.domain.viewmodel.playscreen.popup.AbstractNotificationBoardVM;
import hundun.gdxgame.quizgame.core.domain.viewmodel.playscreen.popup.GeneralDelayAnimationVM;
import hundun.gdxgame.quizgame.core.domain.viewmodel.playscreen.popup.QuestionResultAnimationVM;
import hundun.gdxgame.quizgame.core.domain.viewmodel.playscreen.popup.SkillAnimationVM;
import hundun.gdxgame.quizgame.core.domain.viewmodel.playscreen.popup.TeamSwitchAnimationVM;
import hundun.gdxgame.quizgame.core.domain.viewmodel.playscreen.popup.FirstGetQuestionNotificationBoardVM;
import hundun.gdxgame.quizgame.core.domain.viewmodel.playscreen.popup.MatchFinishNotificationBoardVM;
import hundun.gdxgame.quizgame.core.domain.viewmodel.playscreen.popup.MatchSituationNotificationBoardVM;
import hundun.gdxgame.quizgame.core.screen.HistoryScreen.MatchFinishHistory;
import hundun.gdxgame.share.base.BaseHundunScreen;
import hundun.gdxgame.share.base.LogicFrameHelper;
import hundun.gdxgame.share.base.util.DrawableFactory;
import hundun.gdxgame.share.base.util.JavaFeatureForGwt;
import hundun.quizlib.exception.QuizgameException;
import hundun.quizlib.model.domain.QuestionModel;
import hundun.quizlib.prototype.TeamPrototype;
import hundun.quizlib.prototype.event.AnswerResultEvent;
import hundun.quizlib.prototype.event.MatchFinishEvent;
import hundun.quizlib.prototype.event.SkillResultEvent;
import hundun.quizlib.prototype.event.StartMatchEvent;
import hundun.quizlib.prototype.event.SwitchQuestionEvent;
import hundun.quizlib.prototype.event.SwitchTeamEvent;
import hundun.quizlib.prototype.match.MatchConfig;
import hundun.quizlib.prototype.skill.SkillSlotPrototype;
import hundun.quizlib.service.BuiltinSkillSlotPrototypeFactory;
import hundun.quizlib.service.GameService;
import hundun.quizlib.view.match.MatchSituationView;
import lombok.Setter;

/**
 * @author hundun
 * Created on 2022/08/30
 */
public class QuizPlayScreen extends BaseHundunScreen<QuizGdxGame, QuizRootSaveData> implements 
        CountdownClockVM.CallerAndCallback, 
        QuestionOptionAreaVM.CallerAndCallback, 
        SystemBoardVM.CallerAndCallback,
        SkillBoardVM.CallerAndCallback, 
        IAudioCallback
{
    

    private final GameService quizLib;
    // --- inner class ---
    private final SkillEffectHandler skillEffectHandler = new SkillEffectHandler();
    private final BlockingAnimationQueueHandler animationQueueHandler = new BlockingAnimationQueueHandler(); 
    private final AnimationCallerAndCallbackDelegation animationCallerAndCallback = new AnimationCallerAndCallbackDelegation();
    private final NotificationCallerAndCallbackDelegation notificationCallerAndCallback = new NotificationCallerAndCallbackDelegation();
    
    // --- for quizLib ---
    MatchConfig matchConfig;
    List<TeamPrototype> teamPrototypes;
    
    // ====== onShowLazyInit ======
    Image backImage;
    MatchSituationView currentMatchSituationView;
    CountdownClockVM countdownClockVM;
    QuestionStemVM questionStemVM;
    TeamInfoBoardVM teamInfoBoardVM;
    SystemBoardVM systemBoardVM;
    QuestionResourceAreaVM questionResourceAreaVM;
    QuestionOptionAreaVM questionOptionAreaVM;
    SkillBoardVM skillBoardVM;
    
    // --- lazy add to stage ---
    FirstGetQuestionNotificationBoardVM waitConfirmFirstGetQuestionMaskBoardVM;
    MatchSituationNotificationBoardVM waitConfirmMatchSituationMaskBoardVM;
    MatchFinishNotificationBoardVM waitConfirmMatchFinishMaskBoardVM;
    QuestionResultAnimationVM questionResultAnimationVM;
    TeamSwitchAnimationVM teamSwitchAnimationVM;
    SkillAnimationVM skillAnimationVM;
    GeneralDelayAnimationVM generalDelayAnimationVM;
    
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

        
        questionResourceAreaVM = new QuestionResourceAreaVM(
                game,
                this,
                DrawableFactory.getSimpleBoardBackground(),
                DrawableFactory.getSimpleBoardBackground(),
                DrawableFactory.getSimpleBoardBackground()
                );
        questionResourceAreaVM.setBounds(0, 0, 400, 600);
        uiRootTable.addActor(questionResourceAreaVM);
//        uiRootTable.add(imageAreaVM)
//                .center()
//                .colspan(2)
//                .expand()
//                ;
        
        questionOptionAreaVM = new QuestionOptionAreaVM(
                game, 
                this,
                new TextureRegionDrawable(game.getTextureConfig().getOptionButtonBackground())
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
        
        waitConfirmFirstGetQuestionMaskBoardVM = new FirstGetQuestionNotificationBoardVM(
                game, 
                notificationCallerAndCallback, 
                DrawableFactory.getSimpleBoardBackground((int) (game.getWidth() * maskBoardScale), (int) (game.getHeight() * maskBoardScale))
                );
        waitConfirmMatchSituationMaskBoardVM = new MatchSituationNotificationBoardVM(
                game, 
                notificationCallerAndCallback, 
                DrawableFactory.getSimpleBoardBackground((int) (game.getWidth() * maskBoardScale), (int) (game.getHeight() * maskBoardScale))
                );
        waitConfirmMatchFinishMaskBoardVM = new MatchFinishNotificationBoardVM(
                game, 
                notificationCallerAndCallback, 
                DrawableFactory.getSimpleBoardBackground((int) (game.getWidth() * maskBoardScale), (int) (game.getHeight() * maskBoardScale))
                );
        questionResultAnimationVM = new QuestionResultAnimationVM(game, animationCallerAndCallback);
        teamSwitchAnimationVM = new TeamSwitchAnimationVM(game, animationCallerAndCallback);
        skillAnimationVM = new SkillAnimationVM(game, animationCallerAndCallback);
        generalDelayAnimationVM = new GeneralDelayAnimationVM(game, animationCallerAndCallback);
        
        if (game.debugMode) {
            uiStage.setDebugAll(true);
        }
        
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
        
        
        notificationCallerAndCallback.callShowFirstGetQuestionConfirm();
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
        questionResourceAreaVM.updateQuestion(currentMatchSituationView.getQuestion());
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
            questionOptionAreaVM.showAllOption();
            
            animationQueueHandler.addAnimationTask(() -> animationCallerAndCallback.callShowQuestionResultAnimation(answerResultEvent));
            animationQueueHandler.addAnimationTask(() -> animationCallerAndCallback.callShowGeneralDelayAnimation(3.0f));
            
            if (matchFinishEvent != null) {
                animationQueueHandler.addAnimationTask(() -> notificationCallerAndCallback.callShowMatchFinishConfirm());
                animationQueueHandler.setAfterAllAnimationDoneTask(() -> {
                            handelExitAsFinishMatch(toHistory());
                        });
            } else {
                if (switchTeamEvent != null) {
                    animationQueueHandler.addAnimationTask(() -> animationCallerAndCallback.callShowTeamSwitchAnimation(switchTeamEvent));
                }
                animationQueueHandler.setAfterAllAnimationDoneTask(() -> {
                            // --- quiz logic ---
                            if (switchTeamEvent != null) {
                                handleCurrentTeam();
                            }
                            handleNewQuestion();
                        });
            }
            animationQueueHandler.checkNextAnimation();
            
            
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
    public void onChooseSkill(int index) {
        Gdx.app.log(this.getClass().getSimpleName(), "onChooseSkill called");
        TeamPrototype currentTeamPrototype = teamPrototypes.get(currentMatchSituationView.getCurrentTeamIndex());
        SkillSlotPrototype skillSlotPrototype = currentTeamPrototype.getRolePrototype().getSkillSlotPrototypes().get(index);
        try {
            currentMatchSituationView = quizLib.teamUseSkill(currentMatchSituationView.getId(), skillSlotPrototype.getName());
            SkillResultEvent skillResultEvent = JavaFeatureForGwt.requireNonNull(currentMatchSituationView.getSkillResultEvent());
            Gdx.app.log(this.getClass().getSimpleName(), JavaFeatureForGwt.stringFormat(
                    "skillResultEvent by Type = %s", 
                    skillResultEvent.getType()
                    ));
            animationQueueHandler.addAnimationTask(() -> animationCallerAndCallback.callShowSkillAnimation(skillResultEvent));
            animationQueueHandler.setAfterAllAnimationDoneTask(() -> {
                        skillBoardVM.updateSkill(index, skillResultEvent.getSkillRemainTime());
                        skillEffectHandler.handle(skillResultEvent);
                    });
            animationQueueHandler.checkNextAnimation();
            
        } catch (QuizgameException e) {
            Gdx.app.error(this.getClass().getSimpleName(), e.getClass().getSimpleName() + ": " + e.getMessage());
        }
        
        
        
    }

    @Override
    public void onChooseSystem(SystemButtonType type) {
        Gdx.app.log(this.getClass().getSimpleName(), "onChooseSystem called");
        switch (type) {
            case SHOW_MATCH_SITUATION:
                notificationCallerAndCallback.callShowMatchSituationConfirm();
                break;
            case EXIT_AS_DISCARD_MATCH:
                handelExitAsDiscardMatch();
                break;
            case EXIT_AS_FINISH_MATCH:
                notificationCallerAndCallback.callShowMatchFinishConfirm();
                break;
            default:
                break;
        }

        
    }
    
    private void exitClear() {
        matchConfig = null;
        currentMatchSituationView = null;
        
        animationQueueHandler.clear();
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
    

    
    
    
    private void handleCurrentTeam() {
        TeamPrototype currentTeamPrototype = teamPrototypes.get(currentMatchSituationView.getCurrentTeamIndex());
        teamInfoBoardVM.updateTeam(currentTeamPrototype, teamPrototypes);
        skillBoardVM.updateRole(currentTeamPrototype.getRolePrototype(), currentMatchSituationView.getCurrentTeamRuntimeInfo().getRoleRuntimeInfo());
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

    
    
    
    
    @Override
    protected void renderPopupAnimations(float delta, SpriteBatch spriteBatch) {
        animationQueueHandler.render(delta, spriteBatch);
    }
    
    class SkillEffectHandler {

        public void handle(SkillResultEvent skillResultEvent) {
            switch (skillResultEvent.getSkillName()) {
                case BuiltinSkillSlotPrototypeFactory.SKILL_NAME_5050:
                    this.handle5050(skillResultEvent.getArgs()); 
                    break;
                default:
                    Gdx.app.error(this.getClass().getSimpleName(), JavaFeatureForGwt.stringFormat(
                            "unhandle SkillName = %s", 
                            skillResultEvent.getSkillName()
                            ));
                break;
            }
        }
        
        private void handle5050(List<String> args) {
            int showOptionAmout = Integer.valueOf(args.get(0));
            questionOptionAreaVM.showRandomOption(showOptionAmout);
        }
        
    }

    class NotificationCallerAndCallbackDelegation implements 
            FirstGetQuestionNotificationBoardVM.CallerAndCallback,
            MatchSituationNotificationBoardVM.CallerAndCallback, 
            MatchFinishNotificationBoardVM.CallerAndCallback {
        
        Runnable afterComfirmTask;
        
        @Override
        public void onNotificationConfirmed() {
            Gdx.app.log(this.getClass().getSimpleName(), "onConfirmed called");
            // --- for screen ---
            popupRootTable.clear();
            Gdx.input.setInputProcessor(uiStage);
            logicFrameHelper.setLogicFramePause(false);
            // --- for notificationBoardVM ---
            if (afterComfirmTask != null) {
                Gdx.app.log(this.getClass().getSimpleName(), "has afterComfirmTask");
                Runnable temp = afterComfirmTask;
                afterComfirmTask = null;
                temp.run();
            }
        }
        

        @Override
        public void callShowMatchFinishConfirm() {
            MatchFinishHistory history = toHistory();
            generalCallShowNotificationBoard(
                    waitConfirmMatchFinishMaskBoardVM, 
                    history, 
                    () -> {
                        handelExitAsFinishMatch(history);
                    }
                    );
        }
     
        @Override
        public void callShowFirstGetQuestionConfirm() {
            generalCallShowNotificationBoard(
                    waitConfirmFirstGetQuestionMaskBoardVM, 
                    matchConfig, 
                    () -> {
                        handleNewQuestion();
                    }
                    );
        }


        @Override
        public void callShowMatchSituationConfirm() {
            generalCallShowNotificationBoard(
                    waitConfirmMatchSituationMaskBoardVM, 
                    currentMatchSituationView, 
                    null
                    );
        }

        private <T> void generalCallShowNotificationBoard(
                AbstractNotificationBoardVM<T> notificationBoardVM, 
                T arg,
                Runnable afterComfirmTask
                ) {
            Gdx.app.log(this.getClass().getSimpleName(), JavaFeatureForGwt.stringFormat(
                    "generalCallShowNotificationBoard called, notificationBoardVM = %s", 
                    notificationBoardVM.getClass().getSimpleName()
                    ));
            // --- for screen ---
            popupRootTable.add(notificationBoardVM);
            logicFrameHelper.setLogicFramePause(true);
            Gdx.input.setInputProcessor(popupUiStage);
            // --- for notificationBoardVM ---
            this.afterComfirmTask = afterComfirmTask;
            notificationBoardVM.onCallShow(arg);
        }
    }
    
    class AnimationCallerAndCallbackDelegation implements 
            QuestionResultAnimationVM.CallerAndCallback,
            TeamSwitchAnimationVM.CallerAndCallback,
            SkillAnimationVM.CallerAndCallback,
            GeneralDelayAnimationVM.CallerAndCallback {
        
        @Override
        public void callShowGeneralDelayAnimation(float second) {
            generalCallShowAnimation(generalDelayAnimationVM, second);
        }

        @Override
        public void callShowQuestionResultAnimation(AnswerResultEvent answerResultEvent) {
            generalCallShowAnimation(questionResultAnimationVM, answerResultEvent);
        }

        @Override
        public void onAnimationDone() {
            Gdx.app.log(this.getClass().getSimpleName(), "onAnimationDone called");
            // --- for screen ---
            popupRootTable.clear();
            Gdx.input.setInputProcessor(uiStage);
            logicFrameHelper.setLogicFramePause(false);
            // --- for animationVM ---
            animationQueueHandler.setCurrentAnimationVM(null);
            animationQueueHandler.checkNextAnimation();
        }
        
        @Override
        public void callShowSkillAnimation(SkillResultEvent skillResultEvent) {
            generalCallShowAnimation(skillAnimationVM, skillResultEvent);
        }
        
        private <T> void generalCallShowAnimation(AbstractAnimationVM<T> animationVM, T arg) {
            Gdx.app.log(this.getClass().getSimpleName(), JavaFeatureForGwt.stringFormat(
                    "generalCallShowAnimation called, animationVM = %s", 
                    animationVM.getClass().getSimpleName()
                    ));
            // --- for screen ---
            popupRootTable.add(animationVM);
            Gdx.input.setInputProcessor(popupUiStage);
            logicFrameHelper.setLogicFramePause(true);
            // --- for animationVM ---
            animationQueueHandler.setCurrentAnimationVM(animationVM);
            animationVM.callShow(arg);
        }
        
        @Override
        public void callShowTeamSwitchAnimation(SwitchTeamEvent switchTeamEvent) {
            generalCallShowAnimation(teamSwitchAnimationVM, switchTeamEvent);
        }
    }
    
    static class BlockingAnimationQueueHandler {
        private List<Runnable> blockingAnimationTaskQueue = new LinkedList<>();
        @Setter
        private Runnable afterAllAnimationDoneTask;
        @Setter
        private AbstractAnimationVM<?> currentAnimationVM;
        
        public void addAnimationTask(Runnable animationTask) {
            this.blockingAnimationTaskQueue.add(animationTask);
        }

        public void checkNextAnimation() {
            if (blockingAnimationTaskQueue.size() > 0) {
                blockingAnimationTaskQueue.remove(0).run();
            } else if (afterAllAnimationDoneTask != null) {
                Runnable temp = afterAllAnimationDoneTask;
                afterAllAnimationDoneTask = null;
                temp.run();
            }
        }
        
        public void clear() {
            blockingAnimationTaskQueue.clear();
            afterAllAnimationDoneTask = null;
        }
        
        public void render(float delta, SpriteBatch spriteBatch) {
            if (currentAnimationVM != null) {
                currentAnimationVM.updateFrame(delta, spriteBatch);
            }
        }
    }

    @Override
    public void onFirstPlayDone() {
        Gdx.app.log(this.getClass().getSimpleName(), "onFirstPlayDone called");
        logicFrameHelper.setLogicFramePause(false);
    }

    @Override
    public void onPlayReady() {
        Gdx.app.log(this.getClass().getSimpleName(), "onPlayReady called");
        logicFrameHelper.setLogicFramePause(true);
    }
    

}
