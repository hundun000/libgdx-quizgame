package hundun.gdxgame.quizgame.core.viewmodel.playscreen.popup;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Scaling;

import hundun.gdxgame.quizgame.core.QuizGdxGame;

/**
 * @author hundun
 * Created on 2021/11/12
 */
public class GeneralDelayAnimationVM extends AbstractAnimationVM<Float> {
    
    CallerAndCallback callerAndCallback;
    
    public GeneralDelayAnimationVM(
            QuizGdxGame game,
            CallerAndCallback callerAndCallback
            ) {
        super(game, callerAndCallback);
        this.callerAndCallback = callerAndCallback;
    }
    
    @Override
    public void callShow(Float second) {
        Animation<Drawable> animation = aminationFactoryBySumTime(
                game.getTextureConfig().getPlayScreenAnimationsTextureAtlas(), 
                "delay", 
                second, 
                PlayMode.NORMAL
                );
        
        setAnimation(animation);
        
        super.resetFrame();
    }

    public static interface CallerAndCallback extends IAnimationCallback {
        void callShowGeneralDelayAnimation(float second);
    }

}
