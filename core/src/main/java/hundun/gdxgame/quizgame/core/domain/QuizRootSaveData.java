package hundun.gdxgame.quizgame.core.domain;

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
    
    MyGameSaveData data;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MyGameSaveData {
        String value;
    }
    
    public static class Factory {
        public static QuizRootSaveData newGame() {
            return new QuizRootSaveData(
                    new MyGameSaveData("Hello world")
                    );
        }
    }

}
