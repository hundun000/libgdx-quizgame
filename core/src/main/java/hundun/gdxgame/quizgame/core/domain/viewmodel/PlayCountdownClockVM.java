package hundun.gdxgame.quizgame.core.domain.viewmodel;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.gdxgame.share.base.util.JavaFeatureForGwt.NumberFormat;

/**
 * @author hundun
 * Created on 2022/09/01
 */
public class PlayCountdownClockVM extends Table {

    Label leftPart;
    Label rightPart;
    
    NumberFormat format;
    
    public PlayCountdownClockVM(
            QuizGdxGame game,
            Drawable background
            ) {
        this.format = NumberFormat.getFormat(1, 1);
        setBackground(background);
        
        leftPart = new Label("Time: ", game.getMainSkin());
        this.add(leftPart);
        rightPart = new Label("TEMP", game.getMainSkin());
        this.add(rightPart);
    }
    
    public void updateCoutdownSecond(double second) {
        rightPart.setText(format.format(second));
    }

}
