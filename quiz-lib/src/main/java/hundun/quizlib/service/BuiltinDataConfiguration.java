package hundun.quizlib.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hundun.quizlib.context.IQuizComponent;
import hundun.quizlib.context.QuizComponentContext;
import hundun.quizlib.exception.QuizgameException;
import hundun.quizlib.prototype.RolePrototype;
import hundun.quizlib.prototype.buff.BuffPrototype;
import hundun.quizlib.prototype.buff.BuffStrategyType;
import hundun.quizlib.prototype.skill.AddBuffSkillEffect;
import hundun.quizlib.prototype.skill.SkillSlotPrototype;

/**
 * @author hundun
 * Created on 2021/07/17
 */
public class BuiltinDataConfiguration implements IQuizComponent {
    public static String DEMO_LIST_TEAM_NAME_0 = "游客";
    public static String DEMO_LIST_TEAM_NAME_1 = "游客1";
    public static String DEMO_LIST_TEAM_NAME_2 = "游客2";
    
    public static String HAS_ROLE_TEAM_NAME_1 = "砍口垒同好组";
    public static String HAS_ROLE_TEAM_NAME_2 = "方舟同好组";
    
    public static String DEMO_ROLE_NAME = "ZACA娘";
    
    public enum BuiltinSkill {
        SKILL_SKIP(),
        SKILL_5050(),
        SKILL_HELP_1(),
        SKILL_HELP_2(),
        SKILL_DEMO_BUFF(),
        ;

    }
    
    private TeamService teamService;
    private RoleSkillService roleSkillService;
    private BuffService buffService;
    
    @Override
    public void postConstruct(QuizComponentContext context) throws QuizgameException {
        this.teamService = context.getTeamService();
        this.roleSkillService = context.getRoleSkillService();
        this.buffService = context.getBuffService();

    }
    
    public void register2() throws QuizgameException {
        List<SkillSlotPrototype> prototypes = Arrays.asList(
                new SkillSlotPrototype(BuiltinSkill.SKILL_DEMO_BUFF.name(), "连击之力", "为自己增加一层“连击中”。", null, Arrays.asList(new AddBuffSkillEffect("连击中", 1)), 2)
                );
        prototypes.forEach(it -> roleSkillService.registerSkill(it));
        
        teamService.registerTeam(BuiltinDataConfiguration.DEMO_LIST_TEAM_NAME_0, new ArrayList<>(0), new ArrayList<>(0), null);
        teamService.registerTeam(BuiltinDataConfiguration.DEMO_LIST_TEAM_NAME_1, new ArrayList<>(0), new ArrayList<>(0), null);
        teamService.registerTeam(BuiltinDataConfiguration.DEMO_LIST_TEAM_NAME_2, new ArrayList<>(0), new ArrayList<>(0), null);
        
        buffService.registerBuffPrototype(new BuffPrototype(
                "连击中", 
                "答题正确时，额外获得与“连击中”层数相同的分数，且“连击中”层数加1（最大为3层）；否则，失去所有“连击中”层数。", 
                3, 
                BuffStrategyType.SCORE_MODIFY
                ));
    }
    
    public void register1() throws QuizgameException {
        

        List<SkillSlotPrototype> prototypes = Arrays.asList(
                new SkillSlotPrototype(BuiltinSkill.SKILL_SKIP.name(), "跳过", "结束本题。本题不计入得分、答对数、答错数。", null, null, 2),
                new SkillSlotPrototype(BuiltinSkill.SKILL_5050.name(), "5050", "揭示2个错误选项。", Arrays.asList("2"), null, 2),
                new SkillSlotPrototype(BuiltinSkill.SKILL_HELP_1.name(), "求助现场", "答题时间增加30秒，并且本题期间可与现场观众交流。", Arrays.asList("30"), null, 2),
                new SkillSlotPrototype(BuiltinSkill.SKILL_HELP_2.name(), "求助专家", "答题时间增加30秒，并且本题期间可与专家团交流。", Arrays.asList("30"), null, 2)
                );
        prototypes.forEach(it -> roleSkillService.registerSkill(it));
        
        roleSkillService.registerRole(new RolePrototype(
                DEMO_ROLE_NAME, 
                "主人公。", 
                Arrays.asList(
                        roleSkillService.getSkillSlotPrototype(BuiltinSkill.SKILL_5050.name()),
                        roleSkillService.getSkillSlotPrototype(BuiltinSkill.SKILL_SKIP.name()),
                        roleSkillService.getSkillSlotPrototype(BuiltinSkill.SKILL_HELP_1.name()),
                        roleSkillService.getSkillSlotPrototype(BuiltinSkill.SKILL_HELP_2.name())
                )
        ));
        

        
        
        teamService.registerTeam(HAS_ROLE_TEAM_NAME_1, 
                new ArrayList<>(Arrays.asList("单机游戏")),
                new ArrayList<>(Arrays.asList("动画")),
                DEMO_ROLE_NAME
                );
        teamService.registerTeam(HAS_ROLE_TEAM_NAME_2, 
                new ArrayList<>(Arrays.asList("动画")),
                new ArrayList<>(Arrays.asList("单机游戏")),
                DEMO_ROLE_NAME
                );
        


        
    }



    
}
