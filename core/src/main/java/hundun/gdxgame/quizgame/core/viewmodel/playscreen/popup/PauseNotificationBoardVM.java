package hundun.gdxgame.quizgame.core.viewmodel.playscreen.popup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.gdxgame.quizgame.core.config.TextureAtlasKeys;
import hundun.quizlib.prototype.match.MatchConfig;
import hundun.quizlib.view.match.MatchSituationView;

/**
 * @author hundun
 * Created on 2021/11/12
 */
public class PauseNotificationBoardVM extends AbstractNotificationBoardVM<Void> {

    MatchSituationView data;
    Image image;

    public PauseNotificationBoardVM(
            QuizGdxGame game,
            CallerAndCallback callback,
            Drawable background, 
            TextureAtlas textureAtlas
            ) {
        super(game, callback, background);

        this.image = new Image(new TextureRegionDrawable(
                textureAtlas.findRegion(TextureAtlasKeys.PLAYSCREEN_POPUP_PLAY)
                ));
        
        this.add(image).width(300).height(300);
    }
    
    @Override
    public void onCallShow(Void data) {

        image.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                callback.onNotificationConfirmed();
            }
        });
    }
    
    
    public static interface CallerAndCallback extends IWaitConfirmNotificationCallback {
        void callShowPauseConfirm();
    }
}
