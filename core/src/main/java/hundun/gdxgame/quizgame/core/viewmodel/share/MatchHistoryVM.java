package hundun.gdxgame.quizgame.core.viewmodel.share;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.gdxgame.quizgame.core.screen.HistoryScreen.MatchHistoryDTO;
import hundun.gdxgame.share.base.util.DrawableFactory;

/**
 * @author hundun
 * Created on 2022/09/09
 */
public class MatchHistoryVM extends Table {

    
    public static final int MatchHistoryVM_WIDTH = 800;
    public static final int MatchHistoryVM_HEIGHT = 100;
    
    static final int NUM_SLOT = 2;
    List<TeamScorePairSlotVM> slotVMs = new ArrayList<>();
    
    public MatchHistoryVM(QuizGdxGame game) {
        this.padRight(50);
        for (int i = 0; i < NUM_SLOT; i++) {
            TeamScorePairSlotVM slotVM = new TeamScorePairSlotVM(game);
            slotVMs.add(slotVM);
            this.add(slotVM).row();
        }
        if (game.debugMode) {
            this.debugAll();
        }
    }
    
    public static class Factory {
        
        public static MatchHistoryVM fromMap(QuizGdxGame game, Map<String, Integer> data) {
            MatchHistoryVM vm = new MatchHistoryVM(game);
            
            List<String> keys = new ArrayList<>(data.keySet());
            for (int i = 0; i < NUM_SLOT; i++) {
                if (keys.size() > 0) {
                    String key = keys.remove(0);
                    vm.slotVMs.get(i).update(key, data.get(key));
                } else {
                    vm.slotVMs.get(i).update(null, null);
                }
            }
            
            return vm;
        }
        
        public static MatchHistoryVM fromBO(QuizGdxGame game, MatchHistoryDTO history) {
            return fromMap(game, history.getData());
        }
    }
    
    
    public static class TeamScorePairSlotVM extends Table {
        final QuizGdxGame game;
        
        public TeamScorePairSlotVM(QuizGdxGame game) {
            this.game = game;
            this.setBackground(DrawableFactory.getSimpleBoardBackground());
        }
        
        public void update(String name, Integer score) {
            
            Actor leftPart;
            Actor rightPart;
            if (name != null) {
                leftPart = new Label(name, game.getMainSkin());
                rightPart = new Label(score + "分", game.getMainSkin());
            } else {
                leftPart = new Image();
                rightPart = new Image();
            }
            
            this.clear();
            this.add(leftPart)
                    .width(600)
                    .height(50)
                    .padRight(10)
                    ;
            this.add(rightPart)
                    .width(100)
                    .height(50)
                    ;
        }
    }
}
