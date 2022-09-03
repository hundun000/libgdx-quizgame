package hundun.gdxgame.quizgame.core.domain.viewmodel.playscreen.mask;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.quizlib.prototype.event.AnswerResultEvent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author hundun
 * Created on 2022/09/07
 */
public abstract class AbstractAnimationVM extends Table {
    private final IAnimationCallback callback;
    
    protected final QuizGdxGame game;
    @Getter
    private boolean runningState;
    
    // Objects used
    @Setter(value = AccessLevel.PROTECTED)
    private Animation<Drawable> animation; // Must declare frame type (TextureRegion)
    

    // A variable for tracking elapsed time for the animation
    private float stateTime;
    
    public AbstractAnimationVM(QuizGdxGame game, IAnimationCallback callback) {
        this.game = game;
        this.callback = callback;
    }
    
    public void resetBackground() {
        // Instantiate a SpriteBatch for drawing and reset the elapsed animation
        // time to 0
        runningState = true;
        stateTime = 0f; 
        Drawable currentFrame = animation.getKeyFrame(stateTime);
        this.setBackground(currentFrame);
    }
    
    public void updateBackground(float delta, SpriteBatch spriteBatch) {
        stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time

        if (!animation.isAnimationFinished(stateTime)) {
            Drawable currentFrame = animation.getKeyFrame(stateTime);
            this.setBackground(currentFrame);
        } else {
            runningState = false;
            callback.onAnimationDone();
        }

    }
    
    public static Drawable[] aminationFactory(Texture sheet, int FRAME_COLS, int FRAME_ROWS) {
        
        // Use the split utility method to create a 2D array of TextureRegions. This is
        // possible because this sprite sheet contains frames of equal size and they are
        // all aligned.
        TextureRegion[][] tmp = TextureRegion.split(sheet,
                sheet.getWidth() / FRAME_COLS,
                sheet.getHeight() / FRAME_ROWS);

        // Place the regions into a 1D array in the correct order, starting from the top
        // left, going across first. The Animation constructor requires a 1D array.
        Drawable[] walkFrames = new Drawable[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index++] = new TextureRegionDrawable(tmp[i][j]);
            }
        }
        return walkFrames;
    }
    
    public static interface IAnimationCallback {
        void onAnimationDone();
    }
}
