package hundun.gdxgame.quizgame.core.viewmodel.preparescreen.popup;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.gdxgame.quizgame.core.viewmodel.preparescreen.TeamManageSlotVM;
import hundun.gdxgame.quizgame.core.viewmodel.preparescreen.TeamNodeVM;
import hundun.gdxgame.quizgame.core.viewmodel.preparescreen.popup.AbstractSelectPopoupVM.LayoutConfig;
import hundun.gdxgame.quizgame.core.viewmodel.preparescreen.popup.TagSelectPopoupVM.TagSelectDoneClickListener;
import hundun.gdxgame.share.base.util.DrawableFactory;
import hundun.quizlib.prototype.TeamPrototype;

/**
 * @author hundun
 * Created on 2022/09/14
 */
public class TeamSelectPopoupVM extends AbstractSelectPopoupVM<TeamNodeVM> {

    IWaitTeamSelectCallback callback;
    Button doneButton;
    
    public TeamSelectPopoupVM(
            QuizGdxGame game, 
            IWaitTeamSelectCallback callback
            ) {
        super(game, 
                DrawableFactory.getViewportBasedAlphaBoard(game.getWidth(), game.getHeight()), 
                new LayoutConfig(TeamNodeVM.NODE_WIDTH, TeamNodeVM.NODE_HEIGHT, 2.5f, false));
        this.callback = callback;
        
        this.doneButton = new TextButton("返回", game.getMainSkin());
        
        doneButton.addListener(new TeamSelectClickListener(callback, null));
        
        this.row();
        this.add(doneButton);
    }
    
    public static class TeamSelectClickListener extends ClickListener {
        IWaitTeamSelectCallback callback;
        TeamNodeVM teamNodeVM;
        TeamSelectClickListener(IWaitTeamSelectCallback callback, TeamNodeVM teamNodeVM) {
            this.callback = callback;
            this.teamNodeVM = teamNodeVM;
        }
        public void clicked(InputEvent event, float x, float y) {
            super.clicked(event, x, y);
            callback.onTeamSelectDone(teamNodeVM != null ? teamNodeVM.getData() : null);
        };
    }
    
    public static interface IWaitTeamSelectCallback {
        void callShowTeamSelectPopoup();
        void onTeamSelectDone(TeamPrototype teamPrototype);
    }

    public void callShow(List<TeamPrototype> teamPrototypes) {
        List<TeamNodeVM> candidateVMs = teamPrototypes.stream()
                .map(teamPrototype -> {
                    TeamNodeVM teamNodeVM = new TeamNodeVM(game, teamPrototype);
                    teamNodeVM.addListener(new TeamSelectClickListener(callback, teamNodeVM));
                    teamNodeVM.setTouchable(Touchable.enabled);
                    return teamNodeVM;
                })
                .collect(Collectors.toList())
                ;
        updateScrollPane(candidateVMs);
    }

}
