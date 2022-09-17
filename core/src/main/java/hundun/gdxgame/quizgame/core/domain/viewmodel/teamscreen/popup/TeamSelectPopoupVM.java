package hundun.gdxgame.quizgame.core.domain.viewmodel.teamscreen.popup;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.gdxgame.quizgame.core.domain.viewmodel.teamscreen.TeamNodeVM;
import hundun.gdxgame.share.base.util.DrawableFactory;
import hundun.quizlib.prototype.TeamPrototype;

/**
 * @author hundun
 * Created on 2022/09/14
 */
public class TeamSelectPopoupVM extends AbstractSelectPopoupVM<TeamNodeVM> {

    IWaitTeamSelectCallback callback;
    public TeamSelectPopoupVM(
            QuizGdxGame game, 
            IWaitTeamSelectCallback callback,
            Drawable background
            ) {
        super(game, background);
        this.callback = callback;
        
        
    }
    
    public static class Factory {
        public static TeamSelectPopoupVM build(QuizGdxGame game, IWaitTeamSelectCallback callback) {
            
            return new TeamSelectPopoupVM(game, 
                    callback,
                    DrawableFactory.getSimpleBoardBackground()
                    );
        }
    }
    
    public static class TeamSelectClickListener extends ClickListener {
        IWaitTeamSelectCallback callback;
        TeamNodeVM teamNodeVM;
        TeamSelectClickListener(IWaitTeamSelectCallback callback, TeamNodeVM teamNodeVM) {
            this.callback = callback;
            this.teamNodeVM = teamNodeVM;
        }
        @Override
        public void clicked(InputEvent event, float x, float y) {
            super.clicked(event, x, y);
            callback.onTeamSelectDone(teamNodeVM.getData());
        }
    }
    
    public static interface IWaitTeamSelectCallback {
        void callShowTeamSelectPopoup();
        void onTeamSelectDone(TeamPrototype teamPrototype);
    }

    public void callShow(List<TeamPrototype> teamPrototypes) {
        List<TeamNodeVM> candidateVMs = teamPrototypes.stream()
                .map(teamPrototype -> {
                    TeamNodeVM teamNodeVM = new TeamNodeVM(game);
                    teamNodeVM.updateData(teamPrototype);
                    teamNodeVM.addListener(new TeamSelectClickListener(callback, teamNodeVM));
                    return teamNodeVM;
                })
                .collect(Collectors.toList())
                ;
        updateScrollPane(candidateVMs);
    }
}
