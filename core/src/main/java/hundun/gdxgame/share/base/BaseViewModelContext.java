package hundun.gdxgame.share.base;

import java.util.HashMap;
import java.util.Map;

import hundun.gdxgame.share.base.util.JavaFeatureForGwt;

/**
 * @author hundun
 * Created on 2022/08/30
 * @param <T>
 */
public abstract class BaseViewModelContext<T_SAVE> {

    protected Map<Class<?>, BaseHundunScreen<?, ?>> screenMap = new HashMap<>();
    
    protected abstract void applySaveData(T_SAVE saveData);
    protected abstract T_SAVE currentSituationToSaveData();
    protected abstract T_SAVE genereateNewGameSaveData();
    
    protected abstract void lazyInitOnGameCreate();
    protected abstract void disposeAll();
    
    @SuppressWarnings("unchecked")
    public <T extends BaseHundunScreen<?, ?>> T getScreen(Class<T> clazz) {
        return (T) JavaFeatureForGwt.requireNonNull(screenMap.get(clazz));
    }

}
