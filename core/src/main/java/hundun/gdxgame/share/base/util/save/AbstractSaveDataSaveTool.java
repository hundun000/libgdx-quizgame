package hundun.gdxgame.share.base.util.save;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import hundun.gdxgame.quizgame.core.domain.QuizRootSaveData;


/**
 * @author hundun
 * Created on 2022/08/04
 */
public abstract class AbstractSaveDataSaveTool implements ISaveTool<QuizRootSaveData> {
    
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
