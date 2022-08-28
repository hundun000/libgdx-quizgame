package hundun.gdxgame.quizgame.core.domain.viewmodel;

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
public class WaitConfirmMatchSituationMaskBoardVM extends Table {

    MatchSituationView data;
    CallerAndCallback callback;
    
    Label label;
    
    
    public WaitConfirmMatchSituationMaskBoardVM(
            QuizGdxGame game,
            CallerAndCallback callback,
            Drawable background
            ) {
        
        
        this.setBackground(background);
        this.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        label = new Label("TEMP", game.getMainSkin());
        this.add(label).center().row();

        Button textButton = new TextButton("yes", game.getMainSkin());
        textButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //WaitConfirmMatchConfigMaskBoardVM.this.setVisible(false);
                callback.onMatchSituationConfirmed();
            }
        });
        this.add(textButton).center();

        //this.setVisible(false);

    }
    
    
    public void onCallShow(MatchSituationView data) {
        //this.setVisible(true);
        this.data = data;
        
        // --- render data ---
        
        
    }
    
    
    public static interface CallerAndCallback {
        void onMatchSituationConfirmed();
        void onMatchSituationConfirmCallShow();
    }
}
