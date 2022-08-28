package hundun.gdxgame.share.base;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import lombok.Getter;



/**
 * @author hundun
 * Created on 2021/11/02
 * @param <T_GAME>
 * @param <T_SAVE>
 */
public abstract class BaseHundunScreen<T_GAME extends BaseHundunGame<T_SAVE>, T_SAVE> implements Screen {
    @Getter
    protected final T_GAME game;
    protected final Stage uiStage;


    public BaseHundunScreen(T_GAME game) {
        this.game = game;
        OrthographicCamera camera = new OrthographicCamera(game.LOGIC_WIDTH, game.LOGIC_HEIGHT);
        FitViewport viewport = new FitViewport(game.LOGIC_WIDTH, game.LOGIC_HEIGHT, camera);
        this.uiStage = new Stage(viewport, game.getBatch());
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
