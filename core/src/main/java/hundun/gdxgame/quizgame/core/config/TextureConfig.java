package hundun.gdxgame.quizgame.core.config;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import lombok.Getter;

public class TextureConfig {

    @Getter
    protected Texture menuTexture;
    
    private Texture textureOrDefault(FileHandle file) {
        try {
            return new Texture(file);
        } catch (Exception e) {
            return new Texture(Gdx.files.internal("badlogic.jpg"));
        }
    }
    
    public TextureConfig() {


        menuTexture = textureOrDefault(Gdx.files.internal("menu.png"));
        
        
    }

}