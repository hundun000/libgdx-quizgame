package hundun.gdxgame.quizgame.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import hundun.gdxgame.base.BaseHundunGame;
import hundun.gdxgame.base.util.save.ISaveTool;
import hundun.gdxgame.quizgame.core.data.RootSaveData;

public class MyGdxGame extends BaseHundunGame<RootSaveData> {

	
	private RootSaveData rootSaveData;
	
	public MyGdxGame(ISaveTool<RootSaveData> saveTool) {
	    super(480, 320, saveTool);
	}
	
	@Override
	public void create () {
		super.create();
		
		// test save and load

	}
	
	@Override
	public void pause() {
	    saveCurrent();
	}


    @Override
    protected RootSaveData getNewGameSaveData() {
        rootSaveData = RootSaveData.Factory.newGame();
        rootSaveData.getData().setValue("new Hello world");
        return rootSaveData;
    }

    @Override
    protected RootSaveData currentSituationToSaveData() {
        return rootSaveData;
    }
}
