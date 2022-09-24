package hundun.gdxgame.quizgame.core.viewmodel.preparescreen;
/**
 * @author hundun
 * Created on 2022/08/30
 */

import java.util.stream.Collectors;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;

import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.quizlib.prototype.TeamPrototype;
import lombok.Getter;

public class TeamNodeVM extends Table {
    
    public static final int NODE_WIDTH = 500;
    public static final int NODE_HEIGHT = 100;
    
    @Getter
    TeamPrototype data;
    
    Label label;

    Label banInfoLabel;
    Label pickInfoLabel;
    
    public TeamNodeVM(QuizGdxGame game, TeamPrototype teamPrototype) {
        this.label = new Label("TEMP", game.getMainSkin());
        this.banInfoLabel = new Label("TEMP", game.getMainSkin());
        this.pickInfoLabel = new Label("TEMP", game.getMainSkin());
        
        Table banpickInfoGroup = new Table();
        Label banInfoKey = new Label("ban: ", game.getMainSkin());
        Label pickInfoKey = new Label("pick: ", game.getMainSkin());
        banpickInfoGroup.add(banInfoKey).right().padRight(10);
        banpickInfoGroup.add(banInfoLabel);
        banpickInfoGroup.row();
        banpickInfoGroup.add(pickInfoKey).right().padRight(10);
        banpickInfoGroup.add(pickInfoLabel);
        
        this.add(label).padRight(50);
        this.add(banpickInfoGroup);
        
        updateData(teamPrototype);
    }
    
    private void updateData(TeamPrototype data) {
        this.data = data;
        
        label.setText(data.getName());
        banInfoLabel.setText(data.getBanTags().size());
        pickInfoLabel.setText(data.getPickTags().size());
    }

    
}
