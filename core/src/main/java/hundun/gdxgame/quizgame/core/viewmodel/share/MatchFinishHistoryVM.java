package hundun.gdxgame.quizgame.core.viewmodel.share;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

import hundun.gdxgame.quizgame.core.QuizGdxGame;

/**
 * @author hundun
 * Created on 2022/09/09
 */
public class MatchFinishHistoryVM extends HorizontalGroup {

    static final int NUM_NODE = 2; 
    List<TeamScorePairVM> pairs = new ArrayList<>();
    
    public MatchFinishHistoryVM(QuizGdxGame game, Map<String, Integer> data) {
        this.padRight(50);
        for (int i = 0; i < NUM_NODE; i++) {
            TeamScorePairVM pair = new TeamScorePairVM(game);
            pairs.add(pair);
            this.addActor(pair);
        }
        
        List<String> keys = new ArrayList<>(data.keySet());
        for (int i = 0; i < NUM_NODE; i++) {
            if (keys.size() > 0) {
                String key = keys.remove(0);
                pairs.get(i).update(key, data.get(key));
            } else {
                pairs.get(i).update(null, null);
            }
        }
        
    }
    
    
    public static class TeamScorePairVM extends Table {
        final QuizGdxGame game;
        
        public TeamScorePairVM(QuizGdxGame game) {
            this.game = game;
        }
        
        public void update(String name, Integer score) {
            
            Actor leftPart;
            Actor rightPart;
            if (name != null) {
                leftPart = new Label(name, game.getMainSkin());
                rightPart = new Label(String.valueOf(score), game.getMainSkin());
            } else {
                leftPart = new Image();
                rightPart = new Image();
            }
            
            this.clear();
            this.add(leftPart)
                    .width(100)
                    .padRight(10)
                    ;
            this.add(rightPart)
                    .width(100)
                    ;
        }
    }
}
