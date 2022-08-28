package hundun.quizlib.frontend.swing;

import java.util.TimerTask;

/**
 * @author hundun
 * Created on 2019/09/12
 */
public class SecondTimerTask extends TimerTask {

    ISecondEventReceiver receiver;
    
    public SecondTimerTask(ISecondEventReceiver receiver) {
        this.receiver = receiver;
    }
    
    @Override
    public void run() {
        receiver.whenReceiveSecondClock();
    }

}
