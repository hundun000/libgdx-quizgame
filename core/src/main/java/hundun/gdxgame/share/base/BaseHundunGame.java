package hundun.gdxgame.share.base;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.eskalon.commons.core.ManagedGame;
import de.eskalon.commons.screen.ManagedScreen;
import de.eskalon.commons.screen.transition.ScreenTransition;
import hundun.gdxgame.share.base.util.JavaFeatureForGwt;
import hundun.gdxgame.share.base.util.save.ISaveTool;
import lombok.Getter;


public abstract class BaseHundunGame<T_SAVE> extends ManagedGame<ManagedScreen, ScreenTransition> {
    public boolean debugMode;
    public final int LOGIC_WIDTH;
    public final int LOGIC_HEIGHT;
    
    private static final String DEFAULT_MAIN_SKIN_FILE_PATH = "skins/default/skin/uiskin.json";
    

    @Getter
    private SpriteBatch batch;

    
    
    @Getter
    private Skin mainSkin;

    private ISaveTool<T_SAVE> saveTool;


    // ------ init in createStage1(), or keep null ------
    @Getter
    protected BaseViewModelContext modelContext;
    protected AbstractSaveHandler<T_SAVE> saveHandler;
    protected String mainSkinFilePath;
    
    
    public BaseHundunGame(int LOGIC_WIDTH, int LOGIC_HEIGHT, 
            ISaveTool<T_SAVE> saveTool
            ) {
        this.LOGIC_WIDTH = LOGIC_WIDTH;
        this.LOGIC_HEIGHT = LOGIC_HEIGHT;
        this.saveTool = saveTool;
    }
    
    protected abstract void createStage1();
    protected abstract void createStage3();
    
	@Override
	public void create() {
	    super.create();
	    
	    createStage1();
	    
	    this.batch = new SpriteBatch();
        if (mainSkinFilePath != null) {
            this.mainSkin = new Skin(Gdx.files.internal(mainSkinFilePath));
        } else {
            this.mainSkin = new Skin(Gdx.files.internal(DEFAULT_MAIN_SKIN_FILE_PATH));
        }
        
        this.saveTool.lazyInitOnGameCreate();
        this.modelContext.lazyInitOnGameCreate();
        
        createStage3();
	}
	
	// ====== save & load ======
	public void systemSettingLoad() {

        T_SAVE saveData = saveTool.readRootSaveData();

        saveHandler.applySystemSetting(saveData);
        Gdx.app.log(this.getClass().getSimpleName(), "systemSettingLoad call");
    }
	
	
	public void gameLoadOrNew(boolean load) {

	    T_SAVE saveData;
	    if (load) {
	        saveData = saveTool.readRootSaveData();
	    } else {
	        saveData = saveHandler.genereateNewGameSaveData();
	    }

	    saveHandler.applyGameSaveData(saveData);
	    Gdx.app.log(this.getClass().getSimpleName(), load ? "load game done" : "new game done");
	}
    public void gameSaveCurrent() {
        Gdx.app.log(this.getClass().getSimpleName(), "saveCurrent called");
        saveTool.writeRootSaveData(saveHandler.currentSituationToSaveData());
    }
    public boolean gameHasSave() {
        return saveTool.hasSave();
    }
    // ====== ====== ======



	@Override
	public void dispose () {
		batch.dispose();
		modelContext.disposeAll();
	}
	
	
	@SuppressWarnings("unchecked")
    protected <T extends BaseHundunScreen<?, ?>> T getScreen(Class<T> clazz) {
        return (T) JavaFeatureForGwt.requireNonNull(getScreenManager().getScreen(clazz.getSimpleName()));
    }


    
}
