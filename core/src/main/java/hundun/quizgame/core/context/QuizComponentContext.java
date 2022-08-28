package hundun.quizgame.core.context;

import hundun.quizgame.core.model.domain.match.strategy.MatchStrategyFactory;
import hundun.quizgame.core.service.BuffService;
import hundun.quizgame.core.service.BuiltinDataConfiguration;
import hundun.quizgame.core.service.GameService;
import hundun.quizgame.core.service.QuestionLoaderService;
import hundun.quizgame.core.service.QuestionService;
import hundun.quizgame.core.service.RoleSkillService;
import hundun.quizgame.core.service.SessionService;
import hundun.quizgame.core.service.TeamService;
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
