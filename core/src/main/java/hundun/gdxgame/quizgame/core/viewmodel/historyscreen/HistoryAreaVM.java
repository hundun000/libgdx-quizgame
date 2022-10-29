package hundun.gdxgame.quizgame.core.viewmodel.historyscreen;

import java.util.List;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.gdxgame.quizgame.core.viewmodel.share.MatchHistoryVM;
import hundun.gdxgame.corelib.base.util.DrawableFactory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @author hundun
 * Created on 2021/11/12
 */
public class HistoryAreaVM extends Table {
    
    
    protected final QuizGdxGame game;
    
    List<MatchHistoryVM> candidateVMs;
    @Getter
    ScrollPane scrollPane;
    Table childTable;
    LayoutConfig layoutConfig;
    public HistoryAreaVM(
            QuizGdxGame game,
            Drawable background,
            LayoutConfig layoutConfig
            ) {
        this.game = game;
        this.layoutConfig = layoutConfig;
        this.setBackground(background);
        //this.setBounds(0, 0, game.getWidth(), game.getHeight());

        childTable = new Table();
        childTable.setBackground(game.getTextureConfig().getHistoryAreaVMBackgroundDrawable());
        this.scrollPane = new ScrollPane(childTable, game.getMainSkin());
        scrollPane.setScrollingDisabled(true, false);
        
        float areaWidth = layoutConfig.CELL_WIDTH;
        float areaHeight = layoutConfig.areaSize * layoutConfig.CELL_HEIGHT;
        this.add(scrollPane)
                .width(areaWidth)
                .height(areaHeight)
                ;
        
        //this.setVisible(false);
    }
    
    public void updateScrollPane(List<MatchHistoryVM> candidateVMs) {
        childTable.clear();
        this.candidateVMs = candidateVMs;
        candidateVMs.forEach(it -> {
            childTable.add(it)
                    .width(layoutConfig.CELL_WIDTH)
                    .height(layoutConfig.CELL_HEIGHT)
                    .fill()
                    ;
            childTable.row();
        });
        if (game.debugMode) {
            childTable.debugCell();
        }
    }
    
    @Data
    @AllArgsConstructor
    public static class LayoutConfig {
        private int CELL_WIDTH;
        private int CELL_HEIGHT;
        private float areaSize;
    }
}
