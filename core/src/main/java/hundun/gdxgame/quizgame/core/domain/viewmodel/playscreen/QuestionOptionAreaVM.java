package hundun.gdxgame.quizgame.core.domain.viewmodel.playscreen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.gdxgame.share.base.util.DrawableFactory;
import hundun.gdxgame.share.base.util.JavaFeatureForGwt.NumberFormat;
import hundun.quizlib.view.question.QuestionView;
import lombok.Setter;

/**
 * @author hundun
 * Created on 2022/09/01
 */
public class QuestionOptionAreaVM extends Table {
    final QuizGdxGame game;
    final CallerAndCallback callerAndCallback;
    
    List<OptionNode> nodes = new ArrayList<>();

    static final int SIZE = 4;
    public QuestionOptionAreaVM(
            QuizGdxGame game,
            CallerAndCallback callerAndCallback,
            Drawable background
            ) {
        this.game = game;
        this.callerAndCallback = callerAndCallback;
        //setBackground(background);
        
        OptionNode optionButton;
        for (int i = 0; i < SIZE; i++) {
            optionButton = new OptionNode(game, i, background);
            nodes.add(optionButton);
            this.add(optionButton)
                    .height((float) (game.getHeight() * 0.08))
                    .width((float) (game.getWidth()* 0.3))
                    .pad((float) (game.getHeight() * 0.01))
                    ;
            this.row();
        }
        
    }
    
    public void updateQuestion(QuestionView questionView) {
        for (int i = 0; i < SIZE; i++) {
            String optiontext = questionView.getOptions().get(i);
            nodes.get(i).updateForNewQuestion(optiontext, i == questionView.getAnswer());
        }
    }
    
    enum OptionButtonShowState {
        SHOW_CORRECT,
        SHOW_WRONG,
        HIDE_CORRECT,
        HIDE_WRONG,
        ;
    }
    
    public class OptionNode extends Table {
        OptionButtonShowState showState;
        final int index;
//        @Setter
//        Drawable mask;
        Label textButton;
//        Image maskActor;
//        protected void drawMask (Batch batch, float parentAlpha, float x, float y) {
//            if (mask == null) return;
//            Color color = getColor();
//            batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
//            mask.draw(batch, x, y, getWidth(), getHeight());
//        }
        
//        @Override
//        public void draw(Batch batch, float parentAlpha) {
//            super.draw(batch, parentAlpha);
//            applyTransform(batch, computeTransform());
//            drawMask(batch, parentAlpha, 0, 0);
//            resetTransform(batch);
//        }
        
        public OptionNode(QuizGdxGame game, int index, Drawable background) {
            this.index = index;
            
            this.textButton = new Label("TEMP", game.getMainSkin());
            this.add(textButton)
                    .expand()
                    //.fill()
                    ;
            
//            this.maskActor = new Image();
//            maskActor.setPosition(0, 0);
//            this.addActor(maskActor);
            
            this.addListener(
                    new InputListener(){
                        @Override
                        public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                            callerAndCallback.onChooseOption(index);
                        }
                        @Override
                        public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                            return true;
                        }
                    });
//            this.setBackground(background);
            this.setTouchable(Touchable.enabled);
        }
        
        public void updateForNewQuestion(String optiontext, boolean isCorrect) {
            this.textButton.setText(optiontext);
            Texture texture = game.getTextureConfig().getOptionButtonBackground();
//            this.maskActor.setDrawable(null);
//            this.setMask(null);
            this.setBackground(new TextureRegionDrawable(texture));
            if (isCorrect) {
                this.showState = OptionButtonShowState.HIDE_CORRECT;
            } else {
                this.showState = OptionButtonShowState.HIDE_WRONG;
            }
        }
        
        public void updateShowStateToShow() {
            Texture texture;
            if (this.showState == OptionButtonShowState.HIDE_CORRECT) {
                this.showState = OptionButtonShowState.SHOW_CORRECT;
                texture = game.getTextureConfig().getOptionButtonCorrectMask();
            } else {
                this.showState = OptionButtonShowState.SHOW_WRONG;
                texture = game.getTextureConfig().getOptionButtonWrongMask();
            }
            //this.setMask(new TextureRegionDrawable(texture));
            //this.maskActor.setDrawable(new TextureRegionDrawable(texture));
            this.setBackground(new TextureRegionDrawable(texture));
        }

    }
    
    public static interface CallerAndCallback {
        void onChooseOption(int index);
    }

    public void showRandomOption(int showOptionAmout) {
        List<Integer> showIndexs = IntStream.range(0, SIZE)
            .boxed()
            .collect(Collectors.toList())
            ;
        Collections.shuffle(showIndexs);
        showIndexs = showIndexs.subList(0, showOptionAmout);
        
        showIndexs.forEach(showIndex -> {
            OptionNode node = nodes.get(showIndex);
            node.updateShowStateToShow();
        });
    }
    
    public void showAllOption() {
        nodes.forEach(node -> {
            node.updateShowStateToShow();
        });
    }
}
