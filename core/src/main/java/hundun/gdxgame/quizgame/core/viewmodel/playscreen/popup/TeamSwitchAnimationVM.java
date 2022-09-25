package hundun.gdxgame.quizgame.core.viewmodel.playscreen.popup;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.quizlib.prototype.event.SwitchTeamEvent;

/**
 * @author hundun
 * Created on 2021/11/12
 */
public class TeamSwitchAnimationVM extends AbstractAnimationVM<SwitchTeamEvent> {

    CallerAndCallback callerAndCallback;
    Label resultLable;

    public TeamSwitchAnimationVM(
            QuizGdxGame game,
            CallerAndCallback callerAndCallback
            ) {
        super(game, callerAndCallback);
        this.callerAndCallback = callerAndCallback;

        
        resultLable = new Label("TEMP", game.getMainSkin());
        this.add(resultLable);
    }
    
    @Override
    public void callShow(SwitchTeamEvent switchTeamEvent) {

        // Initialize the Animation with the frame interval and array of frames
        setAnimation(aminationFactory(
                game.getTextureConfig().getAnimationsTextureAtlas(), 
                "teamSwitch", 0.25f, PlayMode.REVERSED
                ));
        resultLable.setText(switchTeamEvent.getToTeamName());
        
        super.resetFrame();
    }
    
    
    public static interface CallerAndCallback extends IAnimationCallback {
        void callShowTeamSwitchAnimation(SwitchTeamEvent switchTeamEvent);
    }


    
}
