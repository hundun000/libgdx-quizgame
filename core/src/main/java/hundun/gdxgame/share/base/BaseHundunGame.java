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

import hundun.gdxgame.share.base.util.save.ISaveTool;
import lombok.Getter;


public abstract class BaseHundunGame<T_SAVE> extends Game {
    public boolean debugMode;
    public final int LOGIC_WIDTH;
    public final int LOGIC_HEIGHT;
    protected String DEFAULT_MAIN_SKIN_FILE_PATH = "skins/default/skin/uiskin.json";
    protected String mainSkinFilePath;

    @Getter
    private SpriteBatch batch;

    @Getter
    protected BaseViewModelContext<T_SAVE> modelContext;

    @Getter
    private Skin mainSkin;

    private ISaveTool<T_SAVE> saveTool;


    public BaseHundunGame(int LOGIC_WIDTH, int LOGIC_HEIGHT, 
            ISaveTool<T_SAVE> saveTool
            ) {
        this.LOGIC_WIDTH = LOGIC_WIDTH;
        this.LOGIC_HEIGHT = LOGIC_HEIGHT;
        this.saveTool = saveTool;
    }
    
    protected abstract BaseViewModelContext<T_SAVE> beforeCreateLazyInit();

	@Override
	public void create() {
	    
	    this.modelContext = beforeCreateLazyInit();
	    
	    this.batch = new SpriteBatch();
        if (mainSkinFilePath != null) {
            this.mainSkin = new Skin(Gdx.files.internal(mainSkinFilePath));
        } else {
            this.mainSkin = new Skin(Gdx.files.internal(DEFAULT_MAIN_SKIN_FILE_PATH));
        }
        
        this.saveTool.lazyInitOnGameCreate();
        this.modelContext.lazyInitOnGameCreate();
	}
	
	// ====== save & load ======
	
	
	
	public void gameLoadOrNew(boolean load) {

	    T_SAVE saveData;
	    if (load) {
	        saveData = saveTool.readRootSaveData();
	    } else {
	        saveData = modelContext.genereateNewGameSaveData();
	    }

	    modelContext.applySaveData(saveData);
	    Gdx.app.log(this.getClass().getSimpleName(), load ? "load game done" : "new game done");
	}
    public void gameSaveCurrent() {
        Gdx.app.log(this.getClass().getSimpleName(), "saveCurrent called");
        saveTool.writeRootSaveData(modelContext.currentSituationToSaveData());
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



    
}
