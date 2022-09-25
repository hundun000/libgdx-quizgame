package hundun.gdxgame.quizgame.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.gdxgame.quizgame.core.domain.QuizRootSaveData;
import hundun.gdxgame.share.base.BaseHundunGame;
import hundun.gdxgame.share.base.BaseHundunScreen;
import hundun.gdxgame.share.base.util.JavaFeatureForGwt;
import hundun.gdxgame.share.starter.StarterMenuScreen;

/**
 * @author hundun
 * Created on 2022/09/01
 */
public class QuizMenuScreen extends BaseHundunScreen<QuizGdxGame, QuizRootSaveData> {

    int BUTTON_WIDTH = 100;
    int BUTTON_BIG_HEIGHT = 100;
    int BUTTON_SMALL_HEIGHT = 75;
    
    Actor title;
    Actor buttonContinueGame;
    Actor buttonNewGame;
    Actor buttonHistorySreen;
    Image backImage;

    
    public QuizMenuScreen(QuizGdxGame game
            ) {
        super(game);
        
        Image titleLabel = new Image(
                game.getTextureConfig().getMenuTitle());
        
        Image backImage = new Image(game.getTextureConfig().getMenuScreenBackground());
        
        Button buttonNewGame = new TextButton("新建存档", game.getMainSkin());
        buttonNewGame.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                game.gameLoadOrNew(false);
                game.getScreenManager().pushScreen(PrepareScreen.class.getSimpleName(), "blending_transition");
            }
        });
        
        Button buttonContinueGame = new TextButton("继续存档", game.getMainSkin());
        buttonContinueGame.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                game.gameLoadOrNew(true);
                game.getScreenManager().pushScreen(PrepareScreen.class.getSimpleName(), "blending_transition");
            }
        });
        
        Button buttonIntoSettingScreen = new TextButton("历史记录", game.getMainSkin());
        buttonIntoSettingScreen.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                game.getScreenManager().pushScreen(HistoryScreen.class.getSimpleName(), "blending_transition");
            }
        });
        
        this.title = titleLabel;
        this.backImage = backImage;
        this.buttonContinueGame = buttonContinueGame;
        this.buttonNewGame = buttonNewGame;
        this.buttonHistorySreen = buttonIntoSettingScreen;
    }

    
//    public StarterMenuScreen(T_GAME game, 
//            Actor title,
//            Image backImage,
//            Actor buttonContinueGame,
//            Actor buttonNewGame,
//            Actor buttonIntoSettingScreen
//            ) {
//        super(game);
//        this.backImage = backImage;
//        this.buttonContinueGame = buttonContinueGame;
//        this.buttonNewGame = buttonNewGame;
//        this.buttonIntoSettingScreen = buttonIntoSettingScreen;
//        
//
//    }

    private void initScene2d() {

        backUiStage.clear();
        uiRootTable.clear();
        
        backUiStage.addActor(backImage);

        uiRootTable.add(title)
                .row();
        if (game.gameHasSave()) {
            uiRootTable.add(buttonContinueGame)
                    .height(BUTTON_BIG_HEIGHT)
                    .fillY()
                    .padTop(10)
                    .row();
        }
        uiRootTable.add(buttonNewGame)
                .height(game.gameHasSave() ? BUTTON_SMALL_HEIGHT : BUTTON_BIG_HEIGHT)
                .fillY()
                .padTop(10)
                .row();
        uiRootTable.add(buttonHistorySreen)
                .padTop(10)
                .row();
        
    }

    @Override
    public void show() {
        super.show();
        //addInputProcessor(uiStage);
        Gdx.input.setInputProcessor(uiStage);
        game.getBatch().setProjectionMatrix(uiStage.getViewport().getCamera().combined);
        
        initScene2d();
    }

    @Override
    public void dispose() {
        
    }


    @Override
    protected void create() {

    }

    
    
}
