package hundun.gdxgame.quizgame.core.viewmodel.preparescreen;

import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.gdxgame.quizgame.core.config.TextureAtlasKeys;

/**
 * @author hundun
 * Created on 2022/09/30
 */
public class ToPlayScreenButtonVM extends Table {

    private final Drawable enableDrawable;
    private final Drawable disableDrawable;
    
    public ToPlayScreenButtonVM(QuizGdxGame game, InputListener listener) {
        
        
        this.enableDrawable = new TextureRegionDrawable(
                game.getTextureConfig().getPlayScreenUITextureAtlas().findRegion(
                        TextureAtlasKeys.PLAYSCREEN_EMPTY_BUTTON
                        )
                );
        this.disableDrawable = new TextureRegionDrawable(
                game.getTextureConfig().getPlayScreenUITextureAtlas().findRegion(
                        TextureAtlasKeys.PLAYSCREEN_SKILLBUTTONUSEOUT
                        )
                );
        Label label = new Label("开始", game.getMainSkin());
        label.setFontScale(2.0f);
        
        this.add(label);
        
        this.addListener(listener);
    }
    
    @Override
        public void setTouchable(Touchable touchable) {
            super.setTouchable(touchable);
            if (touchable == Touchable.enabled) {
                this.setBackground(enableDrawable);
            } else {
                this.setBackground(disableDrawable);
            }
            
        }
}
