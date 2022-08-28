package hundun.gdxgame.base;
/**
 * @author hundun
 * Created on 2022/08/30
 * @param <T>
 */
public abstract class ViewModelContext<T_SAVE> {

    
    abstract void applySaveData(T_SAVE saveData);

    protected abstract void initContexts();

    protected abstract void contextsLazyInit();

    protected abstract void disposeAll();
}
