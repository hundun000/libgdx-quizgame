package hundun.quizlib.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import hundun.quizlib.context.IQuizComponent;
import hundun.quizlib.context.QuizComponentContext;
import hundun.quizlib.exception.QuizgameException;
import hundun.quizlib.model.SessionDataPackage;
import hundun.quizlib.model.domain.QuestionModel;
import hundun.quizlib.model.domain.TeamRuntimeModel;
import hundun.quizlib.prototype.question.ResourceType;
import lombok.extern.slf4j.Slf4j;

public class QuestionService implements IQuizComponent {
    
    SessionService sessionService;
    QuestionLoaderService questionLoaderService;
    
    @Override
    public void postConstruct(QuizComponentContext context) {
        this.sessionService = context.getSessionService();
        this.questionLoaderService = context.getQuestionLoaderService();
    }
    
	private Random hitRandom = new Random(1);
	private Random insertQuestionRandom = new Random(1);
	
	
	private Set<String> tags = new HashSet<>();
	
	Map<String, List<QuestionModel>> questionPackages = new HashMap<>();
	Map<String, QuestionModel> questionPool = new HashMap<>();

	
	public QuestionModel getNewQuestionForTeam(String sessionId, TeamRuntimeModel teamRuntimeModel, boolean removeToDirty) throws QuizgameException {
	    SessionDataPackage sessionDataPackage = sessionService.getSessionDataPackage(sessionId);
  
	    QuestionModel question;
		boolean hitPick = hitRandom.nextDouble() < teamRuntimeModel.getHitPickRate();
		if (hitPick) {
		    question = getFirstPickQuestionIndex(sessionId, teamRuntimeModel);
			teamRuntimeModel.resetHitPickRate();
			//log.debug("FirstPickQuestionIndex");
		} else {
		    question = getFirstNotBanQuestionIndex(sessionId, teamRuntimeModel);
			teamRuntimeModel.increaseHitPickRate();
			//log.debug("FirstNotBanQuestionIndex");
		}
		if (question == null) {
		    question = getFirstQuestionIgnorePickBan(sessionId, teamRuntimeModel);
		}
		//log.info("questionDTO = {}", question.toQuestionDTO());
		
		sessionDataPackage.getQuestionIds().remove(question.getId());
		if (removeToDirty) {
		    sessionDataPackage.getDirtyQuestionIds().add(question);
		} else {
		    int insertIndex = Math.min(sessionDataPackage.getQuestionIds().size(), sessionDataPackage.getQuestionIds().size()/2 + insertQuestionRandom.nextInt(sessionDataPackage.getQuestionIds().size()/2));
		    sessionDataPackage.getQuestionIds().add(insertIndex, question.getId());
		}
		
		return question;
	}
	
	
	private QuestionModel getFirstQuestionIgnorePickBan(String sessionId, TeamRuntimeModel teamRuntimeModel) throws QuizgameException {
	    SessionDataPackage sessionDataPackage = sessionService.getSessionDataPackage(sessionId);
        QuestionModel question = null;
        int i = 0;  
        if (sessionDataPackage.getQuestionIds().isEmpty()) {
            throw new QuizgameException("题库已经空了", -1);
        }
        String questionId = sessionDataPackage.getQuestionIds().get(i);
        question = questionPool.get(questionId); 
           
        return question;
    }


    private QuestionModel getFirstPickQuestionIndex(String sessionId, TeamRuntimeModel teamRuntimeModel) throws QuizgameException {
	    SessionDataPackage sessionDataPackage = sessionService.getSessionDataPackage(sessionId);
		QuestionModel question = null;
		int i = 0;	
		while (question == null && i < sessionDataPackage.getQuestionIds().size()) {
		    String questionId = sessionDataPackage.getQuestionIds().get(i);
            question = questionPool.get(questionId); 
			
			if (!teamRuntimeModel.isPickAndNotBan(question.getTags())) {
				question = null;
				i++;
				continue;
			}
			if (!sessionDataPackage.isAllowImageResource() && question.getResource().getType() == ResourceType.IMAGE) {
                question = null;
                i++;
                continue;
            }
            if (!sessionDataPackage.isAllowVoiceResource() && question.getResource().getType() == ResourceType.VOICE) {
                question = null;
                i++;
                continue;
            }
		}
		return question;
	}
	
	private QuestionModel getFirstNotBanQuestionIndex(String sessionId, TeamRuntimeModel teamRuntimeModel) throws QuizgameException {
	    SessionDataPackage sessionDataPackage = sessionService.getSessionDataPackage(sessionId);
		QuestionModel question = null;
		int i = 0;	
		while (question == null && i < sessionDataPackage.getQuestionIds().size()) {
			String questionId = sessionDataPackage.getQuestionIds().get(i);
			question = questionPool.get(questionId); 
			
			if (!teamRuntimeModel.isNotBan(question.getTags())) {
				question = null;
				i++;
				continue;
			}
			if (!sessionDataPackage.isAllowImageResource() && question.getResource().getType() == ResourceType.IMAGE) {
                question = null;
                i++;
                continue;
            }
			if (!sessionDataPackage.isAllowVoiceResource() && question.getResource().getType() == ResourceType.VOICE) {
                question = null;
                i++;
                continue;
            }
		}
		return question;
	}
	
	

    public List<QuestionModel> getQuestions(String questionPackageName) throws QuizgameException {
        if (!questionPackages.containsKey(questionPackageName)) {
            List<QuestionModel> questions = questionLoaderService.loadAllQuestions(questionPackageName);
            questionPackages.put(questionPackageName, questions);
            questions.forEach(item -> questionPool.put(item.getId(), item));
        }
        return questionPackages.get(questionPackageName);
    }
    
    public Set<String> getTags(String questionPackageName) throws QuizgameException {
        List<QuestionModel> questions = getQuestions(questionPackageName);
        Set<String> tags = new HashSet<>();
        questions.forEach(question -> tags.addAll(question.getTags()));
        return tags;
    }
    
    public void addTag(String tag) {
        tags.add(tag);
    }
    
    public Set<String> getTags() {
        return tags;
    }
    
    public boolean tagExsist(String tag) {
        return tags.contains(tag);
    }

}
