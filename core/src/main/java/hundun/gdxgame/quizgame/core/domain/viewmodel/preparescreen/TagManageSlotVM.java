package hundun.gdxgame.quizgame.core.domain.viewmodel.preparescreen;
/**
 * @author hundun
 * Created on 2022/08/30
 */

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.quizlib.prototype.TeamPrototype;
import lombok.Getter;

public class TagManageSlotVM extends Table {
   
    String tag;
    TeamPrototype currenTeamPrototype;
    
    
    Label label;
    CheckBox normalCheckBox;
    CheckBox pickedCheckBox;
    CheckBox bannedCheckBox;
    ButtonGroup<CheckBox> buttonGroup;
    
    public TagManageSlotVM(QuizGdxGame game) {
        this.label = new Label("TEMP", game.getMainSkin());
        this.normalCheckBox = new CheckBox("normal", game.getMainSkin());
        this.pickedCheckBox = new CheckBox("picked", game.getMainSkin());
        this.bannedCheckBox = new CheckBox("banned", game.getMainSkin());
        this.buttonGroup = new ButtonGroup<>(normalCheckBox, pickedCheckBox, bannedCheckBox);
        
        normalCheckBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                currenTeamPrototype.getPickTags().remove(tag);
                currenTeamPrototype.getBanTags().remove(tag);
            }
        });
        pickedCheckBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                currenTeamPrototype.getPickTags().add(tag);
                currenTeamPrototype.getBanTags().remove(tag);
            }
        });
        bannedCheckBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                currenTeamPrototype.getPickTags().remove(tag);
                currenTeamPrototype.getBanTags().add(tag);
            }
        });
        
        
        this.add(label).padRight(40);
        this.add(normalCheckBox).padRight(20);
        this.add(pickedCheckBox).padRight(20);
        this.add(bannedCheckBox);
    }
    
    public void updateData(String tag, TeamPrototype currenTeamPrototype) {
        this.tag = tag;
        this.currenTeamPrototype = currenTeamPrototype;

        label.setText(tag);
        if (currenTeamPrototype.getPickTags().contains(tag)) {
            pickedCheckBox.setChecked(true);
        } else if (currenTeamPrototype.getBanTags().contains(tag)) {
            bannedCheckBox.setChecked(true);
        } else {
            normalCheckBox.setChecked(true);
        }
    }

}
