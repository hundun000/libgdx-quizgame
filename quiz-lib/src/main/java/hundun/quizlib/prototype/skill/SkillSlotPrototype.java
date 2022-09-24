package hundun.quizlib.prototype.skill;
/**
 * @author hundun
 * Created on 2019/10/08
 */

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SkillSlotPrototype {
    private String name;
    private String showName;
    private String description;
    private List<String> eventArgs;
    private List<AddBuffSkillEffect> backendEffects;
    private int fullUseTime;
}
