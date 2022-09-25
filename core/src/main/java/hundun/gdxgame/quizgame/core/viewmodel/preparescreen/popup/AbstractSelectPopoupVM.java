package hundun.gdxgame.quizgame.core.viewmodel.preparescreen.popup;

import java.util.List;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.gdxgame.share.base.util.DrawableFactory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @author hundun
 * Created on 2021/11/12
 */
public abstract class AbstractSelectPopoupVM<T extends Actor> extends Table {
    
    
    protected final QuizGdxGame game;
    
    List<T> candidateVMs;
    @Getter
    ScrollPane scrollPane;
    Table childTable;
    LayoutConfig layoutConfig;
    public AbstractSelectPopoupVM(
            QuizGdxGame game,
            Drawable background,
            LayoutConfig layoutConfig
            ) {
        this.game = game;
        this.layoutConfig = layoutConfig;
        this.setBackground(background);
        //this.setBounds(0, 0, game.getWidth(), game.getHeight());

        childTable = new Table();
        childTable.setBackground(DrawableFactory.getSimpleBoardBackground());
        this.scrollPane = new ScrollPane(childTable, game.getMainSkin());
        scrollPane.setScrollingDisabled(!layoutConfig.horizontal, layoutConfig.horizontal);
        
        float areaWidth = !layoutConfig.horizontal ? layoutConfig.CELL_WIDTH : layoutConfig.areaSize * layoutConfig.CELL_WIDTH;
        float areaHeight = layoutConfig.horizontal ? layoutConfig.CELL_HEIGHT : layoutConfig.areaSize * layoutConfig.CELL_HEIGHT;
        this.add(scrollPane)
                .width(areaWidth)
                .height(areaHeight)
                ;
        
        //this.setVisible(false);
    }
    
    public void updateScrollPane(List<T> candidateVMs) {
        childTable.clear();
        this.candidateVMs = candidateVMs;
        candidateVMs.forEach(it -> {
            childTable.add(it)
                    .width(layoutConfig.CELL_WIDTH)
                    .height(layoutConfig.CELL_HEIGHT)
                    .fill()
                    ;
            if (!layoutConfig.horizontal) {
                childTable.row();
            }
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
        boolean horizontal;
    }
}
