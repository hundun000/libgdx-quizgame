package hundun.quizlib.view.question;

import java.util.List;
import java.util.Set;

import hundun.quizlib.prototype.question.Resource;
import lombok.Data;

/**
 * @author hundun
 * Created on 2021/05/08
 */
@Data
public class QuestionView {
    String id;
    String stem;
    List<String> options;
    int answer;
    Resource resource;
    Set<String> tags;
    
    
    
}
