package hundun.gdxgame.quizgame.core.domain.viewmodel.teamscreen;

import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.quizlib.prototype.match.MatchStrategyType;

/**
 * @author hundun
 * Created on 2022/09/14
 */
public class MatchStrategySelectVM extends Table {
    CheckBox preCheckBox;
    CheckBox mainCheckBox;
    ButtonGroup<CheckBox> buttonGroup;
    public MatchStrategySelectVM(QuizGdxGame game) {
        this.preCheckBox = new CheckBox("PRE", game.getMainSkin());
        this.mainCheckBox = new CheckBox("MAIN", game.getMainSkin());
        this.buttonGroup = new ButtonGroup<>(preCheckBox, mainCheckBox);
        
        this.add(preCheckBox).padRight(50);
        this.add(mainCheckBox).padRight(50);
    }
    
    public MatchStrategyType getSelected() {
        if (preCheckBox.isChecked()) {
            return MatchStrategyType.PRE;
        }
        if (mainCheckBox.isChecked()) {
            return MatchStrategyType.MAIN;
        }
        return null;
    }
}
