package hundun.gdxgame.quizgame.core.viewmodel.playscreen.popup;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.quizlib.prototype.event.SkillResultEvent;

/**
 * @author hundun
 * Created on 2021/11/12
 */
public class SkillAnimationVM extends AbstractAnimationVM<SkillResultEvent> {


    CallerAndCallback callerAndCallback;
    Label resultLable;
    
    
    // Constant rows and columns of the sprite sheet
    private static final int FRAME_COLS = 6, FRAME_ROWS = 5;


    
    
    public SkillAnimationVM(
            QuizGdxGame game,
            CallerAndCallback callerAndCallback
            ) {
        super(game, callerAndCallback);
        this.callerAndCallback = callerAndCallback;

        
        resultLable = new Label("TEMP", game.getMainSkin());
        this.add(resultLable);
        this.setTransform(true);
    }
    
    @Override
    public void callShow(SkillResultEvent skillResultEvent) {

        // TODO different animations by skillName
        setAnimation(aminationFactory( 
                game.getTextureConfig().getSkillAnimationsTextureAtlas(), 
                "skill",
                0.75f, PlayMode.NORMAL
                ));
        resultLable.setText(skillResultEvent.getSkillName() + "\n" + skillResultEvent.getSkillDesc());
        
        super.resetFrame();
    }
    
    
    public static interface CallerAndCallback extends IAnimationCallback {
        void callShowSkillAnimation(SkillResultEvent skillResultEvent);
    }


    
}
