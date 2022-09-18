package hundun.gdxgame.quizgame.core.viewmodel.playscreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.gdxgame.quizgame.core.config.TextureAtlasKeys;
import hundun.gdxgame.share.base.util.JavaFeatureForGwt;
import hundun.gdxgame.share.base.util.JavaFeatureForGwt.NumberFormat;
import hundun.quizlib.prototype.RolePrototype;
import hundun.quizlib.prototype.skill.SkillSlotPrototype;
import hundun.quizlib.view.question.QuestionView;
import hundun.quizlib.view.role.RoleRuntimeView;

/**
 * @author hundun
 * Created on 2022/09/01
 */
public class SkillBoardVM extends Table {
    
    CallerAndCallback callerAndCallback;
    
    List<SkillNode> nodes = new ArrayList<>();
    QuizGdxGame game;
    
    public SkillBoardVM(
            QuizGdxGame game,
            CallerAndCallback callerAndCallback,
            Drawable background
            ) {
        this.game = game;
        this.callerAndCallback = callerAndCallback;
        //setBackground(background);
        
        
        
    }
    
    public class SkillNode extends Table {
        static final int LENGTH = 160;
        
        Image backgroundImage;
        final Label mainLabel;
        final Label textLabel;
        Drawable normalBackground;
        Drawable useOutBackground;
        public SkillNode(QuizGdxGame game, int index) {
            this.mainLabel = new Label("TEMP", game.getMainSkin());
            this.textLabel = new Label("TEMP", game.getMainSkin());
            this.backgroundImage = new Image();
            backgroundImage.setBounds(0, 0, SkillNode.LENGTH, SkillNode.LENGTH);
            this.addActor(backgroundImage);
            
            this.add(mainLabel);
            this.row();
            this.add(textLabel);
            
            this.addListener(
                    new ClickListener(){
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            super.clicked(event, x, y);
                            callerAndCallback.onChooseSkill(index);
                        }
                    });
        }

        public void updateRuntime(int skillRemainTime) {
            textLabel.setText(JavaFeatureForGwt.stringFormat("剩余次数：%s", skillRemainTime));
            Drawable background;
            if (skillRemainTime > 0) {
                setTouchable(Touchable.enabled);
                background = normalBackground;
            } else {
                setTouchable(Touchable.disabled);
                background = useOutBackground;
            }
            backgroundImage.setDrawable(background);
        }
        
        public void updatePrototy(SkillSlotPrototype skillSlotPrototype) {
            mainLabel.setText(skillSlotPrototype.getShowName());
            String regionId = JavaFeatureForGwt.stringFormat(
                    TextureAtlasKeys.PLAYSCREEN_SKILLBUTTON_TEMPLATE, 
                    skillSlotPrototype.getName());
            normalBackground = new TextureRegionDrawable(
                    game.getTextureConfig().getPlayScreenUITextureAtlas().findRegion(regionId)
                    );
            useOutBackground = new TextureRegionDrawable(
                    game.getTextureConfig().getPlayScreenUITextureAtlas().findRegion(TextureAtlasKeys.PLAYSCREEN_SKILLBUTTONUSEOUT)
                    );
        }
    }
    
    public static interface CallerAndCallback {
        void onChooseSkill(int index);
    }

    public void updateRole(RolePrototype rolePrototype, RoleRuntimeView roleRuntimeView) {
        nodes.clear();
        this.clear();
        if (rolePrototype == null) {
            return;
        }
        
        int newLineIndex = (rolePrototype.getSkillSlotPrototypes().size() - 1) / 2;
        for (int i = 0; i < rolePrototype.getSkillSlotPrototypes().size(); i++) {
            SkillSlotPrototype skillSlotPrototype = rolePrototype.getSkillSlotPrototypes().get(i);
            int remainUseTime = roleRuntimeView.getSkillSlotRuntimeViews().get(i).getRemainUseTime();
            SkillNode node = new SkillNode(game, i);
            node.updatePrototy(skillSlotPrototype);
            node.updateRuntime(remainUseTime);
            nodes.add(node);
            Cell<SkillNode> cell = this.add(node)
                    .padBottom(SkillNode.LENGTH / 4)
                    .width(SkillNode.LENGTH)
                    .height(SkillNode.LENGTH)
                    ;
            
            
            if (i == newLineIndex) {
                cell.padRight(SkillNode.LENGTH / 2);
                this.row();
            }
            if (i == newLineIndex + 1) {
                cell.padLeft(SkillNode.LENGTH / 2);
            }
            
        }
        
        if (game.debugMode) {
            this.debugAll();
        }
    }

    public void updateSkillRuntime(int index, int skillRemainTime) {
        nodes.get(index).updateRuntime(skillRemainTime);
    }
}
