package hundun.quizlib.view.match;
/**
 * @author hundun
 * Created on 2021/05/08
 */

import java.util.List;
import java.util.Set;

import hundun.quizlib.prototype.event.AnswerResultEvent;
import hundun.quizlib.prototype.event.MatchFinishEvent;
import hundun.quizlib.prototype.event.SkillResultEvent;
import hundun.quizlib.prototype.event.StartMatchEvent;
import hundun.quizlib.prototype.event.SwitchQuestionEvent;
import hundun.quizlib.prototype.event.SwitchTeamEvent;
import hundun.quizlib.prototype.match.ClientActionType;
import hundun.quizlib.prototype.match.MatchState;
import hundun.quizlib.view.question.QuestionView;
import hundun.quizlib.view.team.TeamRuntimeView;
import lombok.Data;

@Data
public class MatchSituationView {
    String id;
    QuestionView question;
    TeamRuntimeView currentTeamRuntimeInfo;
    int currentTeamIndex;
    List<TeamRuntimeView> teamRuntimeInfos;
    MatchState state;
    Set<ClientActionType> actionAdvices;
    
    AnswerResultEvent answerResultEvent;
    SkillResultEvent skillResultEvent;
    SwitchQuestionEvent switchQuestionEvent;
    SwitchTeamEvent switchTeamEvent;
    MatchFinishEvent finishEvent;
    StartMatchEvent startMatchEvent;
}
