package hundun.quizlib.model.domain;

import java.util.ArrayList;
import java.util.List;

import hundun.quizlib.prototype.RolePrototype;
import hundun.quizlib.view.role.RoleRuntimeView;
import hundun.quizlib.view.skill.SkillSlotRuntimeView;
import lombok.Getter;

/**
 * @author hundun
 * Created on 2019/10/26
 */
@Getter
public class RoleRuntimeModel {
    
    private final RolePrototype prototype;

    private final List<SkillSlotRuntimeModel> skillSlotRuntimeModels = new ArrayList<>();
    
    public RoleRuntimeModel(RolePrototype prototype) {
        this.prototype = prototype;
        skillSlotRuntimeModels.clear();
        prototype.getSkillSlotPrototypes().forEach(skillSlotPrototype -> skillSlotRuntimeModels.add(new SkillSlotRuntimeModel(skillSlotPrototype)));
    }
    
    

    public RoleRuntimeView toRoleRuntimeInfoDTO() {
        RoleRuntimeView dto = new RoleRuntimeView();
        dto.setName(prototype.getName());
        List<SkillSlotRuntimeView> skillSlotRuntimeViews = new ArrayList<>();
        skillSlotRuntimeModels.forEach(model -> skillSlotRuntimeViews.add(new SkillSlotRuntimeView(model.getPrototype().getName(), model.getRemainUseTime())));
        dto.setSkillSlotRuntimeViews(skillSlotRuntimeViews);
        return dto;
    }

    
}
