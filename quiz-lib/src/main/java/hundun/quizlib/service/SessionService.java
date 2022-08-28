package hundun.quizlib.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import hundun.quizlib.context.IQuizComponent;
import hundun.quizlib.context.QuizComponentContext;
import hundun.quizlib.exception.NotFoundException;
import hundun.quizlib.exception.QuizgameException;
import hundun.quizlib.model.SessionDataPackage;
import hundun.quizlib.model.domain.QuestionModel;

/**
 * @author hundun
 * Created on 2021/05/10
 */
public class SessionService implements IQuizComponent {
    
    private static int currentId;
    
    QuestionService questionService;
    @Override
    public void postConstruct(QuizComponentContext context) {
        this.questionService = context.getQuestionService();
    }
    
    Map<String, SessionDataPackage> dataPackages = new HashMap<>();
    
    private Random shuffleRandom = new Random(System.currentTimeMillis());
    
    public SessionDataPackage createSession(String questionPackageName) throws QuizgameException {
        
        SessionDataPackage sessionDataPackage = new SessionDataPackage();
        

        List<QuestionModel> questions = questionService.getQuestions(questionPackageName);
        List<String> questionIds = new ArrayList<>(questions.size());
        Set<String> tags = new HashSet<>();
        for (QuestionModel question : questions) {
            questionIds.add(question.getId());
            tags.addAll(question.getTags());
        }
        Collections.shuffle(questionIds, shuffleRandom);

        sessionDataPackage.setSessionId(String.valueOf(currentId++));
        sessionDataPackage.setQuestionIds(questionIds);
        sessionDataPackage.setDirtyQuestionIds(new LinkedList<>());
        sessionDataPackage.setTags(tags);
        
        dataPackages.put(sessionDataPackage.getSessionId(), sessionDataPackage);
        
        return sessionDataPackage;
    }
    
    public SessionDataPackage getSessionDataPackage(String sessionId) throws QuizgameException {
        SessionDataPackage sessionDataPackage = dataPackages.get(sessionId);
        if (sessionDataPackage == null) {
            throw new NotFoundException("sessionDataPackage by sessionId", sessionId);
        }
        return sessionDataPackage;
    }

    
    
}
