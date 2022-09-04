package hundun.gdxgame.quizgame.core.domain.viewmodel.playscreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.gdxgame.share.base.util.JavaFeatureForGwt.NumberFormat;
import hundun.quizlib.prototype.TeamPrototype;
import hundun.quizlib.view.question.QuestionView;

/**
 * @author hundun
 * Created on 2022/09/01
 */
public class TeamInfoBoardVM extends Table {
    
    Label teamInfoLabel;

    
    public TeamInfoBoardVM(
            QuizGdxGame game,
            Drawable background
            ) {
        setBackground(background);
        
        
        teamInfoLabel = new Label("TEMP", game.getMainSkin());
        this.add(teamInfoLabel);
        
        
    }

    public void updateTeam(TeamPrototype teamPrototype) {
        teamInfoLabel.setText(teamPrototype.getName());
    }
}
