package com.mygdx.game.core.util.save;

import com.mygdx.game.core.data.RootSaveData;

/**
 * @author hundun
 * Created on 2022/04/08
 */
public interface ISaveTool {
    void lazyInitOnGameCreate();
    boolean hasSave();
    void saveRootSaveData(RootSaveData saveData);
    RootSaveData loadRootSaveData();
}