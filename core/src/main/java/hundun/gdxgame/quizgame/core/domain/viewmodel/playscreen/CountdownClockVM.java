package hundun.gdxgame.quizgame.core.domain.viewmodel.playscreen;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.gdxgame.share.base.LogicFrameHelper;
import hundun.gdxgame.share.base.util.JavaFeatureForGwt.NumberFormat;
import lombok.Getter;
import lombok.Setter;

/**
 * @author hundun
 * Created on 2022/09/01
 */
public class CountdownClockVM extends Table {

    private final CallerAndCallback callerAndCallback;
    private final LogicFrameHelper logicFrameHelper;
    
    private static final String WORD = "Time: ";
    Label wordPart;
    Label countdownPart;
    
    NumberFormat format;
    
    int currentCountdownFrame;
    @Getter
    boolean isCountdownState;
    
    public CountdownClockVM(
            QuizGdxGame game,
            CallerAndCallback callerAndCallback,
            LogicFrameHelper logicFrameHelper,
            Drawable background
            ) {
        this.callerAndCallback = callerAndCallback;
        this.logicFrameHelper = logicFrameHelper;
        this.format = NumberFormat.getFormat(1, 1);
        setBackground(background);
        
        wordPart = new Label("TEMP", game.getMainSkin());
        this.add(wordPart);
        countdownPart = new Label("TEMP", game.getMainSkin());
        this.add(countdownPart);
        
        clearCountdown();
    }
    
    public void updateCoutdownSecond(int countdownModify) {
        currentCountdownFrame += countdownModify;
        double second = logicFrameHelper.frameNumToSecond(currentCountdownFrame);

        countdownPart.setText(format.format(second));
        if (second < 0.0000001) {
            clearCountdown();
            callerAndCallback.onCountdownZero();
        }
    }
    
    public void resetCountdown(double second) {
        this.isCountdownState = true;
        this.currentCountdownFrame = logicFrameHelper.secondToFrameNum(second);
        wordPart.setText(WORD);
        updateCoutdownSecond(0);
    }
    
    public void clearCountdown() {
        this.isCountdownState = false;
        this.currentCountdownFrame = 0;
        wordPart.setText("CLEARED: ");
        countdownPart.setText("CLEARED");
    }

    public static interface CallerAndCallback {
        void onCountdownZero();
    }
}
