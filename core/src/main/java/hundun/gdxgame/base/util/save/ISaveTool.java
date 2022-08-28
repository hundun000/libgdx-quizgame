package hundun.gdxgame.base.util.save;

import hundun.gdxgame.quizgame.core.data.RootSaveData;

/**
 * @author hundun
 * Created on 2022/04/08
 */
public interface ISaveTool<T_SAVE> {
    void lazyInitOnGameCreate();
    boolean hasSave();
    void saveRootSaveData(T_SAVE saveData);
    T_SAVE loadRootSaveData();
}