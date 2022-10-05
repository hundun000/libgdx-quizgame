package hundun.swingquiz.frontend.swing;

import hundun.quizlib.context.QuizComponentContext;
import hundun.quizlib.exception.QuizgameException;
import hundun.quizlib.service.GameService;
import hundun.swingquiz.frontend.SimpleFrontEnd;

/**
 * @author hundun
 * Created on 2019/09/12
 */
public class GUILoader {

    
    static int SMALL_WIDTH = 460;
    static int BIG_WIDTH = 850;


    public static void main(String[] args) {
        
        
        SimpleFrontEnd simpleFrontEnd = new SimpleFrontEnd();
        QuizComponentContext quizComponentContext;
        try {
            quizComponentContext = QuizComponentContext.Factory.create(simpleFrontEnd);
            quizComponentContext.getBuiltinDataConfiguration().registerForGuest();
        } catch (QuizgameException e) {
            e.printStackTrace();
            return;
        }
        
        MyFrame frame = new MyFrame();
        frame.lazyInit(quizComponentContext, simpleFrontEnd);
        
        
        frame.setBounds(frame.getX(), frame.getY(), SMALL_WIDTH, frame.getHeight());

    }

}
