package hundun.gdxgame.quizgame.core.domain;

import java.util.ArrayList;
import java.util.List;

import hundun.gdxgame.quizgame.core.config.TextureConfig;
import hundun.gdxgame.quizgame.core.screen.HistoryScreen.MatchFinishHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hundun
 * Created on 2022/08/17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizRootSaveData {
    
    MyGameSaveData gameSaveData;
    SystemSetting systemSetting;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MyGameSaveData {
        List<MatchFinishHistory> matchFinishHistories;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SystemSetting {
        String env;
    }
    
    public static class Factory {
        public static QuizRootSaveData newGame() {
            return new QuizRootSaveData(
                    MyGameSaveData.builder()
                            .matchFinishHistories(new ArrayList<>())
                            .build(),
                    new SystemSetting(TextureConfig.DEFAULT_ENV)
                    );
        }
    }

}
