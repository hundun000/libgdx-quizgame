package hundun.gdxgame.quizgame.desktop;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import hundun.gdxgame.quizgame.core.domain.QuizRootSaveData;
import hundun.gdxgame.corelib.base.util.save.AbstractSaveDataSaveTool;



/**
 * @author hundun
 * Created on 2021/11/10
 */
public class DesktopPreferencesSaveTool extends AbstractSaveDataSaveTool<QuizRootSaveData> {
    
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
    public void writeRootSaveData(QuizRootSaveData saveData) {
        try {
            preferences.putString(ROOT_KEY, objectMapper.writeValueAsString(saveData));
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
            QuizRootSaveData saveData = objectMapper.readValue(date, QuizRootSaveData.class);
            return saveData;
        } catch (IOException e) {
            Gdx.app.error(getClass().getSimpleName(), "load() error");
            throw new RuntimeException(e);
        }
    }
}
