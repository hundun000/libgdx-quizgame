package com.mygdx.game.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.core.data.RootSaveData;
import com.mygdx.game.core.util.save.ISaveTool;

public class MyGdxGame extends Game {
    private SpriteBatch batch;
	private Texture img;
	private BitmapFont font;
	
	private RootSaveData rootSaveData;
	private final ISaveTool saveTool;
	
	public MyGdxGame(ISaveTool saveTool) {
	    this.saveTool = saveTool;
	}
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		img = new Texture("badlogic.jpg");
		
		saveTool.lazyInitOnGameCreate();
		
		// test save and load
		rootSaveData = RootSaveData.Factory.newGame();
		rootSaveData.getData().setValue("new Hello world");
		saveTool.saveRootSaveData(rootSaveData);
		
		rootSaveData = saveTool.loadRootSaveData();

	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		//batch.draw(img, 0, 0);
		
		font.draw(batch, "SaveData: " + rootSaveData.getData().getValue(), 50, 50);
		
		batch.end();
	}
	
	@Override
	public void pause() {
	    saveTool.saveRootSaveData(rootSaveData);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
