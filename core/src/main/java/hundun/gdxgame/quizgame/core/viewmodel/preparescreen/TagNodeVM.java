package hundun.gdxgame.quizgame.core.viewmodel.preparescreen;
/**
 * @author hundun
 * Created on 2022/08/30
 */

import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.quizlib.prototype.TeamPrototype;
import lombok.Getter;

public class TagNodeVM extends Table {
    
    public static final int NODE_WIDTH = 300;
    public static final int NODE_HEIGHT = 100;
    
    Label label;

    
    public TagNodeVM(QuizGdxGame game, String tag) {
        this.label = new Label("TEMP", game.getMainSkin());

        
        this.add(label);
        
        label.setText(tag);
    }


}
