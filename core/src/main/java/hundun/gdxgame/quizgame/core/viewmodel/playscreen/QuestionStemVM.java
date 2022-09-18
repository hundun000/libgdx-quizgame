package hundun.gdxgame.quizgame.core.viewmodel.playscreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.gdxgame.quizgame.core.config.TextureAtlasKeys;
import hundun.gdxgame.share.base.util.JavaFeatureForGwt.NumberFormat;
import hundun.quizlib.view.question.QuestionView;

/**
 * @author hundun
 * Created on 2022/09/01
 */
public class QuestionStemVM extends Table {
    
   
    
    Label stemPart;

    
    public QuestionStemVM(
            QuizGdxGame game,

            TextureAtlas textureAtlas
            ) {
        
        setBackground(new TextureRegionDrawable(textureAtlas.findRegion(TextureAtlasKeys.PLAYSCREEN_QUESTIONSTEMBACKGROUND)));
        
        stemPart = new Label("TEMP", game.getMainSkin());
        this.add(stemPart);
        
    }
    
    public void updateQuestion(QuestionView questionView) {
        stemPart.setText(questionView.getStem());
    }

}
