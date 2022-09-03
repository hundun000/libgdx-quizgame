package hundun.gdxgame.quizgame.core.domain.viewmodel.playscreen.mask;

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
public class QuestionResultMaskBoardVM extends Image {


    CallerAndCallback callback;
    @Getter
    boolean runningState;
    
    
    // Constant rows and columns of the sprite sheet
    private static final int FRAME_COLS = 6, FRAME_ROWS = 5;

    // Objects used
    Animation<Drawable> animation; // Must declare frame type (TextureRegion)
    

    // A variable for tracking elapsed time for the animation
    float stateTime;
    
    
    public QuestionResultMaskBoardVM(
            QuizGdxGame game,
            CallerAndCallback callback
            ) {
        this.callback = callback;

        
        Texture walkSheet;
        walkSheet = game.getTextureConfig().getQuestionResultAnimationSheet();

        // Use the split utility method to create a 2D array of TextureRegions. This is
        // possible because this sprite sheet contains frames of equal size and they are
        // all aligned.
        TextureRegion[][] tmp = TextureRegion.split(walkSheet,
                walkSheet.getWidth() / FRAME_COLS,
                walkSheet.getHeight() / FRAME_ROWS);

        // Place the regions into a 1D array in the correct order, starting from the top
        // left, going across first. The Animation constructor requires a 1D array.
        Drawable[] walkFrames = new Drawable[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index++] = new TextureRegionDrawable(tmp[i][j]);
            }
        }

        // Initialize the Animation with the frame interval and array of frames
        animation = new Animation<>(0.025f, walkFrames);

        
    }
    
    
    public void callShow(AnswerResultEvent answerResultEvent) {
        // Instantiate a SpriteBatch for drawing and reset the elapsed animation
        // time to 0
        runningState = true;
        stateTime = 0f; 
        Drawable currentFrame = animation.getKeyFrame(stateTime);
        this.setDrawable(currentFrame);
    }
    
    
    public static interface CallerAndCallback {
        void callShowQuestionResultAnimation(AnswerResultEvent answerResultEvent);
        void onQuestionResultAnimationDone();
    }


    public void update(float delta, SpriteBatch spriteBatch) {
        stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time

        if (!animation.isAnimationFinished(stateTime)) {
            Drawable currentFrame = animation.getKeyFrame(stateTime);
            this.setDrawable(currentFrame);
        } else {
            runningState = false;
            callback.onQuestionResultAnimationDone();
        }

    }
}
