package hundun.gdxgame.quizgame.core.domain;

import java.util.ArrayList;
import java.util.List;

import hundun.gdxgame.quizgame.core.config.TextureConfig;
import hundun.gdxgame.quizgame.core.screen.HistoryScreen.MatchHistoryDTO;
import hundun.quizlib.prototype.TeamPrototype;
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
@Builder
public class QuizRootSaveData {
    
    MyGameSaveData gameSaveData;
    SystemSetting systemSetting;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MyGameSaveData {
        List<TeamPrototype> teamPrototypes;
        List<MatchHistoryDTO> matchFinishHistories;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SystemSetting {
        String env;
    }
    
    public static class Factory {
        public static QuizRootSaveData newGame() {
            return QuizRootSaveData.builder()
                    .gameSaveData(MyGameSaveData.builder()
                            .teamPrototypes(null)
                            .matchFinishHistories(new ArrayList<>())
                            .build()
                            )
                    .systemSetting(new SystemSetting(TextureConfig.DEFAULT_ENV))
                    .build();
        }
    }

}
