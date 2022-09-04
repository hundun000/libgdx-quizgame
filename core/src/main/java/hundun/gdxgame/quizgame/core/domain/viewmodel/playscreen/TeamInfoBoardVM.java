package hundun.gdxgame.quizgame.core.domain.viewmodel.playscreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.gdxgame.share.base.util.JavaFeatureForGwt.NumberFormat;
import hundun.quizlib.prototype.TeamPrototype;
import hundun.quizlib.view.question.QuestionView;

/**
 * @author hundun
 * Created on 2022/09/01
 */
public class TeamInfoBoardVM extends Table {
    
    
    QuizGdxGame game;
    Map<TeamPrototype, TeamInfoNode> map = new HashMap<>();
    int teamPrototypesHashCode = -1;
    
    public TeamInfoBoardVM(
            QuizGdxGame game,
            Drawable background
            ) {
        setBackground(background);
        this.game = game;
    }
    
    private class TeamInfoNode extends Table {
        final int SIGN_SIZE = 50;
        final Image sign;
        final TextureRegionDrawable drawable;
        final int NAME_WIDTH = 200;
        final Label teamInfoLabel;
        
        TeamInfoNode(TeamPrototype teamPrototype) {
            drawable = new TextureRegionDrawable(game.getTextureConfig().getCurrentTeamSignTexture());
            
            sign = new Image();
            this.add(sign).width(SIGN_SIZE).height(SIGN_SIZE).padRight(SIGN_SIZE * 0.5f);
            
            teamInfoLabel = new Label(teamPrototype.getName(), game.getMainSkin());
            this.add(teamInfoLabel).width(NAME_WIDTH);
        }
        
        void updateCurrent(boolean isCurrent) {
            if (isCurrent) {
                sign.setDrawable(drawable);
            } else {
                sign.setDrawable(null);
            }
        }
    }

    public void updateTeam(TeamPrototype currentTeamPrototype, List<TeamPrototype> teamPrototypes) {
        
        if (teamPrototypesHashCode != teamPrototypes.hashCode()) {
            teamPrototypesHashCode = teamPrototypes.hashCode();
            this.clear();
            map.clear();
            teamPrototypes.forEach(it -> {
                TeamInfoNode vm = new TeamInfoNode(it);
                map.put(it, vm);
                TeamInfoBoardVM.this.add(vm).row();
            });
        }
        
        map.forEach((k, v) -> {
            v.updateCurrent(k == currentTeamPrototype);
        });
        
    }
}
