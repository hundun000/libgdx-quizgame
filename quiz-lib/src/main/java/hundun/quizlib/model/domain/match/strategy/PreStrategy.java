package hundun.quizlib.model.domain.match.strategy;

import hundun.quizlib.prototype.event.SwitchTeamEvent;
import hundun.quizlib.prototype.match.HealthType;
import hundun.quizlib.service.BuffService;
import hundun.quizlib.service.QuestionService;
import hundun.quizlib.service.RoleSkillService;
import hundun.quizlib.service.TeamService;

/**
 * @author hundun
 * Created on 2019/09/06
 */
public class PreStrategy extends BaseMatchStrategy {
    

    public PreStrategy(
            QuestionService questionService, 
            TeamService teamService, 
            RoleSkillService roleSkillService,
            BuffService buffService
            ) {
        super(questionService, teamService, roleSkillService, buffService,
                HealthType.SUM
                );
    }
    

    
    @Override
    public SwitchTeamEvent checkSwitchTeamEvent() {
        /*
         * 一定不换队
         */
        return null;
    }



    @Override
    public int calculateCurrentHealth() {
        /*
         * 累计答n题后死亡
         */
        int fullHealth = 5;
        int currentHealth = fullHealth - parent.getRecorder().countSum(parent.getCurrentTeam().getName(), fullHealth);
        
        return currentHealth;
    }

    @Override
    public int calculateSkillStartUseTime(int fullUseTime) {
        return 0;
    }

}
