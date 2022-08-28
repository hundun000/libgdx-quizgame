package hundun.quizgame.core.model.domain.match.strategy;

import hundun.quizgame.core.context.IQuizComponent;
import hundun.quizgame.core.context.QuizComponentContext;
import hundun.quizgame.core.exception.NotFoundException;
import hundun.quizgame.core.exception.QuizgameException;
import hundun.quizgame.core.prototype.match.MatchStrategyType;

/**
 * @author hundun
 * Created on 2021/06/28
 */
public class MatchStrategyFactory implements IQuizComponent {
 
    private QuizComponentContext context;
    
    @Override
    public void postConstruct(QuizComponentContext context) {
        this.context = context;
    }
    
    public BaseMatchStrategy getMatchStrategy(MatchStrategyType type) throws QuizgameException {
        switch (type) {
            case ENDLESS:
                return new EndlessStrategy(context.getQuestionService(), context.getTeamService(), context.getRoleSkillService(), context.getBuffService());
            case PRE:
                return new PreStrategy(context.getQuestionService(), context.getTeamService(), context.getRoleSkillService(), context.getBuffService());
            case MAIN:
                return new MainStrategy(context.getQuestionService(), context.getTeamService(), context.getRoleSkillService(), context.getBuffService());
            default:
                throw new NotFoundException("MatchStrategyType", type.name());
        }
    }

    
}
