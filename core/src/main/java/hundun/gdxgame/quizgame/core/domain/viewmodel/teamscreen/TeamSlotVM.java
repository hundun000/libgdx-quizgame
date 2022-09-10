package hundun.gdxgame.quizgame.core.domain.viewmodel.teamscreen;
/**
 * @author hundun
 * Created on 2022/08/30
 */

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.quizlib.prototype.TeamPrototype;
import lombok.Getter;

public class TeamSlotVM extends Table {
    @Getter
    TeamPrototype data;
    
    Label label;
    Button button;
    
    public TeamSlotVM(QuizGdxGame game) {
        this.label = new Label("TEMP", game.getMainSkin());
        this.button = new TextButton("change", game.getMainSkin());
        
        this.add(label);
        this.row();
        this.add(button);
    }
    
    public void updateData(TeamPrototype data) {
        this.data = data;
        if (data != null) {
            label.setText(data.getName());
        } else {
            label.setText("待选择");
        }
        
    }

}
