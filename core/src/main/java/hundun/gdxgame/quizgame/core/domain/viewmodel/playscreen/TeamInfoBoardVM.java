package hundun.gdxgame.quizgame.core.domain.viewmodel.playscreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.gdxgame.share.base.util.JavaFeatureForGwt.NumberFormat;
import hundun.quizlib.view.question.QuestionView;

/**
 * @author hundun
 * Created on 2022/09/01
 */
public class TeamInfoBoardVM extends Table {
    
    CallerAndCallback callerAndCallback;
    
    List<TextButton> buttons = new ArrayList<>();

    
    public TeamInfoBoardVM(
            QuizGdxGame game,
            CallerAndCallback callerAndCallback,
            Drawable background
            ) {
        this.callerAndCallback = callerAndCallback;
        setBackground(background);
        
        TextButton optionButton;
        for (int i = 0; i < SystemButtonType.types.length; i++) {
            optionButton = new SystemButton(game, SystemButtonType.types[i]);
            buttons.add(optionButton);
            this.row();
            this.add(optionButton);
        }
        
    }
    
    public void updateQuestion(QuestionView questionView) {
        for (int i = 0; i < questionView.getOptions().size(); i++) {
            String optiontext = questionView.getOptions().get(i);
            buttons.get(i).setText(optiontext);
        }
    }
    
    public enum SystemButtonType {
        SHOW_MATCH_SITUATION,
        ;
        
        static SystemButtonType[] types = new SystemButtonType[] {
                SHOW_MATCH_SITUATION
        };
    }
    
    public class SystemButton extends TextButton {


        final SystemButtonType type;
        
        public SystemButton(QuizGdxGame game, SystemButtonType type) {
            super("TEMP", game.getMainSkin());
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
