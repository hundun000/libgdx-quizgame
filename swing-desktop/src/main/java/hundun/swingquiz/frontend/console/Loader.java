package hundun.quizlib.frontend.console;

import java.util.Arrays;

import hundun.quizlib.context.QuizComponentContext;
import hundun.quizlib.exception.QuizgameException;
import hundun.quizlib.prototype.match.MatchConfig;
import hundun.quizlib.prototype.match.MatchStrategyType;
import hundun.quizlib.service.BuiltinDataConfiguration;
import hundun.quizlib.service.GameService;
import hundun.quizlib.service.QuestionLoaderService;
import hundun.quizlib.view.match.MatchSituationView;

/**
 * @author hundun
 * Created on 2019/09/12
 */
public class Loader {

    
    static boolean usingSmallFrame = true;
    static int SMALL_WIDTH = 460;
    static int BIG_WIDTH = 850;


    public static void main(String[] args) {
        
        ConsoleFrontEnd frontEnd = new ConsoleFrontEnd();
        QuizComponentContext quizComponentContext;
        try {
            quizComponentContext = QuizComponentContext.Factory.create(frontEnd);
            quizComponentContext.getBuiltinDataConfiguration().registerForGuest();
        } catch (QuizgameException e) {
            e.printStackTrace();
            return;
        }
        
        MatchConfig matchConfig = new MatchConfig();
        matchConfig.setTeamNames(Arrays.asList(BuiltinDataConfiguration.DEMO_LIST_TEAM_NAME_0));
        matchConfig.setQuestionPackageName(QuestionLoaderService.PRELEASE_PACKAGE_NAME);
        matchConfig.setMatchStrategyType(MatchStrategyType.ENDLESS);
        
        try {
            MatchSituationView result = quizComponentContext.getGameService().createMatch(matchConfig);
            System.out.println("result = " + result.toString());
        } catch (QuizgameException e) {
            e.printStackTrace();
            return;
        }
    }

}
