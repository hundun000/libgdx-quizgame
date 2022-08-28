package hundun.quizlib.view.skill;

import java.util.List;

import hundun.quizlib.model.domain.SkillSlotRuntimeModel;
import hundun.quizlib.prototype.skill.SkillSlotPrototype;
import hundun.quizlib.view.role.RoleRuntimeView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hundun
 * Created on 2021/07/17
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SkillSlotRuntimeView {
    private String name;
    private int remainUseTime;
}
