package hundun.gdxgame.quizgame.core.screen;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.gdxgame.quizgame.core.domain.QuizRootSaveData;
import hundun.gdxgame.quizgame.core.screen.HistoryScreen.MatchHistoryDTO;
import hundun.gdxgame.quizgame.core.viewmodel.playscreen.CountdownClockVM;
import hundun.gdxgame.quizgame.core.viewmodel.playscreen.QuestionOptionAreaVM;
import hundun.gdxgame.quizgame.core.viewmodel.playscreen.QuestionResourceAreaVM;
import hundun.gdxgame.quizgame.core.viewmodel.playscreen.QuestionStemVM;
import hundun.gdxgame.quizgame.core.viewmodel.playscreen.SkillBoardVM;
import hundun.gdxgame.quizgame.core.viewmodel.playscreen.SystemBoardVM;
import hundun.gdxgame.quizgame.core.viewmodel.playscreen.TeamInfoBoardVM;
import hundun.gdxgame.quizgame.core.viewmodel.playscreen.QuestionResourceAreaVM.IAudioCallback;
import hundun.gdxgame.quizgame.core.viewmodel.playscreen.SystemBoardVM.SystemButtonType;
import hundun.gdxgame.quizgame.core.viewmodel.playscreen.popup.AbstractAnimationVM;
import hundun.gdxgame.quizgame.core.viewmodel.playscreen.popup.AbstractNotificationBoardVM;
import hundun.gdxgame.quizgame.core.viewmodel.playscreen.popup.GeneralDelayAnimationVM;
import hundun.gdxgame.quizgame.core.viewmodel.playscreen.popup.MatchFinishNotificationBoardVM;
import hundun.gdxgame.quizgame.core.viewmodel.playscreen.popup.PauseNotificationBoardVM;
import hundun.gdxgame.quizgame.core.viewmodel.playscreen.popup.QuestionResultAnimationVM;
import hundun.gdxgame.quizgame.core.viewmodel.playscreen.popup.SkillAnimationVM;
import hundun.gdxgame.quizgame.core.viewmodel.playscreen.popup.TeamSwitchAnimationVM;
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
import hundun.quizlib.service.BuiltinDataConfiguration.BuiltinSkill;
import hundun.quizlib.service.GameService;
import hundun.quizlib.view.match.MatchSituationView;
import lombok.Setter;

/**
 * @author hundun
 * Created on 2022/08/30
 */
public class QuizPlayScreen extends BaseHundunScreen<QuizGdxGame, QuizRootSaveData> {
    

    private final GameService quizLib;
    // --- inner class ---
    private final SkillEffectHandler skillEffectHandler = new SkillEffectHandler();
    private final BlockingAnimationQueueHandler animationQueueHandler = new BlockingAnimationQueueHandler(); 
    private final AnimationCallerAndCallbackDelegation animationCallerAndCallback = new AnimationCallerAndCallbackDelegation();
    private final NotificationCallerAndCallbackDelegation notificationCallerAndCallback = new NotificationCallerAndCallbackDelegation();
    private final QuizInputHandler quizInputHandler = new QuizInputHandler(); 
    
    // --- for quizLib ---
    MatchConfig matchConfig;
    List<TeamPrototype> teamPrototypes;
    MatchSituationView currentMatchSituationView;
    
    // ====== onShowLazyInit ======
    Image backImage;

    


    
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
        
