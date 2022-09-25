package hundun.gdxgame.share.base.util.save;

/**
 * @author hundun
 * Created on 2022/04/08
 */
public interface ISaveTool<T_SAVE> {
    void lazyInitOnGameCreate();
    boolean hasSave();
    void writeRootSaveData(T_SAVE saveData);
    T_SAVE readRootSaveData();
}