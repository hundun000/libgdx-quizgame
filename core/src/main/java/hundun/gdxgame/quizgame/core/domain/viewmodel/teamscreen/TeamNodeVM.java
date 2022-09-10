package hundun.gdxgame.quizgame.core.domain.viewmodel.teamscreen;
/**
 * @author hundun
 * Created on 2022/08/30
 */

import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.quizlib.prototype.TeamPrototype;
import lombok.Getter;

public class TeamNodeVM extends Table {
    @Getter
    TeamPrototype data;
    
    Label label;
    CheckBox checkBox;
    
    public TeamNodeVM(QuizGdxGame game) {
        this.label = new Label("TEMP", game.getMainSkin());
        this.checkBox = new CheckBox("", game.getMainSkin());
        
        this.add(label);
        this.row();
        this.add(checkBox);
    }
    
    public void updateData(TeamPrototype data) {
        this.data = data;
        
        label.setText(data.getName());
        checkBox.setChecked(false);
    }
    
    public boolean isSelected() {
        return checkBox.isChecked();
    }

}
