package hundun.gdxgame.quizgame.core.domain.viewmodel.teamscreen;
/**
 * @author hundun
 * Created on 2022/08/30
 */

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.gdxgame.quizgame.core.domain.viewmodel.teamscreen.MatchStrategySelectVM.ICallerAndCallback;
import hundun.quizlib.prototype.TeamPrototype;
import lombok.Getter;

public class TeamManageSlotVM extends Table {
    @Getter
    TeamPrototype data;
    
    Label label;
    Button changeTeamButton;
    Button modifyTeamButton;
    
    public TeamManageSlotVM(QuizGdxGame game, ICallerAndCallback callerAndCallback) {
        this.label = new Label("TEMP", game.getMainSkin());
        this.changeTeamButton = new TextButton("change", game.getMainSkin());
        this.modifyTeamButton = new TextButton("modify", game.getMainSkin());
        
        changeTeamButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                callerAndCallback.onTeamWantChange(TeamManageSlotVM.this);
            }
        });
        modifyTeamButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                callerAndCallback.onTeamWantModify(TeamManageSlotVM.this);
            }
        });
        
        this.add(label).colspan(2);
        this.row();
        this.add(changeTeamButton);
        this.add(modifyTeamButton);
    }
    
    public void updateData(TeamPrototype data) {
        this.data = data;
        if (data != null) {
            label.setText(data.getName());
            modifyTeamButton.setTouchable(Touchable.enabled);
        } else {
            label.setText("待选择");
            modifyTeamButton.setTouchable(Touchable.disabled);
        }
    }

}
