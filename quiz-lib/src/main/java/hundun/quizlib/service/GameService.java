package hundun.quizlib.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import hundun.quizlib.context.IQuizComponent;
import hundun.quizlib.context.QuizComponentContext;
import hundun.quizlib.exception.QuizgameException;
import hundun.quizlib.model.SessionDataPackage;
import hundun.quizlib.model.domain.RoleRuntimeModel;
import hundun.quizlib.model.domain.SkillSlotRuntimeModel;
import hundun.quizlib.model.domain.TeamRuntimeModel;
import hundun.quizlib.model.domain.match.BaseMatch;
import hundun.quizlib.model.domain.match.MatchRecord;
import hundun.quizlib.model.domain.match.strategy.BaseMatchStrategy;
import hundun.quizlib.model.domain.match.strategy.MatchStrategyFactory;
import hundun.quizlib.prototype.TeamPrototype;
import hundun.quizlib.prototype.match.MatchConfig;
import hundun.quizlib.view.match.MatchSituationView;

/**
 *
 * @author hundun
 * Created on 2019/03/19
 */
public class GameService implements IQuizComponent {

    private TeamService teamService;
    private MatchStrategyFactory matchStrategyFactory;
    private SessionService sessionService;
    
    LinkedHashMap<String, MatchRecord> matchRecords = new LinkedHashMap<>();
    
    public GameService() {
    	
    }

    @Override
    public void postConstruct(QuizComponentContext context) {
        this.teamService = context.getTeamService();
        this.matchStrategyFactory = context.getMatchStrategyFactory();
        this.sessionService = context.getSessionService();
    }
    
    public MatchSituationView createMatch(MatchConfig matchConfig) throws QuizgameException {
        //log.info("createEndlessMatch by {}", matchConfig);
        
        BaseMatchStrategy strategy = matchStrategyFactory.getMatchStrategy(matchConfig.getMatchStrategyType());
        
        SessionDataPackage sessionDataPackage = sessionService.createSession(matchConfig.getQuestionPackageName());
        
        BaseMatch match = new BaseMatch(sessionDataPackage.getSessionId(), strategy);
        List<TeamRuntimeModel> teamRuntimeModels = new ArrayList<>();
        for (String teamName : matchConfig.getTeamNames()) {
            TeamPrototype teamPrototype = teamService.getTeam(teamName);
            RoleRuntimeModel roleRuntimeModel;
            if (teamPrototype.getRolePrototype() != null) {
                List<SkillSlotRuntimeModel> skillSlotRuntimeModels = teamPrototype.getRolePrototype().getSkillSlotPrototypes().stream()
                        .map(prototype -> {
                            int startUseTime = strategy.calculateSkillStartUseTime(prototype.getFullUseTime());
                            SkillSlotRuntimeModel runtimeModel = new SkillSlotRuntimeModel(prototype, startUseTime);
                            return runtimeModel;
                        })
                        .collect(Collectors.toList())
                        ;
                roleRuntimeModel = new RoleRuntimeModel(teamPrototype.getRolePrototype(), skillSlotRuntimeModels);
            } else {
                roleRuntimeModel = null;
            }
            teamRuntimeModels.add(new TeamRuntimeModel(teamPrototype, roleRuntimeModel));
        }
        match.initTeams(teamRuntimeModels);
        
        sessionDataPackage.setMatch(match);
        
        //log.info("match created, id = {}", match.getSessionId());
        MatchSituationView matchSituationView = match.toMatchSituationView();
        return matchSituationView;
    }
    

    

    
    public MatchSituationView startMatch(String sessionId) throws QuizgameException {
        //log.info("start match:{}", sessionId);
        SessionDataPackage sessionDataPackage = sessionService.getSessionDataPackage(sessionId);
        BaseMatch match = sessionDataPackage.getMatch();
        match.start(sessionDataPackage.getQuestionIds());
        MatchSituationView matchSituationView = match.toMatchSituationView();
        return matchSituationView;
    }
    
    public MatchSituationView nextQustion(String sessionId) throws QuizgameException {
        //log.info("nextQustion:{}", sessionId);
        SessionDataPackage sessionDataPackage = sessionService.getSessionDataPackage(sessionId);
        BaseMatch match = sessionDataPackage.getMatch();
        match.nextQustion();
        MatchSituationView matchSituationView = match.toMatchSituationView();
        return matchSituationView;
    }
    
    private void finishMatch(BaseMatch match) throws QuizgameException {
        matchRecords.put(match.getSessionId(), new MatchRecord(match));
        
    }
    
    
    public MatchSituationView teamAnswer(String sessionId, String answer) throws QuizgameException {
        //log.info("teamAnswer match:{}, answer = {}", sessionId, answer);
        SessionDataPackage sessionDataPackage = sessionService.getSessionDataPackage(sessionId);
        BaseMatch match = sessionDataPackage.getMatch();
        match.teamAnswer(answer);
        if (match.finishEvent != null) {
            finishMatch(match);
        }
        MatchSituationView matchSituationView = match.toMatchSituationView();
        return matchSituationView;
    }
	
    public MatchSituationView teamUseSkill(String sessionId, String skillName) throws QuizgameException {
        //log.info("teamUseSkill match:{}, skillName = {}", sessionId, skillName);
        SessionDataPackage sessionDataPackage = sessionService.getSessionDataPackage(sessionId);
        BaseMatch match = sessionDataPackage.getMatch();
        match.teamUseSkill(skillName);
        MatchSituationView matchSituationView = match.toMatchSituationView();
        return matchSituationView;
    }



    

    
    

}
