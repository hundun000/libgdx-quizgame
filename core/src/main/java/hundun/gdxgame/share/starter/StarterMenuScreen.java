package hundun.gdxgame.share.starter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import hundun.gdxgame.share.base.BaseHundunGame;
import hundun.gdxgame.share.base.BaseHundunScreen;
import hundun.gdxgame.share.base.util.JavaFeatureForGwt;

import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;




public class StarterMenuScreen<T_GAME extends BaseHundunGame<T_SAVE>, T_SAVE> 
extends BaseHundunScreen<T_GAME, T_SAVE> {

    int BUTTON_WIDTH = 100;
    int BUTTON_BIG_HEIGHT = 100;
    int BUTTON_SMALL_HEIGHT = 75;
    
    Actor title;
    Actor buttonContinueGame;
    Actor buttonNewGame;
    Actor buttonIntoSettingScreen;
    Image backImage;
    Table table;
    
    public static class Factory {
        public static <T_GAME extends BaseHundunGame<T_SAVE>, T_SAVE> 
        StarterMenuScreen<T_GAME, T_SAVE> 
        simpleBuild(
                T_GAME game, 
                String titleText,
                Texture backTexture,
                InputListener buttonContinueGameInputListener, 
                InputListener buttonNewGameInputListener
                ) {
            Label titleLabel = new Label(
                    JavaFeatureForGwt.stringFormat("     %s     ", titleText), 
                    game.getMainSkin());
            titleLabel.setFontScale(1.5f);
            
            Image backImage = new Image(backTexture);
            
            Button buttonNewGame = new TextButton("New game", game.getMainSkin());
            buttonNewGame.addListener(buttonNewGameInputListener);
            
            Button buttonContinueGame = new TextButton("Continue game", game.getMainSkin());
            buttonContinueGame.addListener(buttonContinueGameInputListener);
            
            Button buttonIntoSettingScreen = new TextButton("Setting", game.getMainSkin());
            
            return new StarterMenuScreen<T_GAME, T_SAVE>(
                    game, 
                    titleLabel, 
                    backImage, 
                    buttonContinueGame, 
                    buttonNewGame, 
                    buttonIntoSettingScreen
                    );
        }
    }
    
    
    public StarterMenuScreen(T_GAME game, 
            Actor title,
            Image backImage,
            Actor buttonContinueGame,
            Actor buttonNewGame,
            Actor buttonIntoSettingScreen
            ) {
        super(game);
        this.backImage = backImage;
        this.buttonContinueGame = buttonContinueGame;
        this.buttonNewGame = buttonNewGame;
        this.buttonIntoSettingScreen = buttonIntoSettingScreen;
        initScene2d();

    }

    private void initScene2d() {

        uiStage.addActor(backImage);

        table = new Table();
        table.setFillParent(true);
        //table.setBounds((game.LOGIC_WIDTH - BUTTON_WIDTH)/2, 0, BUTTON_WIDTH, game.LOGIC_HEIGHT / 2);
        uiStage.addActor(table);


        table.add(title)
                .row();
        

        if (game.gameHasSave()) {
            table.add(buttonContinueGame)
                .height(BUTTON_BIG_HEIGHT)
                .fillY()
                .padTop(10)
                .row();
        }

        table.add(buttonNewGame)
            .height(game.gameHasSave() ? BUTTON_SMALL_HEIGHT : BUTTON_BIG_HEIGHT)
            .fillY()
            .padTop(10)
            .row();

        if (game.debugMode) {
            table.debugAll();
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(uiStage);

        game.getBatch().setProjectionMatrix(uiStage.getViewport().getCamera().combined);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);





        uiStage.act();
        uiStage.draw();
    }



    @Override
    public void dispose() {}
}