        quizInputHandler.handleCreateAndStartMatch();
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
        
        
        quizInputHandler.rebuildUI();
        
        
        if (game.debugMode) {
            uiStage.setDebugAll(true);
        }
        
    }

    



    @Override
    public void dispose() {
    }
    
    @Override
    protected void onLogicFrame() {
        quizInputHandler.onLogicFrame();
        
    }

    
    


    



    

    
    
    
    
    @Override
    protected void renderPopupAnimations(float delta, SpriteBatch spriteBatch) {
        animationQueueHandler.render(delta, spriteBatch);
    }
    
    class QuizInputHandler implements CountdownClockVM.CallerAndCallback, 
            QuestionOptionAreaVM.CallerAndCallback, 
            SystemBoardVM.CallerAndCallback,
            SkillBoardVM.CallerAndCallback, 
            IAudioCallback {
        
        
        
        CountdownClockVM countdownClockVM;
        QuestionStemVM questionStemVM;
        TeamInfoBoardVM teamInfoBoardVM;
        SystemBoardVM systemBoardVM;
        QuestionResourceAreaVM questionResourceAreaVM;
        QuestionOptionAreaVM questionOptionAreaVM;
        SkillBoardVM skillBoardVM;
        
        private void exitClear() {
            matchConfig = null;
            currentMatchSituationView = null;
            
            animationQueueHandler.clear();
        }


       
        private MatchHistoryDTO toHistory() {
            
            MatchHistoryDTO history = new MatchHistoryDTO();
            history.setData(currentMatchSituationView.getTeamRuntimeInfos().stream()
                    .collect(Collectors.toMap(
                            teamRuntimeInfo -> teamRuntimeInfo.getName(), 
                            teamRuntimeInfo -> teamRuntimeInfo.getMatchScore()
                            ))
                    );
            return history;
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
                
                handleCurrentTeam(true);
                quizInputHandler.handleNewQuestion();
            } catch (QuizgameException e) {
                Gdx.app.error(this.getClass().getSimpleName(), "QuizgameException", e);
            }
            
            
            notificationCallerAndCallback.callShowPauseConfirm();
        }

        
        /**
         * @param 一般情况取值ABCD；作为超时传QuestionModel.TIMEOUT_ANSWER_TEXT；作为跳过时传QuestionModel.SKIP_ANSWER_TEXT
         */
        protected void onChooseOptionOrCountdownZero(String ansOrControl) {
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
                questionResourceAreaVM.stopAudio();
                questionOptionAreaVM.showAllOption();
                
                animationQueueHandler.addAnimationTask(() -> animationCallerAndCallback.callShowQuestionResultAnimation(answerResultEvent));
                animationQueueHandler.addAnimationTask(() -> animationCallerAndCallback.callShowGeneralDelayAnimation(3.0f));
                
                if (matchFinishEvent != null) {
                    animationQueueHandler.addAnimationTask(() -> notificationCallerAndCallback.callShowMatchFinishConfirm());
                    animationQueueHandler.setAfterAllAnimationDoneTask(() -> {
                                handleCurrentTeam(false);        
                                handelExitAsFinishMatch(toHistory());
                            });
                } else {
                    if (switchTeamEvent != null) {
                        animationQueueHandler.addAnimationTask(() -> animationCallerAndCallback.callShowTeamSwitchAnimation(switchTeamEvent));
                    }
                    animationQueueHandler.setAfterAllAnimationDoneTask(() -> {
                                // --- quiz logic ---
                                handleCurrentTeam(false);
                                handleNewQuestion();
                            });
                }
                animationQueueHandler.checkNextAnimation();
                
                
            } catch (QuizgameException e) {
                Gdx.app.error(this.getClass().getSimpleName(), "QuizgameException", e);
            }
        }

        private void handelExitAsFinishMatch(MatchHistoryDTO history) {
            exitClear();
            game.getScreenManager().pushScreen(HistoryScreen.class.getSimpleName(), 
                    "blending_transition",
                    history
                    );
        }

        private void handleCurrentTeam(boolean isNewPrototypes) {
            
            if (isNewPrototypes) {
                teamInfoBoardVM.updateTeamPrototype(teamPrototypes);
                TeamPrototype currentTeamPrototype = teamPrototypes.get(currentMatchSituationView.getCurrentTeamIndex());
                skillBoardVM.updateRole(currentTeamPrototype.getRolePrototype(), currentMatchSituationView.getCurrentTeamRuntimeInfo().getRoleRuntimeInfo());
            }
            teamInfoBoardVM.updateTeamRuntime(matchConfig.getMatchStrategyType(), currentMatchSituationView.getCurrentTeamIndex(), currentMatchSituationView.getTeamRuntimeInfos());
        }
        
        protected void rebuildUI() {
            

            countdownClockVM = new CountdownClockVM(
                    game,
                    quizInputHandler,
                    logicFrameHelper,
                    game.getTextureConfig().getPlayScreenUITextureAtlas()
                    );
            countdownClockVM.setBounds(10, 650, 200, 300);
            uiRootTable.addActor(countdownClockVM);
//            uiRootTable.add(countdownClockVM)
//                    .center()
//                    .colspan(1)
//                    .expand()
//                    ;
         
            questionStemVM = new QuestionStemVM(
                    game,
                    game.getTextureConfig().getPlayScreenUITextureAtlas()
                    );
            questionStemVM.setBounds(230, 600, 830, 280);
            uiRootTable.addActor(questionStemVM);
//            uiRootTable.add(questionStemVM)
//                    .center()
//                    .colspan(3)
//                    .expand()
//                    .width(730)
//                    .height(300)
//                    ;
            
            teamInfoBoardVM = new TeamInfoBoardVM(
                    game,
                    game.getTextureConfig().getHistoryAreaVMBackgroundDrawable()
                    );
            teamInfoBoardVM.setBounds(1100, 600, 350, 280);
            uiRootTable.addActor(teamInfoBoardVM);
//            uiRootTable.add(teamInfoBoardVM)
//                    .center()
//                    .colspan(2)
//                    .expand()
//                    ;
            
            
            systemBoardVM = new SystemBoardVM(
                    game,
                    quizInputHandler,
                    game.getTextureConfig().getPlayScreenUITextureAtlas()
                    );
            systemBoardVM.setBounds(1500, 600, 100, 280);
            uiRootTable.addActor(systemBoardVM);

            
            questionResourceAreaVM = new QuestionResourceAreaVM(
                    game,
                    quizInputHandler,
                    game.getTextureConfig().getPlayScreenUITextureAtlas()
                    );
            questionResourceAreaVM.setBounds(0, 0, 450, 600);
            uiRootTable.addActor(questionResourceAreaVM);
//            uiRootTable.add(imageAreaVM)
//                    .center()
//                    .colspan(2)
//                    .expand()
//                    ;
            
            questionOptionAreaVM = new QuestionOptionAreaVM(
                    game, 
                    quizInputHandler,
                    game.getTextureConfig().getPlayScreenUITextureAtlas(),
                    game.getTextureConfig().getMaskTextureAtlas()
                    );
            questionOptionAreaVM.setBounds(450, 50, 700, 500);
            uiRootTable.addActor(questionOptionAreaVM);
//            uiRootTable.add(questionOptionAreaVM)
//                    .center()
//                    .colspan(3)
//                    .expand()
//                    ;

            skillBoardVM = new SkillBoardVM(
                    game, 
                    quizInputHandler,
                    DrawableFactory.getSimpleBoardBackground()
                    );
            skillBoardVM.setBounds(1200, 50, 400, 500);
            uiRootTable.addActor(skillBoardVM);

            if (game.debugMode) {
                uiStage.setDebugAll(true);
            }
            
        }

        
        
        
        protected void onLogicFrame() {
            if (countdownClockVM.isCountdownState()) {
                countdownClockVM.updateCoutdownSecond(-1);
            }
        }

        
        protected void handleNewQuestion() {
            try {
                currentMatchSituationView = quizLib.nextQustion(currentMatchSituationView.getId());
            } catch (QuizgameException e) {
                Gdx.app.error(this.getClass().getSimpleName(), "QuizgameException", e);
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
                            skillBoardVM.updateSkillRuntime(index, skillResultEvent.getSkillRemainTime());
                            skillEffectHandler.handle(skillResultEvent);
                        });
                animationQueueHandler.checkNextAnimation();
                
            } catch (QuizgameException e) {
                Gdx.app.error(this.getClass().getSimpleName(), "QuizgameException", e);
            }
            
            
            
        }

        @Override
        public void onChooseSystem(SystemButtonType type) {
            Gdx.app.log(this.getClass().getSimpleName(), "onChooseSystem called");
            switch (type) {
                case PAUSE:
                    notificationCallerAndCallback.callShowPauseConfirm();
                    break;
                case EXIT:
                    animationQueueHandler.addAnimationTask(() -> notificationCallerAndCallback.callShowMatchFinishConfirm());
                    animationQueueHandler.setAfterAllAnimationDoneTask(() -> {
                                handleCurrentTeam(false);        
                                handelExitAsFinishMatch(toHistory());
                            });
                    animationQueueHandler.checkNextAnimation();
                    break;
                default:
                    break;
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
    
    class SkillEffectHandler {

        public void handle(SkillResultEvent skillResultEvent) {
            if (skillResultEvent.getSkillName() == BuiltinSkill.SKILL_5050.name()) {
                this.handle5050(skillResultEvent.getArgs()); 
            } else if (skillResultEvent.getSkillName() == BuiltinSkill.SKILL_SKIP.name()) {
                this.handleSkip(skillResultEvent.getArgs()); 
            } else if (skillResultEvent.getSkillName() == BuiltinSkill.SKILL_HELP_1.name()) {
                this.handleHelp(skillResultEvent.getArgs()); 
            } else if (skillResultEvent.getSkillName() == BuiltinSkill.SKILL_HELP_2.name()) {
                this.handleHelp(skillResultEvent.getArgs()); 
            } else {
                Gdx.app.error(this.getClass().getSimpleName(), JavaFeatureForGwt.stringFormat(
                        "unhandle SkillName = %s", 
                        skillResultEvent.getSkillName()
                        ));
            }
        }
        
        private void handleHelp(List<String> args) {
            int addSecond = Integer.valueOf(args.get(0));
            quizInputHandler.countdownClockVM.updateCoutdownSecond(addSecond);
        }

        private void handleSkip(List<String> args) {
            quizInputHandler.onChooseOptionOrCountdownZero(QuestionModel.SKIP_ANSWER_TEXT);
        }

        private void handle5050(List<String> args) {
            int showOptionAmout = Integer.valueOf(args.get(0));
            quizInputHandler.questionOptionAreaVM.showRandomOption(showOptionAmout);
        }
        
    }

    class NotificationCallerAndCallbackDelegation implements 
            PauseNotificationBoardVM.CallerAndCallback, 
            MatchFinishNotificationBoardVM.CallerAndCallback {
        
        Runnable afterComfirmTask;
        
        // --- runtime add to stage ---
        PauseNotificationBoardVM pauseNotificationBoardVM;
        MatchFinishNotificationBoardVM waitConfirmMatchFinishMaskBoardVM;
        
        public NotificationCallerAndCallbackDelegation() {
            
            pauseNotificationBoardVM = new PauseNotificationBoardVM(
                    game, 
                    this, 
                    DrawableFactory.getViewportBasedAlphaBoard(game.getWidth(), game.getHeight()),
                    game.getTextureConfig().getPlayScreenUITextureAtlas()
                    );
            waitConfirmMatchFinishMaskBoardVM = new MatchFinishNotificationBoardVM(
                    game, 
                    this, 
                    DrawableFactory.getViewportBasedAlphaBoard(game.getWidth(), game.getHeight())
                    );
        }
        
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
            MatchHistoryDTO history = quizInputHandler.toHistory();
            generalCallShowNotificationBoard(
                    waitConfirmMatchFinishMaskBoardVM, 
                    history, 
                    () -> {
                        quizInputHandler.handelExitAsFinishMatch(history);
                    }
                    );
        }



        @Override
        public void callShowPauseConfirm() {
            generalCallShowNotificationBoard(
                    pauseNotificationBoardVM, 
                    (Void)null, 
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
        
        // --- runtime add to stage ---
        QuestionResultAnimationVM questionResultAnimationVM;
        TeamSwitchAnimationVM teamSwitchAnimationVM;
        SkillAnimationVM skillAnimationVM;
        GeneralDelayAnimationVM generalDelayAnimationVM;
        
        public AnimationCallerAndCallbackDelegation() {
            
            questionResultAnimationVM = new QuestionResultAnimationVM(game, this);
            teamSwitchAnimationVM = new TeamSwitchAnimationVM(game, this);
            skillAnimationVM = new SkillAnimationVM(game, this);
            generalDelayAnimationVM = new GeneralDelayAnimationVM(game, this);
        }
        
        @Override
        public void callShowGeneralDelayAnimation(float second) {
            generalCallShowAnimation(generalDelayAnimationVM, second, true);
        }

        @Override
        public void callShowQuestionResultAnimation(AnswerResultEvent answerResultEvent) {
            generalCallShowAnimation(questionResultAnimationVM, answerResultEvent, true);
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
            generalCallShowAnimation(skillAnimationVM, skillResultEvent, false);
        }
        
        /**
         * popupRootTable-cell always expand(), fill is optional by argument.
         */
        private <T> void generalCallShowAnimation(AbstractAnimationVM<T> animationVM, T arg, boolean fill) {
            Gdx.app.log(this.getClass().getSimpleName(), JavaFeatureForGwt.stringFormat(
                    "generalCallShowAnimation called, animationVM = %s", 
                    animationVM.getClass().getSimpleName()
                    ));
            // --- for screen ---
            Cell<?> cell = popupRootTable.add(animationVM).expand();

            if (fill) {
                cell.fill();
            }
            if (game.debugMode) {
                popupRootTable.debugTable();
            }
            Gdx.input.setInputProcessor(popupUiStage);
            logicFrameHelper.setLogicFramePause(true);
            // --- for animationVM ---
            animationQueueHandler.setCurrentAnimationVM(animationVM);
            animationVM.callShow(arg);
        }
        
        @Override
        public void callShowTeamSwitchAnimation(SwitchTeamEvent switchTeamEvent) {
            generalCallShowAnimation(teamSwitchAnimationVM, switchTeamEvent, true);
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

    
    

}
