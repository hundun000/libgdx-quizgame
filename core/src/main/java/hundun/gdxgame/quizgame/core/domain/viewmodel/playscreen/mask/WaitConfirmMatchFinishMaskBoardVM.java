package hundun.gdxgame.quizgame.core.domain.viewmodel.playscreen.mask;

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
import hundun.gdxgame.quizgame.core.domain.viewmodel.share.MatchFinishHistoryVM;
import hundun.gdxgame.quizgame.core.screen.HistoryScreen.MatchFinishHistory;
import hundun.quizlib.prototype.event.MatchFinishEvent;
import hundun.quizlib.prototype.match.MatchConfig;
import hundun.quizlib.view.match.MatchSituationView;

/**
 * @author hundun
 * Created on 2021/11/12
 */
public class WaitConfirmMatchFinishMaskBoardVM extends AbstractWaitConfirmMaskBoardVM {

    MatchFinishHistory data;

    MatchFinishHistoryVM vm;
    
    public WaitConfirmMatchFinishMaskBoardVM(
            QuizGdxGame game,
            CallerAndCallback callback,
            Drawable background
            ) {
        super(game, callback, background);
        
        
    }
    
    
    public void onCallShow(MatchFinishHistory history) {
        //this.setVisible(true);
        this.data = history;
        
        // --- render data ---
        this.clear();
        
        this.vm = new MatchFinishHistoryVM(game, history.getData());
        this.add(vm).row();
        
        Button textButton = new TextButton("yes", this.game.getMainSkin());
        textButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //WaitConfirmMatchConfigMaskBoardVM.this.setVisible(false);
                WaitConfirmMatchFinishMaskBoardVM.this.callback.onConfirmed();
            }
        });
        this.add(textButton);
    }
    
    
    public static interface CallerAndCallback extends IWaitConfirmCallback {
        void callShowMatchFinishConfirm();
    }
}
