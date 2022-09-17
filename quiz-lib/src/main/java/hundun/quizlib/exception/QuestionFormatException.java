package hundun.quizlib.exception;

public class QuestionFormatException extends QuizgameException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2199195735884949125L;
	
	public QuestionFormatException(int lineStart, int lineEnd, int index, String expect) {
		super("第"+ (lineStart + 1) +"~"+ (lineEnd + 1) +"行，第"+ index +"题，格式错误，预期得到的是:" + expect, 3);
	}
	

	public QuestionFormatException(QuestionFormatException e, String fileName) {
	    super(fileName + e.getMessage(), e.getRetcode());
	}

}
