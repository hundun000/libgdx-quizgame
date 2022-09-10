package hundun.quizlib.service;

import java.util.Arrays;
import java.util.List;

import hundun.quizlib.prototype.skill.AddBuffSkillEffect;
import hundun.quizlib.prototype.skill.SkillSlotPrototype;

/**
 * @author hundun
 * Created on 2022/09/15
 */
public class BuiltinSkillSlotPrototypeFactory {

    public static final String SKILL_NAME_SKIP = "跳过";
    public static final String SKILL_NAME_5050 = "5050";
    public static final String SKILL_NAME_HELP_1 = "求助现场";
    public static final String SKILL_NAME_HELP_2 = "求助专家";
    public static final String SKILL_NAME_DEMO_BUFF = "连击之力";
    
    public List<SkillSlotPrototype> name() {
        
        List<SkillSlotPrototype> prototypes = Arrays.asList(
                new SkillSlotPrototype(SKILL_NAME_SKIP, "结束本题。本题不计入得分、答对数、答错数。", null, null, 2),
                new SkillSlotPrototype(SKILL_NAME_5050, "揭示2个错误选项。", Arrays.asList("2"), null, 2),
                new SkillSlotPrototype(SKILL_NAME_HELP_1, "答题时间增加30秒，并且本题期间可与现场观众交流。", Arrays.asList("30"), null, 2),
                new SkillSlotPrototype(SKILL_NAME_HELP_2, "答题时间增加30秒，并且本题期间可与专家团交流。", Arrays.asList("30"), null, 2),
                new SkillSlotPrototype(SKILL_NAME_DEMO_BUFF, "为自己增加一层“连击中”。", null, Arrays.asList(new AddBuffSkillEffect("连击中", 1)), 2)
                );
        return prototypes;
    }
    
}
