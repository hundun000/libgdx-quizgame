package hundun.gdxgame.quizgame.html;


import com.badlogic.gdx.Gdx;
import com.github.nmorel.gwtjackson.client.ObjectMapper;
import com.google.gwt.core.client.GWT;

import hundun.gdxgame.quizgame.core.domain.QuizRootSaveData;
import hundun.gdxgame.corelib.base.util.save.AbstractSaveDataSaveTool;


/**
 * @author hundun
 * Created on 2021/11/10
 */
public class GwtPreferencesSaveTool extends AbstractSaveDataSaveTool<QuizRootSaveData> {

    
    private SaveDataMapper objectMapper;
    
    public static interface SaveDataMapper extends ObjectMapper<QuizRootSaveData> {}
    
    
    public GwtPreferencesSaveTool(String preferencesName) {
        super(preferencesName);
        this.objectMapper = GWT.create(SaveDataMapper.class);
    }


    @Override
    public void writeRootSaveData(QuizRootSaveData saveData) {
        try {
            preferences.putString(ROOT_KEY, objectMapper.write(saveData));
            preferences.flush();
            Gdx.app.log(getClass().getSimpleName(), "save() done");
        } catch (Exception e) {
            Gdx.app.error(getClass().getSimpleName(), "save() error", e);
        }
    }



    @Override
    public QuizRootSaveData readRootSaveData() {

        try {
            String date = preferences.getString(ROOT_KEY);
            QuizRootSaveData saveData = objectMapper.read(date);
            return saveData;
        } catch (Exception e) {
            Gdx.app.error(getClass().getSimpleName(), "load() error", e);
            return null;
        }
        
    }
}
