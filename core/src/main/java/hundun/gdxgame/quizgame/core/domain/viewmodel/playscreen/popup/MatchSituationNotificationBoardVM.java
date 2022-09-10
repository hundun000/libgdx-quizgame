package hundun.gdxgame.quizgame.core.domain.viewmodel.playscreen.popup;

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
import hundun.quizlib.view.match.MatchSituationView;

/**
 * @author hundun
 * Created on 2021/11/12
 */
public class MatchSituationNotificationBoardVM extends AbstractNotificationBoardVM<MatchSituationView> {

    MatchSituationView data;


    public MatchSituationNotificationBoardVM(
            QuizGdxGame game,
            CallerAndCallback callback,
            Drawable background
            ) {
        super(game, callback, background);

    }
    
    @Override
    public void onCallShow(MatchSituationView data) {
        //this.setVisible(true);
        this.data = data;
        
        // --- render data ---
        
        
    }
    
    
    public static interface CallerAndCallback extends IWaitConfirmNotificationCallback {
        void callShowMatchSituationConfirm();
    }
}
