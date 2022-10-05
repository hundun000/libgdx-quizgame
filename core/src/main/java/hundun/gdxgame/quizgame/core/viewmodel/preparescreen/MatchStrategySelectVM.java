package hundun.gdxgame.quizgame.core.viewmodel.preparescreen;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.gdxgame.quizgame.core.config.TextureAtlasKeys;
import hundun.gdxgame.share.base.util.DrawableFactory;
import hundun.gdxgame.share.base.util.JavaFeatureForGwt;
import hundun.quizlib.prototype.match.MatchStrategyType;

/**
 * @author hundun
 * Created on 2022/09/14
 */
public class MatchStrategySelectVM extends Table {
    QuizGdxGame game;
    
    MatchStrategyType currentType;
    List<MatchStrategyNode> nodes = new ArrayList<>();

    
    IMatchStrategyChangeListener slotNumListener;
    
    public MatchStrategySelectVM(QuizGdxGame game, IMatchStrategyChangeListener slotNumListener) {
        this.game = game;
        this.slotNumListener = slotNumListener;
        this.setBackground(game.getTextureConfig().getHistoryAreaVMBackgroundDrawable());

        initUI(JavaFeatureForGwt.arraysAsList(MatchStrategyType.PRE, MatchStrategyType.MAIN));
        
        if (game.debugMode) {
            this.debugCell();
        }
        
    }
    
    public void checkSlotNum(MatchStrategyType newType) {

        if (currentType != newType) {
            currentType = newType;
            slotNumListener.onMatchStrategyChange(newType);
        }
        for (int i = 0; i < nodes.size(); i++) {
            MatchStrategyNode vm = nodes.get(i);
            vm.updateRuntime(vm.type == currentType);
        }
    }

    
    public interface IMatchStrategyChangeListener {
        void onMatchStrategyChange(MatchStrategyType newType);
    }
    
    private void initUI(List<MatchStrategyType> teamPrototypes) {
        
        this.clear();
        nodes.clear();
        teamPrototypes.forEach(it -> {
            MatchStrategyNode vm = new MatchStrategyNode();
            vm.updatePrototype(it);
            nodes.add(vm);
            MatchStrategySelectVM.this.add(vm).padBottom(MatchStrategyNode.SIGN_SIZE / 2).row();
        });
    }

    
    private class MatchStrategyNode extends Table {
        final static int SIGN_SIZE = 50;
        final Image signSlotImage;
        final Drawable signDrawable;
        final static int NAME_WIDTH = 200;
        final TextButton nameLabel;
        
        MatchStrategyType type;
        
        MatchStrategyNode() {
            this.signDrawable = new TextureRegionDrawable(game.getTextureConfig().getPlayScreenUITextureAtlas().findRegion(TextureAtlasKeys.pLAYSCREEN_CURRENTTEAMSIGN_BLACK));
            
            this.signSlotImage = new Image();
            this.add(signSlotImage).width(SIGN_SIZE).height(SIGN_SIZE).padRight(SIGN_SIZE * 0.5f);
            
            this.nameLabel = new TextButton("TEMP", game.getMainSkin());
            this.add(nameLabel).width(NAME_WIDTH);
            
            this.setTouchable(Touchable.enabled);
            this.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    checkSlotNum(MatchStrategyNode.this.type);
                }
            });
        }
        
        void updatePrototype(MatchStrategyType type) {
            this.type = type;
            nameLabel.setText(MatchStrategyInfoVM.toMatchStrategyTypeChinese(type));
        }
        
        void updateRuntime(boolean isCurrent) {
            if (isCurrent) {
                signSlotImage.setDrawable(signDrawable);
            } else {
                signSlotImage.setDrawable(null);
            }
        }
    }
}
