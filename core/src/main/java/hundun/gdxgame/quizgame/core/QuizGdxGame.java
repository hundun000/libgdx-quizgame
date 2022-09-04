package hundun.gdxgame.quizgame.core;

import com.badlogic.gdx.Gdx;

import hundun.gdxgame.quizgame.core.config.TextureConfig;
import hundun.gdxgame.quizgame.core.domain.QuizLibBridge;
import hundun.gdxgame.quizgame.core.domain.QuizRootSaveData;
import hundun.gdxgame.quizgame.core.domain.QuizSaveHandler;
import hundun.gdxgame.quizgame.core.domain.QuizViewModelContext;
import hundun.gdxgame.quizgame.core.screen.HistoryScreen;
import hundun.gdxgame.quizgame.core.screen.IScreenSwitchHandler;
import hundun.gdxgame.quizgame.core.screen.QuizMenuScreen;
import hundun.gdxgame.quizgame.core.screen.QuizPlayScreen;
import hundun.gdxgame.quizgame.core.screen.TeamScreen;
import hundun.gdxgame.quizgame.core.screen.HistoryScreen.MatchFinishHistory;
import hundun.gdxgame.share.base.BaseHundunGame;
import hundun.gdxgame.share.base.BaseViewModelContext;
import hundun.gdxgame.share.base.util.JavaFeatureForGwt;
import hundun.gdxgame.share.base.util.save.ISaveTool;
import hundun.gdxgame.share.starter.StarterMenuScreen;
import hundun.quizlib.prototype.event.MatchFinishEvent;
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
	}
	
	@Override
	public void pause() {
	    super.pause();
	    gameSaveCurrent();
	}


    @Override
    public void intoQuizPlayScreen(MatchConfig matchConfig) {
        QuizPlayScreen quizPlayScreen = this.getScreen(QuizPlayScreen.class);
        quizPlayScreen.prepareShow(matchConfig);
        this.screenManager.pushScreen(QuizPlayScreen.class.getSimpleName(), "blending_transition");
    }

    @Override
    public void intoTeamScreen() {
        this.screenManager.pushScreen(TeamScreen.class.getSimpleName(), "blending_transition");
    }

    @Override
    public void intoHistoryScreen(MatchFinishHistory history) {
        HistoryScreen historyScreen = this.getScreen(HistoryScreen.class);
        historyScreen.prepareShow(history);
        this.screenManager.pushScreen(HistoryScreen.class.getSimpleName(), "blending_transition");
    }
    
    @Override
    protected void createStage1() {
        // ------ for super ------
        this.modelContext = new QuizViewModelContext(this);
        this.saveHandler = new QuizSaveHandler(this);
        this.mainSkinFilePath = "skins/DefaultSkinWithChineseHeiti26/uiskin.json";
        // ------ for self ------
        this.textureConfig = new TextureConfig();
        this.quizLibBridge = new QuizLibBridge();
    }

    @Override
    protected void createStage3() {
        systemSettingLoad();
        
        this.screenManager.pushScreen(QuizMenuScreen.class.getSimpleName(), "blending_transition");
        Gdx.app.log(this.getClass().getSimpleName(), "Initialization finished.");
    }

    
    


}
