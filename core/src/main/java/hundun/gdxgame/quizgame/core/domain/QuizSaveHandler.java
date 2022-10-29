package hundun.gdxgame.quizgame.core.domain;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;

import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.gdxgame.quizgame.core.domain.QuizRootSaveData.MyGameSaveData;
import hundun.gdxgame.quizgame.core.domain.QuizRootSaveData.SystemSetting;
import hundun.gdxgame.corelib.base.AbstractSaveHandler;

/**
 * @author hundun
 * Created on 2022/09/09
 */
public class QuizSaveHandler extends AbstractSaveHandler<QuizRootSaveData> {

    boolean gameSaveInited = false;
    boolean systemSettingInited = false;
    List<ISubGameSaveHandler> subGameSaveHandlers = new ArrayList<>();
    List<ISubSystemSettingHandler> subSystemSettingHandlers = new ArrayList<>();
    
    public QuizSaveHandler(QuizGdxGame game) {
        
    }

    @Override
    protected void applySystemSetting(QuizRootSaveData rootSaveData) {
        systemSettingInited = true;
        if (rootSaveData.getSystemSetting() != null) {
            subSystemSettingHandlers.forEach(it -> it.applySystemSetting(rootSaveData.getSystemSetting()));
        }
    }
    
    @Override
    protected QuizRootSaveData currentSituationToSaveData() {
        MyGameSaveData myGameSaveData;
        if (gameSaveInited) {
            myGameSaveData = new MyGameSaveData();
            subGameSaveHandlers.forEach(it -> it.currentSituationToSaveData(myGameSaveData));
        } else {
            myGameSaveData = null;
        }
        
        SystemSetting systemSetting;
        if (systemSettingInited) {
            systemSetting = new SystemSetting();
            subSystemSettingHandlers.forEach(it -> it.currentSituationToSystemSetting(systemSetting));
        } else {
            systemSetting = null;
        }
        return new QuizRootSaveData(
                myGameSaveData,
                systemSetting 
                );
    }
    
    @Override
    protected QuizRootSaveData genereateNewGameSaveData() {
        QuizRootSaveData rootSaveData = QuizRootSaveData.Factory.newGame();
        return rootSaveData;
    }

    @Override
    protected void applyGameSaveData(QuizRootSaveData saveData) {
        gameSaveInited = true;
        if (saveData.getGameSaveData() != null) {
            subGameSaveHandlers.forEach(it -> it.applyGameSaveData(saveData.getGameSaveData()));
        }
    }

    public static interface ISubGameSaveHandler {
        void applyGameSaveData(MyGameSaveData myGameSaveData);
        void currentSituationToSaveData(MyGameSaveData myGameSaveData);
    }
    
    public static interface ISubSystemSettingHandler {
        void applySystemSetting(SystemSetting systemSetting);
        void currentSituationToSystemSetting(SystemSetting systemSetting);
    }
    
    @Override
    public void registerSubHandler(Object object) {
        if (object instanceof ISubGameSaveHandler) {
            subGameSaveHandlers.add((ISubGameSaveHandler)object);
            Gdx.app.log(this.getClass().getSimpleName(), object.getClass().getSimpleName() + " register as ISubGameSaveHandler");
        }
        if (object instanceof ISubSystemSettingHandler) {
            subSystemSettingHandlers.add((ISubSystemSettingHandler)object);
            Gdx.app.log(this.getClass().getSimpleName(), object.getClass().getSimpleName() + " register as ISubSystemSettingHandler");
        }
    }
}
