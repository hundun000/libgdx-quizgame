package hundun.quizlib.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import hundun.quizlib.model.domain.SkillSlotRuntimeModel;
import hundun.quizlib.model.domain.TeamRuntimeModel;
import hundun.quizlib.prototype.RolePrototype;
import hundun.quizlib.prototype.TeamPrototype;
import hundun.quizlib.prototype.event.AnswerResultEvent;
import hundun.quizlib.prototype.event.EventType;
import hundun.quizlib.prototype.event.FinishEvent;
import hundun.quizlib.prototype.event.MatchEvent;
import hundun.quizlib.prototype.event.SkillResultEvent;
import hundun.quizlib.prototype.event.StartMatchEvent;
import hundun.quizlib.prototype.event.SwitchQuestionEvent;
import hundun.quizlib.prototype.event.SwitchTeamEvent;
import hundun.quizlib.prototype.match.AnswerType;

/**
 * @author hundun
 * Created on 2020/05/23
 */
public class MatchEventFactory {
    
    public static boolean isTypeInCollection(Collection<MatchEvent> events, EventType type) {
        for (MatchEvent event : events) {
            if (event.getType() == type) {
                return true;
            }
        }
        return false;
    }
    
    public static StartMatchEvent getTypeStartMatch(List<TeamRuntimeModel> teamRuntimeModels){
        StartMatchEvent event = new StartMatchEvent();
        event.setType(EventType.START_MATCH);
        List<TeamPrototype> teamPrototypes = new ArrayList<>();
        for (TeamRuntimeModel teamRuntimeModel : teamRuntimeModels) {
            teamPrototypes.add(teamRuntimeModel.getPrototype());
        }
        event.setTeamPrototypes(teamPrototypes);
        return event;
    }
    
    public static SwitchTeamEvent getTypeSwitchTeam(TeamRuntimeModel lastTeam, TeamRuntimeModel currentTeam) {
        SwitchTeamEvent event = new SwitchTeamEvent();
        event.setType(EventType.SWITCH_TEAM);
        event.setFromTeamName(lastTeam.getName());
        event.setToTeamName(currentTeam.getName());
        return event;
    }
    
    public static SwitchQuestionEvent getTypeSwitchQuestion(int time) {
        SwitchQuestionEvent event = new SwitchQuestionEvent();
        event.setType(EventType.SWITCH_QUESTION);
        event.setTime(time);
        return event;
    }
    
    
    public static FinishEvent getTypeFinish(Map<String, Integer> scores) {
//        ObjectNode data = mapper.createObjectNode();
//        data.put("scores", scores.toString());
        FinishEvent event = new FinishEvent();
        event.setType(EventType.FINISH);
        return event;
    }
    
    public static final String KEY_SKILL_NAME = "skill_name";

    public static SkillResultEvent getTypeSkillSuccess(String teamName, String roleName, SkillSlotRuntimeModel skillSlotRuntimeModel) {
        SkillResultEvent event = new SkillResultEvent();
        event.setType(EventType.SKILL_SUCCESS);
        event.setTeamName(teamName);
        event.setRoleName(roleName);
        event.setSkillName(skillSlotRuntimeModel.getPrototype().getName());
        event.setSkillDesc(skillSlotRuntimeModel.getPrototype().getDescription());
        event.setSkillRemainTime(skillSlotRuntimeModel.getRemainUseTime());
        event.setArgs(skillSlotRuntimeModel.getPrototype().getEventArgs());
        
        return event;
    }
    
    public static SkillResultEvent getTypeSkillUseOut(String teamName, String roleName, SkillSlotRuntimeModel skillSlotRuntimeModel) {
        SkillResultEvent event = new SkillResultEvent();
        event.setType(EventType.SKILL_USE_OUT);
        event.setTeamName(teamName);
        event.setRoleName(roleName);
        event.setSkillName(skillSlotRuntimeModel.getPrototype().getName());
        event.setSkillDesc(skillSlotRuntimeModel.getPrototype().getDescription());
        event.setSkillRemainTime(skillSlotRuntimeModel.getRemainUseTime());
        event.setArgs(skillSlotRuntimeModel.getPrototype().getEventArgs());
        
        return event;
    }
    
    public static AnswerResultEvent getTypeAnswerResult(AnswerType answerType, int addScore, String addScoreTeamName) {
        AnswerResultEvent event = new AnswerResultEvent();
        event.setType(EventType.ANSWER_RESULT);
        event.setAddScore(addScore);
        event.setAddScore(addScore);
        event.setAddScoreTeamName(addScoreTeamName);
        event.setResult(answerType);
        return event;
    }

}
