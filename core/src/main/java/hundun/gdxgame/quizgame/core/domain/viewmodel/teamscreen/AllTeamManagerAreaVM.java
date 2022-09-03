package hundun.gdxgame.quizgame.core.domain.viewmodel.teamscreen;
/**
 * @author hundun
 * Created on 2022/08/30
 */

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import hundun.gdxgame.quizgame.core.screen.QuizPlayScreen;
import hundun.gdxgame.quizgame.core.screen.TeamScreen;
import hundun.gdxgame.share.base.util.DrawableFactory;
import hundun.quizlib.prototype.TeamPrototype;

public class AllTeamManagerAreaVM extends Table {
    
    public final int LR_BUTTON_HEIGHT = 150;
    public final int LR_BUTTON_WIDTH = 10;
    
    public final int TeamManagerVM_WIDTH = 200;
    public final int TeamManagerVM_ALL_AREA_WIDTH = (int) (TeamManagerVM_WIDTH * 2.0);
    
    List<TeamManagerVM> teamManagerVMs = new ArrayList<>();

    ImageButton leftButton;
    ImageButton rightButton;
    Table childTable;
    
    TeamScreen parent;
    
    public AllTeamManagerAreaVM(TeamScreen parent) {
        this.parent = parent;
        
        

        childTable = new Table();
        childTable.setBackground(DrawableFactory.getSimpleBoardBackground());
        ScrollPane scrollPane = new ScrollPane(childTable, parent.getGame().getMainSkin());
        scrollPane.setScrollingDisabled(false, true);

        leftButton = new ImageButton(DrawableFactory.createBorderBoard(LR_BUTTON_WIDTH, LR_BUTTON_HEIGHT, 0.8f, 3));
//        leftButton.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                super.clicked(event, x, y);
//                
//            }
//        });
        rightButton = new ImageButton(DrawableFactory.createBorderBoard(LR_BUTTON_WIDTH, LR_BUTTON_HEIGHT, 0.8f, 3));
//        rightButton.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                super.clicked(event, x, y);
//
//            }
//        });

        //this.add(leftButton);
        this.add(scrollPane).width(TeamManagerVM_ALL_AREA_WIDTH);
        //this.add(rightButton);
        this.setBackground(DrawableFactory.getSimpleBoardBackground());

        if (parent.getGame().debugMode) {
            this.debugCell();
        }
    }
    
    public void updateData(List<TeamPrototype> teamPrototypes) {
        Gdx.app.log(this.getClass().getSimpleName(), "updateData teamPrototypes size = " + teamPrototypes.size());
        childTable.clearChildren();
        teamManagerVMs.clear();
        
        for (int i = 0; i < teamPrototypes.size(); i++) {
            TeamPrototype teamPrototype = teamPrototypes.get(i);
            TeamManagerVM teamManagerVM = new TeamManagerVM(parent.getGame());
            teamManagerVM.updateData(teamPrototype);
            teamManagerVMs.add(teamManagerVM);
            childTable.add(teamManagerVM).spaceRight(10).width(TeamManagerVM_WIDTH).height(LR_BUTTON_HEIGHT);
        }
        
        if (parent.getGame().debugMode) {
            childTable.debugCell();
        }
    }
}
