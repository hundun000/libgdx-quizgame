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
import hundun.quizlib.prototype.match.MatchConfig;
import hundun.quizlib.view.match.MatchSituationView;

/**
 * @author hundun
 * Created on 2021/11/12
 */
public abstract class AbstractWaitConfirmMaskBoardVM extends Table {
    protected final QuizGdxGame game;
    
    protected final IWaitConfirmCallback callback;
    
    public AbstractWaitConfirmMaskBoardVM(
            QuizGdxGame game,
            IWaitConfirmCallback callback,
            Drawable background
            ) {
        this.game = game;
        this.callback = callback;
        this.setBackground(background);
        //this.setBounds(0, 0, game.LOGIC_WIDTH, game.LOGIC_HEIGHT);

        

        //this.setVisible(false);

    }

    public static void simpleFill(AbstractWaitConfirmMaskBoardVM boardVM) {
        boardVM.clear();
        
        Label label = new Label("TEMP", boardVM.game.getMainSkin());
        boardVM.add(label).center().row();

        Button textButton = new TextButton("yes", boardVM.game.getMainSkin());
        textButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //WaitConfirmMatchConfigMaskBoardVM.this.setVisible(false);
                boardVM.callback.onConfirmed();
            }
        });
        boardVM.add(textButton).center();
    }

    public static interface IWaitConfirmCallback {
        void onConfirmed();
    }
}
