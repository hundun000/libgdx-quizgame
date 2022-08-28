package hundun.quizlib.model;

import java.util.List;
import java.util.Set;

import hundun.quizlib.model.domain.QuestionModel;
import hundun.quizlib.model.domain.match.BaseMatch;
import lombok.Data;

/**
 * @author hundun
 * Created on 2021/05/10
 */
@Data
public class SessionDataPackage {
    String sessionId;
    List<String> questionIds;
    List<QuestionModel> dirtyQuestionIds;
    Set<String> tags;
    boolean allowImageResource = true;
    boolean allowVoiceResource = false;
    BaseMatch match;
}
