package hundun.gdxgame.quizgame.core.domain;

import hundun.gdxgame.quizgame.core.config.TextureConfig;
import lombok.AllArgsConstructor;
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
    
    MyGameSaveData game;
    SystemSetting systemSetting;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MyGameSaveData {
        String value;
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
                    new MyGameSaveData("Hello world"),
                    new SystemSetting(TextureConfig.DEFAULT_ENV)
                    );
        }
    }

}
