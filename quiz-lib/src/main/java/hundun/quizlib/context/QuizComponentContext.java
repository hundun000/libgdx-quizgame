package hundun.quizlib.context;

import hundun.quizlib.model.domain.match.strategy.MatchStrategyFactory;
import hundun.quizlib.service.BuffService;
import hundun.quizlib.service.BuiltinDataConfiguration;
import hundun.quizlib.service.GameService;
import hundun.quizlib.service.QuestionLoaderService;
import hundun.quizlib.service.QuestionService;
import hundun.quizlib.service.RoleSkillService;
import hundun.quizlib.service.SessionService;
import hundun.quizlib.service.TeamService;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author hundun
 * Created on 2022/08/29
 */
@Getter
@AllArgsConstructor
public class QuizComponentContext {
    
    private IFrontEnd frontEnd;
    
    private GameService gameService;
    private QuestionService questionService;
    private TeamService teamService;
    private RoleSkillService roleSkillService;
    private BuffService buffService;
    private SessionService sessionService;
    private MatchStrategyFactory matchStrategyFactory;
    private QuestionLoaderService questionLoaderService;
    private BuiltinDataConfiguration builtinDataConfiguration;
    
    public static class Factory {
        public static QuizComponentContext create(IFrontEnd frontEnd) {
            QuizComponentContext context = new QuizComponentContext(
                    frontEnd,
                    new GameService(),
                    new QuestionService(), 
                    new TeamService(), 
                    new RoleSkillService(), 
                    new BuffService(), 
                    new SessionService(), 
                    new MatchStrategyFactory(), 
                    new QuestionLoaderService(),
                    new BuiltinDataConfiguration()
                    );
            context.gameService.postConstruct(context);
            context.questionService.postConstruct(context);
            context.teamService.postConstruct(context);
            //context.roleSkillService.postConstruct(context);
            //context.buffService.postConstruct(context);
            context.sessionService.postConstruct(context);
            context.matchStrategyFactory.postConstruct(context);
            context.questionLoaderService.postConstruct(context);
            context.builtinDataConfiguration.postConstruct(context);
            
            return context;
        }
    }
}
