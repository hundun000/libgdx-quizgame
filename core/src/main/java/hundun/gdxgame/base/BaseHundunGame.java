package hundun.gdxgame.base;

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

import hundun.gdxgame.base.util.save.ISaveTool;
import lombok.Getter;


public abstract class BaseHundunGame<T_SAVE> extends Game {

    public final int LOGIC_WIDTH;
    public final int LOGIC_HEIGHT;
    protected String DEFAULT_MAIN_SKIN_FILE_PATH = "skins/default/skin/uiskin.json";
    protected String mainSkinFilePath;

    @Getter
    private SpriteBatch batch;

    @Getter
    private ViewModelContext<T_SAVE> modelContext;

    @Getter
    private Skin mainSkin;

    private ISaveTool<T_SAVE> saveTool;


    public BaseHundunGame(int LOGIC_WIDTH, int LOGIC_HEIGHT, ISaveTool<T_SAVE> saveTool) {
        this.LOGIC_WIDTH = LOGIC_WIDTH;
        this.LOGIC_HEIGHT = LOGIC_HEIGHT;
        this.saveTool = saveTool;
    }

	@Override
	public void create() {
	    
	    this.batch = new SpriteBatch();
	    this.saveTool.lazyInitOnGameCreate();
		if (mainSkinFilePath != null) {
            this.mainSkin = new Skin(Gdx.files.internal(mainSkinFilePath));
        } else {
            this.mainSkin = new Skin(Gdx.files.internal(DEFAULT_MAIN_SKIN_FILE_PATH));
        }
		

		this.modelContext.initContexts();
		this.modelContext.contextsLazyInit();
	}
	
	// ====== save & load ======
	protected abstract T_SAVE getNewGameSaveData();
	protected abstract T_SAVE currentSituationToSaveData();
	
	public void loadOrNewGame(boolean load) {

	    T_SAVE saveData;
	    if (load) {
	        saveData = saveTool.loadRootSaveData();
	    } else {
	        saveData = getNewGameSaveData();
	    }

	    modelContext.applySaveData(saveData);
	    Gdx.app.log(this.getClass().getSimpleName(), load ? "load game done" : "new game done");
	}
    public void saveCurrent() {
        Gdx.app.log(this.getClass().getSimpleName(), "saveCurrent called");
        saveTool.saveRootSaveData(this.currentSituationToSaveData());
    }
    // ====== ====== ======



	@Override
	public void dispose () {
		batch.dispose();
		modelContext.disposeAll();
	}



    
}
