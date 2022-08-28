package com.mygdx.game.core.util.save;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;


/**
 * @author hundun
 * Created on 2022/08/04
 */
public abstract class AbstractSaveDataSaveTool implements ISaveTool {
    
    protected String preferencesName;
    protected Preferences preferences;
    protected static final String ROOT_KEY = "root";
    
    public AbstractSaveDataSaveTool(String preferencesName) {
        this.preferencesName = preferencesName;
    }
    
    @Override
    public boolean hasSave() {
        return preferences != null && preferences.contains(ROOT_KEY);
    }
    
    @Override
    public void lazyInitOnGameCreate() {
        this.preferences = Gdx.app.getPreferences(preferencesName);
    }
    
    
    
    
}
