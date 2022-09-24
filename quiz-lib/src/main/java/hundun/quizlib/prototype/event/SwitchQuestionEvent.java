package hundun.quizlib.prototype.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * @author hundun
 * Created on 2021/05/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SwitchQuestionEvent extends MatchEvent {
    int time;
}
