package hundun.quizlib.view.team;

import java.util.List;

import hundun.quizlib.view.buff.BuffRuntimeView;
import hundun.quizlib.view.role.RoleRuntimeView;
import lombok.Data;

/**
 * @author hundun
 * Created on 2021/05/10
 */
@Data
public class TeamRuntimeView {
    String name;
    int matchScore;
    RoleRuntimeView roleRuntimeInfo;
    List<BuffRuntimeView> runtimeBuffs;
    boolean alive;
    int health;
}
