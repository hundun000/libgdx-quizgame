package hundun.gdxgame.quizgame.core.viewmodel.playscreen;

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
import hundun.gdxgame.quizgame.core.config.TextureAtlasKeys;
import hundun.gdxgame.share.base.util.JavaFeatureForGwt;
import hundun.gdxgame.share.base.util.JavaFeatureForGwt.NumberFormat;
import hundun.quizlib.prototype.TeamPrototype;
import hundun.quizlib.prototype.match.MatchStrategyType;
import hundun.quizlib.view.question.QuestionView;
import hundun.quizlib.view.team.TeamRuntimeView;

/**
 * @author hundun
 * Created on 2022/09/01
 */
public class TeamInfoBoardVM extends Table {
    
    
    QuizGdxGame game;
    List<TeamInfoNode> nodes = new ArrayList<>();

    
    public TeamInfoBoardVM(
            QuizGdxGame game,
            Drawable background
            ) {
        setBackground(background);
        this.game = game;
    }
    
    private class TeamInfoNode extends Table {
        final static int SIGN_SIZE = 50;
        final Image signSlotImage;
        final Drawable signDrawable;
        final static int NAME_WIDTH = 200;
        final Label teamInfoLabel;
        final Label teamInfoLabel2;
        
        TeamInfoNode() {
            this.signDrawable = new TextureRegionDrawable(game.getTextureConfig().getPlayScreenUITextureAtlas().findRegion(TextureAtlasKeys.PLAYSCREEN_CURRENTTEAMSIGN));
            
            this.signSlotImage = new Image();
            this.add(signSlotImage).width(SIGN_SIZE).height(SIGN_SIZE).padRight(SIGN_SIZE * 0.5f);
            
            this.teamInfoLabel = new Label("TEMP", game.getMainSkin());
            this.add(teamInfoLabel).width(NAME_WIDTH);
            
            this.row();
            
            this.teamInfoLabel2 = new Label("TEMP", game.getMainSkin());
            this.add(teamInfoLabel2).colspan(2);
        }
        
        void updatePrototype(TeamPrototype teamPrototype) {
            teamInfoLabel.setText(teamPrototype.getName());
            
        }
        
        void updateRuntime(boolean isCurrent, TeamRuntimeView runtimeView, MatchStrategyType matchStrategyType) {
            if (isCurrent) {
                signSlotImage.setDrawable(signDrawable);
            } else {
                signSlotImage.setDrawable(null);
            }
            String healthInfoText;
            switch (matchStrategyType) {
                case PRE:
                    healthInfoText = JavaFeatureForGwt.stringFormat(
                            "剩余题数：%s  分数：%s", 
                            runtimeView.getHealth(),
                            runtimeView.getMatchScore()
                            );
                    break;
                case MAIN:
                    healthInfoText = JavaFeatureForGwt.stringFormat(
                            "剩余生命：%s  分数：%s", 
                            runtimeView.getHealth(),
                            runtimeView.getMatchScore()
                            );
                    break;
                default:
                    healthInfoText = "";
                    break;
            }
            teamInfoLabel2.setText(healthInfoText);
        }
    }

    public void updateTeamPrototype(List<TeamPrototype> teamPrototypes) {

        this.clear();
        nodes.clear();
        teamPrototypes.forEach(it -> {
            TeamInfoNode vm = new TeamInfoNode();
            vm.updatePrototype(it);
            nodes.add(vm);
            TeamInfoBoardVM.this.add(vm).padBottom(TeamInfoNode.SIGN_SIZE / 2).row();
        });
    }
    
    public void updateTeamRuntime(MatchStrategyType matchStrategyType, int currentTeamIndex, List<TeamRuntimeView> teamRuntimeViews) {
        

        for (int i = 0; i < nodes.size(); i++) {
            TeamInfoNode vm = nodes.get(i);
            TeamRuntimeView runtimeView = teamRuntimeViews.get(i);
            vm.updateRuntime(i == currentTeamIndex, runtimeView, matchStrategyType);
        }

    }
    
    
}
