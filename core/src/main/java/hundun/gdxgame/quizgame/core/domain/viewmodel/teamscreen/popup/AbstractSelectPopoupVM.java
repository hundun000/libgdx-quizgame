package hundun.gdxgame.quizgame.core.domain.viewmodel.teamscreen.popup;

import java.util.List;
import java.util.function.Function;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.gdxgame.quizgame.core.domain.viewmodel.teamscreen.TeamNodeVM;
import hundun.gdxgame.share.base.util.DrawableFactory;
import hundun.quizlib.prototype.match.MatchConfig;
import hundun.quizlib.view.match.MatchSituationView;

/**
 * @author hundun
 * Created on 2021/11/12
 */
public abstract class AbstractSelectPopoupVM<T extends Actor> extends Table {
    public final int TeamManagerVM_WIDTH = 200;
    public final int TeamManagerVM_ALL_AREA_WIDTH = (int) (TeamManagerVM_WIDTH * 2.5);
    
    protected final QuizGdxGame game;
    
    List<T> candidateVMs;
    
    Table childTable;
    
    public AbstractSelectPopoupVM(
            QuizGdxGame game,
            Drawable background
            ) {
        this.game = game;
        this.setBackground(background);
        //this.setBounds(0, 0, game.getWidth(), game.getHeight());

        childTable = new Table();
        childTable.setBackground(DrawableFactory.getSimpleBoardBackground());
        ScrollPane scrollPane = new ScrollPane(childTable, game.getMainSkin());
        scrollPane.setScrollingDisabled(false, true);
        this.add(scrollPane).width(TeamManagerVM_ALL_AREA_WIDTH);
        
        
        //this.setVisible(false);
    }
    
    public void updateScrollPane(List<T> candidateVMs) {
        childTable.clear();
        this.candidateVMs = candidateVMs;
        candidateVMs.forEach(it -> {
            childTable.add(it).row();
        });
    }
}
