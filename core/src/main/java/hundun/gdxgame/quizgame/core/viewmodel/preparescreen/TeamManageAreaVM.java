package hundun.gdxgame.quizgame.core.viewmodel.preparescreen;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.quizlib.prototype.TeamPrototype;

/**
 * @author hundun
 * Created on 2022/09/14
 */
public class TeamManageAreaVM extends Table {
    QuizGdxGame game;
    ICallerAndCallback callerAndCallback;

    List<TeamManageSlotVM> teamSlotVMs = new ArrayList<>();
    TeamManageSlotVM operatingSlotVM;
    
    public TeamManageAreaVM(QuizGdxGame game, ICallerAndCallback callerAndCallback) {
        this.game = game;
        this.callerAndCallback = callerAndCallback;
        //this.setBackground(DrawableFactory.getSimpleBoardBackground());
    }
    
    private static final int SLOT_OR_EMPTY_SIZE = 3;
//    public static final int SLOT_WIDTH = 600;
//    public static final int SLOT_HEIGHT = 100;
    
    public void updateSlotNum(int targetSlotNum) {
        
        if (targetSlotNum != teamSlotVMs.size()) {
            this.clear();
            teamSlotVMs.clear();
            for (int i = 0; i < SLOT_OR_EMPTY_SIZE; i++) {
                Actor content;
                if (i < targetSlotNum) {
                    TeamManageSlotVM teamSlotVM = new TeamManageSlotVM(game, callerAndCallback);
                    teamSlotVM.updateData(null);
                    teamSlotVMs.add(teamSlotVM);
                    content = teamSlotVM;
                } else {
                    content = new Image();
                }
                this.add(content)
//                        .width(SLOT_WIDTH)
                        .height(TeamNodeVM.NODE_HEIGHT)
//                        .fill()
                        .padBottom((TeamNodeVM.NODE_HEIGHT * 0.25f))
                        .row();
            }
        }
        
        if (game.debugMode) {
            this.debugCell();
        }
    }
    
    public static interface ICallerAndCallback {
        void onTeamWantChange(TeamManageSlotVM teamSlotVM);
        void onTeamWantModify(TeamManageSlotVM teamSlotVM);
    }

    public List<String> getSelectedTeamNames() {
        return teamSlotVMs.stream().map(it -> it.getData().getName()).collect(Collectors.toList());
    }

    public List<String> updateWaitChangeDone(TeamPrototype teamPrototype) {
        this.operatingSlotVM.updateData(teamPrototype);
        return teamSlotVMs.stream()
                .filter(it -> it.getData() != null)
                .map(it -> it.getData().getName())
                .collect(Collectors.toList());
    }

    public void onTeamWantChange(TeamManageSlotVM teamSlotVM) {
        this.operatingSlotVM = teamSlotVM;
    }


}
