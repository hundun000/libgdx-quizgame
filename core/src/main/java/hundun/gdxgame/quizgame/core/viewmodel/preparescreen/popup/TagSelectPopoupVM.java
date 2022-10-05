package hundun.gdxgame.quizgame.core.viewmodel.preparescreen.popup;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.gdxgame.quizgame.core.viewmodel.preparescreen.TagManageSlotVM;
import hundun.gdxgame.share.base.util.DrawableFactory;
import hundun.quizlib.prototype.TeamPrototype;

/**
 * @author hundun
 * Created on 2022/09/14
 */
public class TagSelectPopoupVM extends AbstractSelectPopoupVM<TagManageSlotVM> {

    
    Button doneButton;
    TeamPrototype currenTeamPrototype;
    
    public TagSelectPopoupVM(
            QuizGdxGame game, 
            IWaitTagSelectCallback callback
            ) {
        super(game, 
                DrawableFactory.getViewportBasedAlphaBoard(game.getWidth(), game.getHeight()), 
                game.getTextureConfig().getHistoryAreaVMBackgroundDrawable(),
                new LayoutConfig(TagManageSlotVM.NODE_WIDTH, TagManageSlotVM.NODE_HEIGHT, 8.0f, false)
                );
        this.doneButton = new TextButton("返回", game.getMainSkin());
        
        doneButton.addListener(new TagSelectDoneClickListener(callback));
        
        this.row();
        this.add(doneButton);
    }

    
    public class TagSelectDoneClickListener extends ClickListener {
        IWaitTagSelectCallback callback;
        
        TagSelectDoneClickListener(IWaitTagSelectCallback callback) {
            this.callback = callback;
        }
        @Override
        public void clicked(InputEvent event, float x, float y) {
            super.clicked(event, x, y);
            callback.onTagSelectDone(currenTeamPrototype);
        }
    }
    
    public static interface IWaitTagSelectCallback {
        void callShowTagSelectPopoup(TeamPrototype currenTeamPrototype, Set<String> allTags);
        void onTagSelectDone(TeamPrototype currenTeamPrototype);
    }

    public void callShow(TeamPrototype currenTeamPrototype, Set<String> allTags) {
        this.currenTeamPrototype = currenTeamPrototype;
        List<TagManageSlotVM> candidateVMs = allTags.stream()
                .map(tag -> {
                    TagManageSlotVM tagNodeVM = new TagManageSlotVM(game);
                    tagNodeVM.updateData(tag, currenTeamPrototype);
                    return tagNodeVM;
                })
                .collect(Collectors.toList())
                ;
        updateScrollPane(candidateVMs);
    }
}
