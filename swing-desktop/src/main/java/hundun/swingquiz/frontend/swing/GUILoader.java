package hundun.quizlib.frontend.swing;

import hundun.quizlib.context.QuizComponentContext;
import hundun.quizlib.exception.QuizgameException;
import hundun.quizlib.service.GameService;

/**
 * @author hundun
 * Created on 2019/09/12
 */
public class GUILoader {

    
    static boolean usingSmallFrame = true;
    static int SMALL_WIDTH = 460;
    static int BIG_WIDTH = 850;


    public static void main(String[] args) {
        
        MyFrame frame = new MyFrame();
        QuizComponentContext quizComponentContext;
        try {
            quizComponentContext = QuizComponentContext.Factory.create(frame);
            quizComponentContext.getBuiltinDataConfiguration().registerForGuest();
        } catch (QuizgameException e) {
            e.printStackTrace();
            return;
        }
        
        frame.lazyInit(quizComponentContext);
        
        
        if (usingSmallFrame) {
            frame.setBounds(frame.getX(), frame.getY(), SMALL_WIDTH, frame.getHeight());
        } else {
            frame.setBounds(frame.getX(), frame.getY(), BIG_WIDTH, frame.getHeight());
        }
    }

}
