package hundun.gdxgame.share.base;

import java.util.HashMap;
import java.util.Map;

import hundun.gdxgame.share.base.util.JavaFeatureForGwt;

/**
 * @author hundun
 * Created on 2022/08/30
 * @param <T>
 */
public abstract class BaseViewModelContext {

    protected abstract void lazyInitOnGameCreate();
    protected abstract void disposeAll();

}
