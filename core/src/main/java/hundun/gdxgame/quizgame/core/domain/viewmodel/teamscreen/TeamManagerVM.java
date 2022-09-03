package hundun.gdxgame.quizgame.core.domain.viewmodel.teamscreen;
/**
 * @author hundun
 * Created on 2022/08/30
 */

import com.badlogic.gdx.scenes.scene2d.ui.Label;

import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.quizlib.prototype.TeamPrototype;

public class TeamManagerVM extends Label {
    
    TeamPrototype data;
    
    public TeamManagerVM(QuizGdxGame game) {
        super("TEMP", game.getMainSkin());
    }
    
    public void updateData(TeamPrototype data) {
        this.setText(data.getName());
    }

}
