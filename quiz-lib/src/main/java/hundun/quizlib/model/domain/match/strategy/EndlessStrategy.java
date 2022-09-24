package hundun.quizlib.model.domain.match.strategy;

import hundun.quizlib.prototype.event.SwitchTeamEvent;
import hundun.quizlib.prototype.match.HealthType;
import hundun.quizlib.service.BuffService;
import hundun.quizlib.service.QuestionService;
import hundun.quizlib.service.RoleSkillService;
import hundun.quizlib.service.TeamService;

/**
 * @author hundun
 * Created on 2021/04/25
 */
public class EndlessStrategy extends BaseMatchStrategy {

    public EndlessStrategy(QuestionService questionService, TeamService teamService, RoleSkillService roleSkillService,
            BuffService buffService) {
        super(
                questionService, 
                teamService, 
                roleSkillService, 
                buffService, 
                HealthType.ENDLESS
                );
    }

    @Override
    public int calculateCurrentHealth() {
        /*
         * 一定不死亡
         */
        return 1;
    }


    @Override
    public SwitchTeamEvent checkSwitchTeamEvent() {
        /*
         * 一定不换队
         */
        return null;
    }

    @Override
    public int calculateSkillStartUseTime(int fullUseTime) {
        return 0;
    }

}
