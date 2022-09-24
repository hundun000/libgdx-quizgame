package hundun.quizlib.view.role;

import java.util.List;

import hundun.quizlib.view.skill.SkillSlotRuntimeView;
import lombok.Data;

/**
 * @author hundun
 * Created on 2021/06/28
 */
@Data
public class RoleRuntimeView {
    String name;
    List<SkillSlotRuntimeView> skillSlotRuntimeViews;
}
