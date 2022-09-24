package hundun.quizlib.prototype.event;

import java.util.List;

import hundun.quizlib.prototype.TeamPrototype;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * @author hundun
 * Created on 2021/05/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StartMatchEvent extends MatchEvent {
    List<TeamPrototype> teamPrototypes;
    List<String> questionIds;
}
