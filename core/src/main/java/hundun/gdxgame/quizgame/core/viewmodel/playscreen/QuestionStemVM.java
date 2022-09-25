package hundun.gdxgame.quizgame.core.viewmodel.playscreen;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.gdxgame.quizgame.core.config.TextureAtlasKeys;
import hundun.quizlib.view.question.QuestionView;

/**
 * @author hundun
 * Created on 2022/09/01
 */
public class QuestionStemVM extends Table {
    
    private static final int WORD_PER_LINE = 20;
    
    Label stemPart;

    
    public QuestionStemVM(
            QuizGdxGame game,

            TextureAtlas textureAtlas
            ) {
        
        setBackground(new TextureRegionDrawable(textureAtlas.findRegion(TextureAtlasKeys.PLAYSCREEN_QUESTIONSTEMBACKGROUND)));
        
        this.stemPart = new Label("TEMP", game.getMainSkin());
        stemPart.setFontScale(1.5f);
        
        this.add(stemPart);
        
    }
    
    public void updateQuestion(QuestionView questionView) {
        String originText = questionView.getStem();
        List<String> lines = new ArrayList<>();
        for (int i = 0; i < originText.length(); i += WORD_PER_LINE) {
            String line = originText.substring(i, Math.min(i + WORD_PER_LINE, originText.length()));
            lines.add(line);
        }
        if (lines.size() > 1) {
            lines.set(0, "  " + lines.get(0));
        }
        stemPart.setText(lines.stream().collect(Collectors.joining("\n")));
    }

}
