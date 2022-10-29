package hundun.gdxgame.quizgame.core.viewmodel.playscreen;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.gdxgame.quizgame.core.config.TextureAtlasKeys;
import hundun.gdxgame.corelib.base.LogicFrameHelper;
import hundun.gdxgame.corelib.base.util.JavaFeatureForGwt.NumberFormat;
import lombok.Getter;

/**
 * @author hundun
 * Created on 2022/09/01
 */
public class CountdownClockVM extends Table {

    private final CallerAndCallback callerAndCallback;
    private final LogicFrameHelper logicFrameHelper;
    
    private static final String WORD = "秒";
    private final Image image;
    Table textAreaTable;
    private final Label wordPart;
    private final Label countdownPart;
    
    private final NumberFormat format;
    
    int currentCountdownFrame;
    @Getter
    boolean isCountdownState;
    Drawable[] clockDrawables;
    
    public CountdownClockVM(
            QuizGdxGame game,
            CallerAndCallback callerAndCallback,
            LogicFrameHelper logicFrameHelper,
            TextureAtlas atlas
            ) {
        this.callerAndCallback = callerAndCallback;
        this.logicFrameHelper = logicFrameHelper;
        this.format = NumberFormat.getFormat(1, 0);
        //setBackground(background);
        
        this.clockDrawables = new Drawable[TextureAtlasKeys.PLAYSCREEN_CLOCK_SIZE];
        for (int i = 0; i < TextureAtlasKeys.PLAYSCREEN_CLOCK_SIZE; i++) {
            clockDrawables[i] = new TextureRegionDrawable(
                    atlas.findRegion(TextureAtlasKeys.PLAYSCREEN_CLOCK, i)
                    );
        }
        
        this.wordPart = new Label(WORD, game.getMainSkin());
        wordPart.setFontScale(1.5f);
        this.countdownPart = new Label("TEMP", game.getMainSkin());
        countdownPart.setFontScale(1.5f);
        this.image = new Image();
        this.textAreaTable = new Table();
        
        textAreaTable.add(countdownPart).expandX().right();
        textAreaTable.add(wordPart).padRight(50);
        textAreaTable.setBackground(new TextureRegionDrawable(
                atlas.findRegion(TextureAtlasKeys.PLAYSCREEN_CLOCKTEXT)
                ));
        
        this.add(image).width(200).height(200);
        this.row();
        this.add(textAreaTable);
        
        clearCountdown();
    }
    
    public void updateCoutdownSecond(int countdownModify) {
        currentCountdownFrame += countdownModify;
        double second = logicFrameHelper.frameNumToSecond(currentCountdownFrame);
        
        countdownPart.setText(format.format(second));
        int clockImageIndex = ((int)second) % clockDrawables.length;
        image.setDrawable(clockDrawables[clockImageIndex]);
        
        if (second < 0.0000001) {
            clearCountdown();
            callerAndCallback.onCountdownZero();
        }
    }
     
    public void resetCountdown(double second) {
        this.isCountdownState = true;
        this.currentCountdownFrame = logicFrameHelper.secondToFrameNum(second);
        textAreaTable.setVisible(true);
        updateCoutdownSecond(0);
    }
    
    public void clearCountdown() {
        this.isCountdownState = false;
        this.currentCountdownFrame = 0;
        textAreaTable.setVisible(false);
    }

    public static interface CallerAndCallback {
        void onCountdownZero();
    }
}
