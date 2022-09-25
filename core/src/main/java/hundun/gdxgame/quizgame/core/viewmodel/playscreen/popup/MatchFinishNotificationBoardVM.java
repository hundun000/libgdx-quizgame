package hundun.gdxgame.quizgame.core.viewmodel.playscreen.popup;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.gdxgame.quizgame.core.screen.HistoryScreen.MatchHistoryDTO;
import hundun.gdxgame.quizgame.core.viewmodel.share.MatchHistoryVM;

/**
 * @author hundun
 * Created on 2021/11/12
 */
public class MatchFinishNotificationBoardVM extends AbstractNotificationBoardVM<MatchHistoryDTO> {

    MatchHistoryDTO data;

    MatchHistoryVM vm;
    
    public MatchFinishNotificationBoardVM(
            QuizGdxGame game,
            CallerAndCallback callback,
            Drawable background
            ) {
        super(game, callback, background);
        
        
    }
    
    @Override
    public void onCallShow(MatchHistoryDTO history) {
        //this.setVisible(true);
        this.data = history;
        this.vm = MatchHistoryVM.Factory.fromBO(game, history);
        Label label = new Label("比赛记录", game.getMainSkin());
        label.setFontScale(1.5f);
        Button textButton = new TextButton("离开并保存", this.game.getMainSkin());
        textButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //WaitConfirmMatchConfigMaskBoardVM.this.setVisible(false);
                MatchFinishNotificationBoardVM.this.callback.onNotificationConfirmed();
            }
        });
        
        // --- render data ---
        this.clear();
        
        
        this.add(label).padBottom(50).row();
        this.add(vm).padBottom(50).row();
        this.add(textButton).width(200).height(50).fill();
        
        if (game.debugMode) {
            this.debugCell();
        }
    }
    
    
    public static interface CallerAndCallback extends IWaitConfirmNotificationCallback {
        void callShowMatchFinishConfirm();
    }
}
