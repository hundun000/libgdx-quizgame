package hundun.quizlib.prototype.event;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * @author hundun
 * Created on 2021/05/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SkillResultEvent extends MatchEvent {
    String teamName;
    String roleName;
    String skillName;
    String skillDesc;
    int skillRemainTime;
    List<String> args;
}
