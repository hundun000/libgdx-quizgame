package hundun.gdxgame.quizgame.core.viewmodel.playscreen.popup;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.quizlib.prototype.event.AnswerResultEvent;
import hundun.quizlib.prototype.match.AnswerType;

/**
 * @author hundun
 * Created on 2021/11/12
 */
public class QuestionResultAnimationVM extends AbstractAnimationVM<AnswerResultEvent> {

    CallerAndCallback callerAndCallback;

    public QuestionResultAnimationVM(
            QuizGdxGame game,
            CallerAndCallback callerAndCallback
            ) {
        super(game, callerAndCallback);
        this.callerAndCallback = callerAndCallback;
        
    }
    
    @Override
    public void callShow(AnswerResultEvent answerResultEvent) {
        
        Animation<Drawable> animation;
        if (answerResultEvent.getResult() == AnswerType.CORRECT) {
            animation = aminationFactory(
                    game.getTextureConfig().getPlayScreenAnimationsTextureAtlas(), 
                    "break", 0.25f, PlayMode.REVERSED
                    ); 
        } else if (answerResultEvent.getResult() == AnswerType.WRONG 
                || answerResultEvent.getResult() == AnswerType.SKIPPED) {
            animation = aminationFactory(
                    game.getTextureConfig().getPlayScreenAnimationsTextureAtlas(), 
                    "continue", 0.25f, PlayMode.REVERSED
                    );
        } else if (answerResultEvent.getResult() == AnswerType.TIMEOUOT_WRONG) {
            animation = aminationFactory(
                    game.getTextureConfig().getPlayScreenAnimationsTextureAtlas(), 
                    "timeout", 0.25f, PlayMode.REVERSED
                    );
        } else {
            throw new RuntimeException("cannot handle AnswerType = " + answerResultEvent.getResult());
        }
        
        setAnimation(animation);
        super.resetFrame();
    }
    
    
    public static interface CallerAndCallback extends IAnimationCallback {
        void callShowQuestionResultAnimation(AnswerResultEvent answerResultEvent);
    }


    
}
