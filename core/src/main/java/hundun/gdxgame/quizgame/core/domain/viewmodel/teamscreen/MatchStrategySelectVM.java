package hundun.gdxgame.quizgame.core.domain.viewmodel.teamscreen;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import hundun.gdxgame.quizgame.core.QuizGdxGame;
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
    ICallerAndCallback callerAndCallback;
    @Getter
    MatchStrategyType currenType;
    List<TeamManageSlotVM> teamSlotVMs = new ArrayList<>();
    
    CheckBox preCheckBox;
    CheckBox mainCheckBox;
    ButtonGroup<CheckBox> buttonGroup;
    public MatchStrategySelectVM(QuizGdxGame game, ICallerAndCallback callerAndCallback) {
        this.game = game;
        this.callerAndCallback = callerAndCallback;
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
            this.clear();
            teamSlotVMs.clear();
            for (int i = 0; i < targetSlotNum; i++) {
                TeamManageSlotVM teamSlotVM = new TeamManageSlotVM(game, callerAndCallback);
                teamSlotVM.updateData(null);
                teamSlotVMs.add(teamSlotVM);
                this.add(teamSlotVM).padRight(50);
            }
            
            this.row();
            this.add(preCheckBox).padRight(50);
            this.add(mainCheckBox).padRight(50);
        }
    }
    
    public static interface ICallerAndCallback {
        void onTeamWantChange(TeamManageSlotVM teamSlotVM);
        void onTeamWantModify(TeamManageSlotVM teamSlotVM);
    }

    public List<String> getSelectedTeamNames() {
        return teamSlotVMs.stream().map(it -> it.getData().getName()).collect(Collectors.toList());
    }

}
