package hundun.gdxgame.quizgame.core;

import com.badlogic.gdx.Gdx;

import hundun.gdxgame.quizgame.core.config.TextureConfig;
import hundun.gdxgame.quizgame.core.domain.QuizLibBridge;
import hundun.gdxgame.quizgame.core.domain.QuizRootSaveData;
import hundun.gdxgame.quizgame.core.domain.QuizViewModelContext;
import hundun.gdxgame.quizgame.core.screen.IScreenSwitchHandler;
import hundun.gdxgame.quizgame.core.screen.QuizMenuScreen;
import hundun.gdxgame.quizgame.core.screen.QuizPlayScreen;
import hundun.gdxgame.quizgame.core.screen.TeamScreen;
import hundun.gdxgame.share.base.BaseHundunGame;
import hundun.gdxgame.share.base.BaseViewModelContext;
import hundun.gdxgame.share.base.util.save.ISaveTool;
import hundun.gdxgame.share.starter.StarterMenuScreen;
import hundun.quizlib.prototype.match.MatchConfig;
import lombok.Getter;

public class QuizGdxGame extends BaseHundunGame<QuizRootSaveData> implements IScreenSwitchHandler {

    public static final int LOGIC_FRAME_PER_SECOND = 20;
    
	@Getter
	TextureConfig textureConfig;
	@Getter
	QuizLibBridge quizLibBridge;
	
	
	public QuizGdxGame(ISaveTool<QuizRootSaveData> saveTool) {
	    super(1600, 900, saveTool);
	    this.debugMode = true;
	    this.DEFAULT_MAIN_SKIN_FILE_PATH = "skins/DefaultSkinWithChineseHeiti26/uiskin.json";
	}
	
	@Override
	public void create() {
	    
	    this.textureConfig = new TextureConfig();
        this.quizLibBridge = new QuizLibBridge();
	    
	    super.create();

		this.screenManager.pushScreen(QuizMenuScreen.class.getSimpleName(), "blending_transition");
		Gdx.app.log(this.getClass().getSimpleName(), "Initialization finished.");
	}
	
	@Override
	public void pause() {
	    gameSaveCurrent();
	}

    @Override
    protected BaseViewModelContext<QuizRootSaveData> beforeCreateLazyInit() {
        return new QuizViewModelContext(this);
    }

    @Override
    public void intoQuizPlayScreen(MatchConfig matchConfig) {
        QuizPlayScreen quizPlayScreen = modelContext.getScreen(QuizPlayScreen.class);
        quizPlayScreen.prepareShow(matchConfig);
        this.screenManager.pushScreen(QuizPlayScreen.class.getSimpleName(), "blending_transition");
    }

    @Override
    public void intoTeamScreen(boolean load) {
        this.gameLoadOrNew(true);
        this.screenManager.pushScreen(TeamScreen.class.getSimpleName(), "blending_transition");
    }


    


}
