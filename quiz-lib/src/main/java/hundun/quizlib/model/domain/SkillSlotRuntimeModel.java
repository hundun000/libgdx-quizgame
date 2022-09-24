package hundun.quizlib.model.domain;

import hundun.quizlib.prototype.skill.SkillSlotPrototype;
import lombok.Data;

/**
 * @author hundun
 * Created on 2021/07/17
 */
@Data
public class SkillSlotRuntimeModel {
    
    private SkillSlotPrototype prototype;
    private int remainUseTime;  
    
    public SkillSlotRuntimeModel(SkillSlotPrototype prototype, int startUseTime) {
        this.prototype = prototype;
        this.remainUseTime = startUseTime;
    }

    public boolean canUseOnce(String skillName) {
        return remainUseTime > 0;
    }
    
    public boolean useOnce(String skillName) {
        if (canUseOnce(skillName)) {
            remainUseTime--;
            return true;
        }
        return false;
    }
}
