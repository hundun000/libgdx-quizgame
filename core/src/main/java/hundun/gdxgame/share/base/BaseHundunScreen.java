package hundun.gdxgame.share.base;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;

import de.eskalon.commons.screen.ManagedScreen;
import lombok.Getter;
import lombok.Setter;



/**
 * @author hundun
 * Created on 2021/11/02
 * @param <T_GAME>
 * @param <T_SAVE>
 */
public abstract class BaseHundunScreen<T_GAME extends BaseHundunGame<T_SAVE>, T_SAVE> extends ManagedScreen {
    @Getter
    protected final T_GAME game;
    
    protected final Stage uiStage;
    protected final Stage popupUiStage;
    protected final Stage backUiStage;


    // ------ lazy init ------
    protected final Table uiRootTable;
    protected final Table popupRootTable;

    protected LogicFrameHelper logicFrameHelper;
    
    public BaseHundunScreen(T_GAME game) {
        this.game = game;
        OrthographicCamera camera = new OrthographicCamera(game.LOGIC_WIDTH, game.LOGIC_HEIGHT);
        FitViewport viewport = new FitViewport(game.LOGIC_WIDTH, game.LOGIC_HEIGHT, camera);
        this.uiStage = new Stage(viewport, game.getBatch());
        this.popupUiStage = new Stage(viewport, game.getBatch());
        this.backUiStage = new Stage(viewport, game.getBatch());
        
        uiRootTable = new Table();
        uiRootTable.setFillParent(true);
        uiStage.addActor(uiRootTable);
        
        popupRootTable = new Table();
        popupRootTable.setFillParent(true);
        popupUiStage.addActor(popupRootTable);
    }
    
    protected void onLogicFrame() {
        // base-class do nothing
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (logicFrameHelper != null) {
            boolean isLogicFrame = logicFrameHelper.logicFrameCheck(delta);
            if (isLogicFrame) {
                onLogicFrame();
            }
        }
        
        uiStage.act();
        
        // ====== be careful of draw order ======
        backUiStage.draw();
        uiStage.draw();
        popupUiStage.draw();
        renderPopupAnimations(delta, game.getBatch());
    }
    
    protected void renderPopupAnimations(float delta, SpriteBatch spriteBatch) {
        // base-class do nothing
    }
    
    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void resize(int width, int height) {
        this.uiStage.getViewport().update(width, height, true);
    }
}
