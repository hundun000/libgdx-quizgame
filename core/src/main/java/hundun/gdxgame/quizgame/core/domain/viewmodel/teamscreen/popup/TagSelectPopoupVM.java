package hundun.gdxgame.quizgame.core.domain.viewmodel.teamscreen.popup;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.gdxgame.quizgame.core.domain.viewmodel.teamscreen.TeamNodeVM;
import hundun.gdxgame.share.base.util.DrawableFactory;
import hundun.quizlib.prototype.TeamPrototype;

/**
 * @author hundun
 * Created on 2022/09/14
 */
public class TagSelectPopoupVM extends AbstractSelectPopoupVM<TagNodeVM> {

    
    Button tagSelectDoneButton;
    
    public TagSelectPopoupVM(
            QuizGdxGame game, 
            IWaitTagSelectCallback callback,
            Drawable background
            ) {
        super(game, background);
        this.tagSelectDoneButton = new TextButton("done", game.getMainSkin());
        
        tagSelectDoneButton.addListener(new TagSelectDoneClickListener(callback));
        
        this.row();
        this.add(tagSelectDoneButton);
    }
    
    public static class Factory {
        public static TagSelectPopoupVM build(QuizGdxGame game, IWaitTagSelectCallback callback) {
            
            return new TagSelectPopoupVM(game, 
                    callback,
                    DrawableFactory.getSimpleBoardBackground()
                    );
        }
    }
    
    public static class TagSelectDoneClickListener extends ClickListener {
        IWaitTagSelectCallback callback;
        
        TagSelectDoneClickListener(IWaitTagSelectCallback callback) {
            this.callback = callback;
        }
        @Override
        public void clicked(InputEvent event, float x, float y) {
            super.clicked(event, x, y);
            callback.onTagSelectDone();
        }
    }
    
    public static interface IWaitTagSelectCallback {
        void callShowTagSelectPopoup(TeamPrototype currenTeamPrototype, Set<String> allTags);
        void onTagSelectDone();
    }

    public void callShow(TeamPrototype currenTeamPrototype, Set<String> allTags) {
        List<TagNodeVM> candidateVMs = allTags.stream()
                .map(tag -> {
                    TagNodeVM tagNodeVM = new TagNodeVM(game);
                    tagNodeVM.updateData(tag, currenTeamPrototype);
                    return tagNodeVM;
                })
                .collect(Collectors.toList())
                ;
        updateScrollPane(candidateVMs);
    }
}
