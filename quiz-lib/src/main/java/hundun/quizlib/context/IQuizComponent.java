package hundun.quizlib.context;

import hundun.quizlib.exception.QuizgameException;

/**
 * @author hundun
 * Created on 2022/08/29
 */
public interface IQuizComponent {
    void postConstruct(QuizComponentContext context) throws QuizgameException;
}
