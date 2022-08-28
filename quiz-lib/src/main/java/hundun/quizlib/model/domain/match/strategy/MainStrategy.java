package hundun.quizlib.model.domain.match.strategy;

import hundun.quizlib.model.domain.TeamRuntimeModel;
import hundun.quizlib.prototype.event.SwitchTeamEvent;
import hundun.quizlib.prototype.match.HealthType;
import hundun.quizlib.service.BuffService;
import hundun.quizlib.service.MatchEventFactory;
import hundun.quizlib.service.QuestionService;
import hundun.quizlib.service.RoleSkillService;
import hundun.quizlib.service.TeamService;

/**
 * @author hundun
 * Created on 2019/09/10
 */
public class MainStrategy extends BaseMatchStrategy {
    


    public MainStrategy(
            QuestionService questionService, 
            TeamService teamService, 
            RoleSkillService roleSkillService,
            BuffService buffService
            ) {
        super(questionService, teamService, roleSkillService, buffService,
                HealthType.CONSECUTIVE_WRONG_AT_LEAST
                );
    }

    
    @Override
    public SwitchTeamEvent checkSwitchTeamEvent() {
        /*
         * 每一题换队（被调用一定换）
         */
        TeamRuntimeModel lastTeam = parent.getCurrentTeam();
        switchToNextTeam();
        return MatchEventFactory.getTypeSwitchTeam(lastTeam, parent.getCurrentTeam());
    }

    int fullHealth = 2;
    
    @Override
    public int calculateCurrentHealth() {
        
        /*
         * 连续答错数, 即为健康度的减少量。
         */
        int currentHealth = fullHealth - parent.getRecorder().countConsecutiveWrong(parent.getCurrentTeam().getName(), fullHealth);
        return currentHealth;
    }

}
