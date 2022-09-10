package hundun.gdxgame.quizgame.core.domain.viewmodel.playscreen;

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
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import hundun.gdxgame.quizgame.core.QuizGdxGame;
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
        
        final SkillButton skillButton;
        
        public SkillNode(QuizGdxGame game, int index, Drawable background) {
            this.skillButton = new SkillButton(game, index);
            
            Image backgroundImage = new Image(background);
            backgroundImage.setBounds(0, 0, SkillNode.LENGTH, SkillNode.LENGTH);
            this.addActor(backgroundImage);
            
            this.add(skillButton);
            
            this.addListener(
                    new InputListener(){
                        @Override
                        public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                            callerAndCallback.onChooseSkill(index);
                        }
                        @Override
                        public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                            return true;
                        }
                    });
        }
    }
    
    public class SkillButton extends Label {

        final int index;
        
        public SkillButton(QuizGdxGame game, int index) {
            super("TEMP", game.getMainSkin());
            this.index = index;
            this.setTouchable(Touchable.disabled);
            
        }
        
        
        
    }
    
    public static interface CallerAndCallback {
        void onChooseSkill(int index);
    }

    public void updateRole(RolePrototype rolePrototype, RoleRuntimeView roleRuntimeView) {
        nodes.clear();
        this.clear();
        
        int newLineIndex = (rolePrototype.getSkillSlotPrototypes().size() - 1) / 2;
        for (int i = 0; i < rolePrototype.getSkillSlotPrototypes().size(); i++) {
            SkillSlotPrototype skillSlotPrototype = rolePrototype.getSkillSlotPrototypes().get(i);
            int remainUseTime = roleRuntimeView.getSkillSlotRuntimeViews().get(i).getRemainUseTime();
            Drawable background;
            if (remainUseTime > 0) {
                background = new TextureRegionDrawable(game.getTextureConfig().getSkillButtonBackground());
            } else {
                background = new TextureRegionDrawable(game.getTextureConfig().getSkillUseOutButtonBackground());
            }
            SkillNode node = new SkillNode(game, i, background);
            node.skillButton.setText(skillSlotPrototype.getName());
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

    public void updateSkill(int index, int skillRemainTime) {
        // TODO Auto-generated method stub
        
    }
}
