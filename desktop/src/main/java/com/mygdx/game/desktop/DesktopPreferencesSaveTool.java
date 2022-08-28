package com.mygdx.game.desktop;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mygdx.game.core.data.RootSaveData;
import com.mygdx.game.core.util.save.AbstractSaveDataSaveTool;



/**
 * @author hundun
 * Created on 2021/11/10
 */
public class DesktopPreferencesSaveTool extends AbstractSaveDataSaveTool {
    
    private ObjectMapper objectMapper;
    
    public DesktopPreferencesSaveTool(String preferencesName) {
        super(preferencesName);
        this.objectMapper = new ObjectMapper()
                .enable(SerializationFeature.INDENT_OUTPUT)
                ;
        
    }



    @Override
    public void lazyInitOnGameCreate() {
        this.preferences = Gdx.app.getPreferences(preferencesName);
    }



    @Override
    public void saveRootSaveData(RootSaveData saveData) {
        try {
            preferences.putString(ROOT_KEY, objectMapper.writeValueAsString(saveData));
            preferences.flush();
            Gdx.app.log(getClass().getSimpleName(), "save() done");
        } catch (Exception e) {
            Gdx.app.error(getClass().getSimpleName(), "save() error", e);
        }
    }



    @Override
    public RootSaveData loadRootSaveData() {
        try {
            String date = preferences.getString(ROOT_KEY);
            RootSaveData saveData = objectMapper.readValue(date, RootSaveData.class);
            return saveData;
        } catch (IOException e) {
            Gdx.app.error(getClass().getSimpleName(), "load() error", e);
            return null;
        }
    }
}
