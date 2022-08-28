package com.mygdx.game.core.data;

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
public class RootSaveData {
    
    MyGameSaveData data;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MyGameSaveData {
        String value;
    }
    
    public static class Factory {
        public static RootSaveData newGame() {
            return new RootSaveData(
                    new MyGameSaveData("Hello world")
                    );
        }
    }

}
