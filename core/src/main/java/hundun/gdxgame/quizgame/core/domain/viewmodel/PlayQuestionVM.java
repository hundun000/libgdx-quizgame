package hundun.gdxgame.quizgame.core.domain.viewmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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
public class PlayQuestionVM extends Table {
    
    Label leftPart;
    List<TextButton> optionButtons = new ArrayList<>();

    
    public PlayQuestionVM(
            QuizGdxGame game,
            Drawable background
            ) {

        setBackground(background);
        
        leftPart = new Label("TEMP", game.getMainSkin());
        this.add(leftPart);
        
        TextButton optionButton;
        for (int i = 0; i < 4; i++) {
            optionButton = new TextButton("TEMP", game.getMainSkin());
            optionButtons.add(optionButton);
            this.row();
            this.add(optionButton);
        }
        
    }
    
    public void updateQuestion(QuestionView questionView) {
        leftPart.setText(questionView.getStem());
        for (int i = 0; i < questionView.getOptions().size(); i++) {
            String optiontext = questionView.getOptions().get(i);
            optionButtons.get(i).setText(optiontext);
        }
    }
}
