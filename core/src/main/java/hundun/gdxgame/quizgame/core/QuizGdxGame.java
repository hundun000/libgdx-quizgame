package hundun.gdxgame.quizgame.core;

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
	    super(480, 320, saveTool);
	    this.debugMode = true;
	    this.DEFAULT_MAIN_SKIN_FILE_PATH = "skins/DefaultSkinWithChineseHeiti26/uiskin.json";
	}
	
	@Override
	public void create() {
	    
	    this.textureConfig = new TextureConfig();
        this.quizLibBridge = new QuizLibBridge();
	    
	    super.create();

		setScreen(modelContext.getScreen(QuizMenuScreen.class));

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
        this.setScreen(quizPlayScreen);
    }

    @Override
    public void intoTeamScreen(boolean load) {
        this.gameLoadOrNew(true);
        this.setScreen(modelContext.getScreen(TeamScreen.class));
    }


    


}
