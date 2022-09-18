package hundun.gdxgame.quizgame.core.domain.viewmodel.preparescreen;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.gdxgame.quizgame.core.screen.PrepareScreen;
import hundun.gdxgame.share.base.util.DrawableFactory;
import hundun.quizlib.prototype.TeamPrototype;
import hundun.quizlib.prototype.match.MatchStrategyType;
import lombok.Getter;

/**
 * @author hundun
 * Created on 2022/09/14
 */
public class MatchStrategySelectVM extends Table {
    QuizGdxGame game;
    @Getter
    MatchStrategyType currenType;
    
    CheckBox preCheckBox;
    CheckBox mainCheckBox;
    ButtonGroup<CheckBox> buttonGroup;
    
    ISlotNumListener slotNumListener;
    
    public MatchStrategySelectVM(QuizGdxGame game, ISlotNumListener slotNumListener) {
        this.game = game;
        this.slotNumListener = slotNumListener;
        this.setBackground(DrawableFactory.getSimpleBoardBackground());
        
        this.preCheckBox = new CheckBox("PRE", game.getMainSkin());
        this.mainCheckBox = new CheckBox("MAIN", game.getMainSkin());
        this.buttonGroup = new ButtonGroup<>(preCheckBox, mainCheckBox);
        
        ClickListener changeListener = new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                checkSlotNum();
            };
        };
        preCheckBox.addListener(changeListener);
        mainCheckBox.addListener(changeListener);
        
        this.add(preCheckBox).padRight(50);
        this.add(mainCheckBox).padRight(50);
        
        if (game.debugMode) {
            this.debugCell();
        }
        
        checkSlotNum();
    }
    
    void checkSlotNum() {
        int targetSlotNum;
        MatchStrategyType newType;
        if (preCheckBox.isChecked()) {
            newType = MatchStrategyType.PRE;
            targetSlotNum = 1;
        } else if (mainCheckBox.isChecked()) {
            newType = MatchStrategyType.MAIN;
            targetSlotNum = 2;
        } else {
            preCheckBox.setChecked(true);
            newType = MatchStrategyType.PRE;
            targetSlotNum = 1;
        }
        
        if (currenType != newType) {
            currenType = newType;
            slotNumListener.updateSlotNum(targetSlotNum);
        }
        
    }

    
    public interface ISlotNumListener {
        void updateSlotNum(int targetSlotNum);
    }
}
