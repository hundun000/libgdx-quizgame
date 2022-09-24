package hundun.gdxgame.quizgame.core.viewmodel.preparescreen;
/**
 * @author hundun
 * Created on 2022/08/30
 */

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.gdxgame.quizgame.core.viewmodel.preparescreen.TeamManageAreaVM.ICallerAndCallback;
import hundun.gdxgame.share.base.util.DrawableFactory;
import hundun.quizlib.prototype.TeamPrototype;
import lombok.Getter;

public class TeamManageSlotVM extends Table {
    QuizGdxGame game;
    @Getter
    TeamPrototype data;
    
    Label noTeamLabel;
    Container<Actor> teamNodeAreaContainer;
    
    Button changeTeamButton;
    Button modifyTeamButton;
    
    public TeamManageSlotVM(QuizGdxGame game, ICallerAndCallback callerAndCallback) {
        this.game = game;
        this.noTeamLabel = new Label("待选择", game.getMainSkin());
        this.teamNodeAreaContainer = new Container<>();
        this.changeTeamButton = new TextButton("更换队伍", game.getMainSkin());
        this.modifyTeamButton = new TextButton("配置ban/pick", game.getMainSkin());
        
        changeTeamButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                callerAndCallback.onTeamWantChange(TeamManageSlotVM.this);
            }
        });
        modifyTeamButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                callerAndCallback.onTeamWantModify(TeamManageSlotVM.this);
            }
        });
        
        this.add(teamNodeAreaContainer).width(TeamNodeVM.NODE_WIDTH).height(TeamNodeVM.NODE_HEIGHT).padRight(50);
        this.add(changeTeamButton).grow().padRight(50);
        this.add(modifyTeamButton).grow();
        
        this.setBackground(DrawableFactory.getViewportBasedAlphaBoard(1, 1));
    }
    
    public void updateData(TeamPrototype data) {
        this.data = data;
        if (data != null) {
            teamNodeAreaContainer.setActor(new TeamNodeVM(game, data));
            modifyTeamButton.setTouchable(Touchable.enabled);
        } else {
            teamNodeAreaContainer.setActor(noTeamLabel);
            modifyTeamButton.setTouchable(Touchable.disabled);
        }
    }
    
    

}
