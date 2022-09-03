package hundun.gdxgame.share.base;

import lombok.Getter;
import lombok.Setter;

/**
 * @author hundun
 * Created on 2022/08/31
 */
public class LogicFrameHelper {
    
    private final int LOGIC_FRAME_PER_SECOND;
    private final float LOGIC_FRAME_LENGTH;
    @Getter
    private int clockCount = 0;
    private float logicFramAccumulator;

    @Getter
    @Setter
    private boolean logicFramePause;
    
    public LogicFrameHelper(int LOGIC_FRAME_PER_SECOND) {
        this.LOGIC_FRAME_PER_SECOND = LOGIC_FRAME_PER_SECOND;
        this.LOGIC_FRAME_LENGTH = 1f / LOGIC_FRAME_PER_SECOND;
    }
    
    public boolean logicFrameCheck(float delta) {
        logicFramAccumulator += delta;
        if (logicFramAccumulator >= LOGIC_FRAME_LENGTH) {
            logicFramAccumulator -= LOGIC_FRAME_LENGTH;
            if (!logicFramePause) {
                clockCount++;
                return true;
            }
        }
        return false;
    }
    
    public double frameNumToSecond(int frameNum) {
        return frameNum * LOGIC_FRAME_LENGTH;
    }

    public int secondToFrameNum(double second) {
        return (int) (LOGIC_FRAME_PER_SECOND * second);
    }
}
