package hundun.gdxgame.quizgame.core.viewmodel.playscreen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.gdxgame.quizgame.core.config.TextureAtlasKeys;
import hundun.quizlib.view.question.QuestionView;

/**
 * @author hundun
 * Created on 2022/09/01
 */
public class QuestionOptionAreaVM extends Table {
    final QuizGdxGame game;
    final CallerAndCallback callerAndCallback;
    
    List<OptionNode> nodes = new ArrayList<>();

    static final int SIZE = 4;
    
    final int NODE_WIDTH;
    final int NODE_HEIGHT;
    
    
    public QuestionOptionAreaVM(
            QuizGdxGame game,
            CallerAndCallback callerAndCallback,
            TextureAtlas textureAtlas,
            TextureAtlas maskTextureAtlas
            ) {
        this.game = game;
        this.callerAndCallback = callerAndCallback;
        //setBackground(background);
        this.NODE_WIDTH = 700;
        this.NODE_HEIGHT = 100;
        
        OptionNode optionButton;
        for (int i = 0; i < SIZE; i++) {
            optionButton = new OptionNode(game, i, textureAtlas, maskTextureAtlas);
            nodes.add(optionButton);
            this.add(optionButton)
                    .height(NODE_HEIGHT)
                    .width(NODE_WIDTH)
                    .padBottom(25)
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
        final Drawable correctMask;
        final Drawable wrongMask;
//        @Setter
//        Drawable mask;
        Label textButton;
        Drawable selectedAtlasRegion;
        Drawable unSelectedAtlasRegion;
        Image maskActor;
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
        
        public OptionNode(QuizGdxGame game, int index, TextureAtlas textureAtlas, TextureAtlas maskTextureAtlas) {
            this.index = index;
            this.selectedAtlasRegion = new TextureRegionDrawable(
                    textureAtlas.findRegion(TextureAtlasKeys.PLAYSCREEN_OPTIONBUTTON, 0)
                    );
            this.unSelectedAtlasRegion = new TextureRegionDrawable(
                    textureAtlas.findRegion(TextureAtlasKeys.PLAYSCREEN_OPTIONBUTTON, 1)
                    );
            this.correctMask = new TextureRegionDrawable(
                    maskTextureAtlas.findRegion(TextureAtlasKeys.MASK_CORRECTOPTION)
                    );
            this.wrongMask = new TextureRegionDrawable(
                    maskTextureAtlas.findRegion(TextureAtlasKeys.MASK_WRONGOPTION)
                    );   
            this.textButton = new Label("TEMP", game.getMainSkin(), "whiteType");
            textButton.setFontScale(1.5f);
            this.maskActor = new Image();
            maskActor.setBounds(0, 0, NODE_WIDTH, NODE_HEIGHT);
            
            this.add(textButton)
                    .expand()
                    //.fill()
                    ;
            this.addActor(maskActor);
            
            this.addListener(
                    new InputListener(){
                        @Override
                        public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                            OptionNode.this.setBackground(selectedAtlasRegion);
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
            
//            this.maskActor.setDrawable(null);
//            this.setMask(null);
            this.setBackground(unSelectedAtlasRegion);
            if (isCorrect) {
                this.showState = OptionButtonShowState.HIDE_CORRECT;
            } else {
                this.showState = OptionButtonShowState.HIDE_WRONG;
            }
            this.maskActor.setDrawable(null);
        }
        
        public void updateShowStateToShow() {
            Drawable mask;
            if (this.showState == OptionButtonShowState.HIDE_CORRECT) {
                this.showState = OptionButtonShowState.SHOW_CORRECT;
                mask = correctMask;
            } else {
                this.showState = OptionButtonShowState.SHOW_WRONG;
                mask = wrongMask;
            }
            //this.setMask(new TextureRegionDrawable(texture));
            
            this.maskActor.setDrawable(mask);
            
            //this.setBackground(new TextureRegionDrawable(texture));
        }

    }
    
    public static interface CallerAndCallback {
        void onChooseOption(int index);
    }

    public void showRandomOption(int showOptionAmout) {
        List<Integer> showIndexs = IntStream.range(0, SIZE)
            .boxed()
            .filter(index -> nodes.get(index).showState == OptionButtonShowState.HIDE_WRONG)
            .collect(Collectors.toList())
            ;
        if (showIndexs.size() < showOptionAmout) {
            return;
        }
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
