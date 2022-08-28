package hundun.gdxgame.quizgame.core.domain.viewmodel;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import hundun.gdxgame.quizgame.core.QuizGdxGame;

/**
 * @author hundun
 * Created on 2022/09/01
 */
public class PlayCountdownClockVM extends Table {

    Label leftPart;
    Label rightPart;
    
    public PlayCountdownClockVM(
            QuizGdxGame game,
            Drawable background
            ) {
        
        setBackground(background);
        
        leftPart = new Label("Time: ", game.getMainSkin());
        this.add(leftPart);
        rightPart = new Label("TEMP", game.getMainSkin());
        this.add(rightPart);
    }
    
    public void updateCoutdown(int second) {
        rightPart.setText(second + "");
    }

}
