package hundun.gdxgame.quizgame.core.viewmodel.playscreen;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.gdxgame.quizgame.core.config.TextureAtlasKeys;

/**
 * @author hundun
 * Created on 2022/09/01
 */
public class SystemBoardVM extends Table {
    
    CallerAndCallback callerAndCallback;
    
    List<SystemButton> buttons = new ArrayList<>();

    
    public SystemBoardVM(
            QuizGdxGame game,
            CallerAndCallback callerAndCallback,
            TextureAtlas textureAtlas
            ) {
        this.callerAndCallback = callerAndCallback;
        
        
        SystemButton optionButton;
        for (int i = 0; i < SystemButtonType.types.length; i++) {
            SystemButtonType type = SystemButtonType.types[i];
            
            AtlasRegion buttonAtlasRegion;
            switch (type) {
                case PAUSE:
                    buttonAtlasRegion = textureAtlas.findRegion(TextureAtlasKeys.PLAYSCREEN_PAUSEBUTTON);
                    break;
                case EXIT:
                    buttonAtlasRegion = textureAtlas.findRegion(TextureAtlasKeys.PLAYSCREEN_EXITBUTTON);
                    break;
                default:
                    buttonAtlasRegion = null;
                    break;
            }
            optionButton = new SystemButton(game, type, buttonAtlasRegion);
            buttons.add(optionButton);
            this.row();
            this.add(optionButton)
                    .height(SystemButton.SIZE)
                    .width(SystemButton.SIZE)
                    .padBottom(SystemButton.SIZE * 0.2f)
                    .fill();
        }
        
    }
    
    public enum SystemButtonType {
        PAUSE,
        EXIT,
        ;
        
        static SystemButtonType[] types = new SystemButtonType[] {
                PAUSE,
                EXIT
        };
    }
    
    public class SystemButton extends Image {
        static final int SIZE = 50;

        final SystemButtonType type;
        
        public SystemButton(QuizGdxGame game, SystemButtonType type, AtlasRegion buttonAtlasRegion) {
            super(buttonAtlasRegion);
            this.type = type;
            this.addListener(
                    new InputListener(){
                        @Override
                        public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                            callerAndCallback.onChooseSystem(type);
                        }
                        @Override
                        public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                            return true;
                        }
                    }
                    );
        }
        
        
        
    }
    
    public static interface CallerAndCallback {
        void onChooseSystem(SystemButtonType type);
    }

}
