package hundun.quizlib.prototype.match;
/**
 * @author hundun
 * Created on 2019/10/08
 */
public enum AnswerType {
    CORRECT,
    WRONG,
    TIMEOUOT_WRONG,
    SKIPPED;
    
    public static AnswerType fromBoolean(boolean isCorrect, boolean isSkip, boolean isTimeout) {
        if (isTimeout) {
            return TIMEOUOT_WRONG;
        } else if (isSkip) {
            return SKIPPED;
        } else {
            return isCorrect ? CORRECT : WRONG;
        }
    }
}
