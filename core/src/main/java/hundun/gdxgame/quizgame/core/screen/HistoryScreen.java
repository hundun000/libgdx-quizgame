package hundun.gdxgame.quizgame.core.screen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.gdxgame.quizgame.core.domain.QuizRootSaveData;
import hundun.gdxgame.quizgame.core.domain.QuizRootSaveData.MyGameSaveData;
import hundun.gdxgame.quizgame.core.domain.QuizSaveHandler.ISubGameSaveHandler;
import hundun.gdxgame.quizgame.core.viewmodel.historyscreen.HistoryAreaVM;
import hundun.gdxgame.quizgame.core.viewmodel.historyscreen.HistoryAreaVM.LayoutConfig;
import hundun.gdxgame.quizgame.core.viewmodel.preparescreen.TagManageSlotVM;
import hundun.gdxgame.quizgame.core.viewmodel.share.MatchHistoryVM;
import hundun.gdxgame.share.base.BaseHundunScreen;
import hundun.gdxgame.share.base.util.DrawableFactory;
import hundun.gdxgame.share.base.util.JavaFeatureForGwt;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author hundun
 * Created on 2022/08/30
 */
public class HistoryScreen extends BaseHundunScreen<QuizGdxGame, QuizRootSaveData> implements ISubGameSaveHandler {

    @Getter
    @Setter
    List<MatchHistoryDTO> histories = new ArrayList<>();
    
    HistoryAreaVM historyAreaVM;
    
    public HistoryScreen(QuizGdxGame game) {
        super(game);
        game.getSaveHandler().registerSubHandler(this);
    }

    @Override
    public void show() {
        super.show();
        Gdx.input.setInputProcessor(uiStage);
        game.getBatch().setProjectionMatrix(uiStage.getViewport().getCamera().combined);
        
        if (pushParams != null && pushParams.length > 0) {
            MatchHistoryDTO newHistory = (MatchHistoryDTO) pushParams[0];
            Gdx.app.log(this.getClass().getSimpleName(), JavaFeatureForGwt.stringFormat(
                    "pushParams by newHistory = %s", 
                    newHistory.toString()
                    ));
            addNewHistory(newHistory);
        }
        
        this.historyAreaVM = new HistoryAreaVM(game, 
                null, 
                new LayoutConfig(MatchHistoryVM.MatchHistoryVM_WIDTH, MatchHistoryVM.MatchHistoryVM_HEIGHT, 6.0f)
                );
        
        backUiStage.clear();
        uiRootTable.clear();
        
        Image backImage = new Image(game.getTextureConfig().getHistoryScreenBackground());
        backImage.setBounds(0, 0, game.getWidth(), game.getHeight());
        backUiStage.addActor(backImage);

        uiRootTable.add(historyAreaVM);
        uiRootTable.row();
        uiRootTable.add(new ToNextScreenButtonVM(game)).width(200).height(100).fill();
        
        if (game.debugMode) {
            uiRootTable.debugCell();
        }

        historyAreaVM.updateScrollPane(histories.stream()
                .map(it -> MatchHistoryVM.Factory.fromBO(game, it))
                .collect(Collectors.toList())
                );
    }

    @Override
    public void dispose() {
    }

    
    
    private static class ToNextScreenButtonVM extends TextButton {

        public ToNextScreenButtonVM(QuizGdxGame game) {
            super("返回", game.getMainSkin());
            
            this.addListener(
                    new ClickListener() {
                        public void clicked(InputEvent event, float x, float y) {
                            game.getScreenManager().pushScreen(QuizMenuScreen.class.getSimpleName(), "blending_transition");;
                        };
                    }
            );
        }
        
        
        
    }


    @Override
    protected void create() {
        
    }

    @Data
    public static class MatchHistoryDTO {
        Map<String, Integer> data;
    }
    
    private void addNewHistory(MatchHistoryDTO history) {
        histories.add(0, history);
    }

    @Override
    public void applyGameSaveData(MyGameSaveData myGameSaveData) {
        histories = myGameSaveData.getMatchFinishHistories();
        Gdx.app.log(this.getClass().getSimpleName(), JavaFeatureForGwt.stringFormat(
                "applyGameSaveData histories.size = %s", 
                histories.size()
                ));
    }

    @Override
    public void currentSituationToSaveData(MyGameSaveData myGameSaveData) {
        myGameSaveData.setMatchFinishHistories(histories);
    }

}
