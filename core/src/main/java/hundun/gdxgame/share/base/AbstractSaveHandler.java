package hundun.gdxgame.share.base;
/**
 * @author hundun
 * Created on 2022/09/09
 */
public abstract class AbstractSaveHandler<T_SAVE> {
    protected abstract void applySystemSetting(T_SAVE saveData);
    protected abstract void applyGameSaveData(T_SAVE saveData);
    protected abstract T_SAVE currentSituationToSaveData();
    protected abstract T_SAVE genereateNewGameSaveData();
    public abstract void registerSubHandler(Object object);
}
