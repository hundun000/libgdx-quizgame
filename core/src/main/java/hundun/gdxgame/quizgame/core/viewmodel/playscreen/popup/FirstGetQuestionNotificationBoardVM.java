package hundun.gdxgame.quizgame.core.viewmodel.playscreen.popup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.quizlib.prototype.match.MatchConfig;

/**
 * @author hundun
 * Created on 2021/11/12
 */
public class FirstGetQuestionNotificationBoardVM extends AbstractNotificationBoardVM<MatchConfig> {

    MatchConfig data;

    
    
    public FirstGetQuestionNotificationBoardVM(
            QuizGdxGame game,
            CallerAndCallback callback,
            Drawable background
            ) {
        super(game, callback, background);
        
    }
    
    @Override
    public void onCallShow(MatchConfig data) {
        //this.setVisible(true);
        this.data = data;
        
        // --- render data ---
        simpleFill(this);
        
    }
    
    
    public static interface CallerAndCallback extends IWaitConfirmNotificationCallback{
        void callShowFirstGetQuestionConfirm();
    }
}
