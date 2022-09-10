package hundun.gdxgame.quizgame.core.domain.viewmodel.playscreen.popup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.quizlib.prototype.event.AnswerResultEvent;
import hundun.quizlib.prototype.match.MatchConfig;
import hundun.quizlib.view.match.MatchSituationView;
import lombok.Getter;

/**
 * @author hundun
 * Created on 2021/11/12
 */
public class QuestionResultAnimationVM extends AbstractAnimationVM {


    CallerAndCallback callerAndCallback;
    Label resultLable;
    
    
    // Constant rows and columns of the sprite sheet
    private static final int FRAME_COLS = 6, FRAME_ROWS = 5;


    
    
    public QuestionResultAnimationVM(
            QuizGdxGame game,
            CallerAndCallback callerAndCallback
            ) {
        super(game, callerAndCallback);
        this.callerAndCallback = callerAndCallback;

        resultLable = new Label("TEMP", game.getMainSkin());
        this.add(resultLable);
    }
    
    
    public void callShow(AnswerResultEvent answerResultEvent) {
        
        setAnimation(new Animation<>(0.025f, aminationFactory(
                game.getTextureConfig().getQuestionResultCorrectAnimationSheet(), 
                FRAME_COLS, FRAME_ROWS
                )));
        resultLable.setText(answerResultEvent.getResult().name());
        
        super.resetBackground();
    }
    
    
    public static interface CallerAndCallback extends IAnimationCallback {
        void callShowQuestionResultAnimation(AnswerResultEvent answerResultEvent);
    }


    
}
