package hundun.gdxgame.quizgame.core.viewmodel.preparescreen;

import java.util.stream.Stream;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.gdxgame.quizgame.core.screen.PrepareScreen;
import hundun.gdxgame.share.base.util.DrawableFactory;
import hundun.quizlib.prototype.match.MatchStrategyType;

/**
 * @author hundun
 * Created on 2022/09/22
 */
public class MatchStrategyInfoVM extends Table {
    
    Label nameLabel;
    Label[][] labelMap;
    
    static final int MAP_LINE_SIZE = 5;
    
    public MatchStrategyInfoVM(QuizGdxGame game) {
        this.nameLabel = new Label("TEMP", game.getMainSkin());
        this.labelMap = new Label[MAP_LINE_SIZE][2];
        this.setBackground(DrawableFactory.getViewportBasedAlphaBoard(1, 1));
        
        labelMap[0][0] = new Label("每题时间限制：", game.getMainSkin());
        labelMap[1][0] = new Label("每局答题总数限制：", game.getMainSkin());
        labelMap[2][0] = new Label("是否可使用技能：", game.getMainSkin());
        labelMap[3][0] = new Label("轮换队伍的答题数：", game.getMainSkin());
        labelMap[4][0] = new Label("参赛队伍数量限制：", game.getMainSkin());
        for (int i = 0; i < MAP_LINE_SIZE; i++) {
            labelMap[i][1] = new Label("", game.getMainSkin());
        }
        
        
    }
    
    public void updateStrategy(MatchStrategyType type) {

        switch (type) {
            case PRE:
                nameLabel.setText("预赛");
                labelMap[0][1].setText("20秒");
                labelMap[1][1].setText("5");
                labelMap[2][1].setText("否");
                labelMap[3][1].setText("");
                labelMap[4][1].setText("1");
                break;
            case MAIN:
                nameLabel.setText("决赛");
                labelMap[0][1].setText("20秒");
                labelMap[1][1].setText("");
                labelMap[2][1].setText("是");
                labelMap[3][1].setText("1");
                labelMap[4][1].setText("2");
                break;
            default:
                nameLabel.setText("决赛");
                labelMap[1][0].setText("");
                labelMap[1][1].setText("");
                labelMap[1][2].setText("");
                labelMap[1][3].setText("");
                labelMap[1][4].setText("");
                break;
        }


        
        this.clear();
        this.add(nameLabel);
        this.row();
        Stream.of(labelMap)
                .filter(entry -> !entry[1].getText().isEmpty())
                .forEach(entry -> {
                    this.add(entry[0]);
                    this.add(entry[1]);
                    this.row();
                });
    }

}
