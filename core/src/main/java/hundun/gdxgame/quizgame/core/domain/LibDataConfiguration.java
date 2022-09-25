package hundun.gdxgame.quizgame.core.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.gdxgame.quizgame.core.domain.QuizRootSaveData.MyGameSaveData;
import hundun.gdxgame.quizgame.core.domain.QuizSaveHandler.ISubGameSaveHandler;
import hundun.quizlib.context.IQuizComponent;
import hundun.quizlib.context.QuizComponentContext;
import hundun.quizlib.exception.QuizgameException;
import hundun.quizlib.prototype.RolePrototype;
import hundun.quizlib.prototype.TeamPrototype;
import hundun.quizlib.prototype.buff.BuffPrototype;
import hundun.quizlib.prototype.buff.BuffStrategyType;
import hundun.quizlib.prototype.skill.AddBuffSkillEffect;
import hundun.quizlib.prototype.skill.SkillSlotPrototype;
import hundun.quizlib.service.BuffService;
import hundun.quizlib.service.BuiltinDataConfiguration.BuiltinSkill;
import hundun.quizlib.service.RoleSkillService;
import hundun.quizlib.service.TeamService;

/**
 * @author hundun
 * Created on 2021/07/17
 */
public class LibDataConfiguration {
    
    public static String ZACA_TEAM_NAME_1 = "砍口垒同好组";
    public static String ZACA_TEAM_NAME_2 = "方舟同好组";
    
    public static String ZACA_ROLE_NAME = "ZACA娘";
    
    private TeamService teamService;
    private RoleSkillService roleSkillService;
    private BuffService buffService;
    
    
    public LibDataConfiguration(QuizComponentContext context) {
        this.teamService = context.getTeamService();
        this.roleSkillService = context.getRoleSkillService();
        this.buffService = context.getBuffService();
    }

    public void registerForSaveData(List<TeamPrototype> teamPrototypes) throws QuizgameException {
        
        roleSkillService.registerRole(new RolePrototype(
                ZACA_ROLE_NAME, 
                "主人公。", 
                Arrays.asList(
                        roleSkillService.getSkillSlotPrototype(BuiltinSkill.SKILL_5050.name()),
                        roleSkillService.getSkillSlotPrototype(BuiltinSkill.SKILL_SKIP.name()),
                        roleSkillService.getSkillSlotPrototype(BuiltinSkill.SKILL_HELP_1.name()),
                        roleSkillService.getSkillSlotPrototype(BuiltinSkill.SKILL_HELP_2.name())
                )
        )); 
        
        if (teamPrototypes == null) {
            teamService.registerTeam(ZACA_TEAM_NAME_1, 
                    new ArrayList<>(),
                    new ArrayList<>(),
                    ZACA_ROLE_NAME
                    );
            teamService.registerTeam(ZACA_TEAM_NAME_2, 
                    new ArrayList<>(),
                    new ArrayList<>(),
                    ZACA_ROLE_NAME
                    );
        } else {
            for (TeamPrototype teamPrototype : teamPrototypes) {
                String roleName = teamPrototype.getRolePrototype() != null ? teamPrototype.getRolePrototype().getName() : null;
                teamService.registerTeam(teamPrototype.getName(), 
                        teamPrototype.getPickTags(),
                        teamPrototype.getBanTags(),
                        roleName
                        );
            }
        }
        
    }
    
}
