package hundun.gdxgame.quizgame.core.config;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import lombok.Getter;

public class TextureConfig {

    private static final String ENV = "pro";
    
    @Getter
    protected Texture menuTexture;
    @Getter
    protected Texture countdownClockTexture;
    @Getter
    protected Texture questionResultCorrectAnimationSheet;
    @Getter
    protected Texture questionResultWrongAnimationSheet;
    @Getter
    protected Texture questionResultSkippedAnimationSheet;
    @Getter
    protected Texture questionResultTineoutAnimationSheet;
    @Getter
    protected Texture skillButtonBackground;
    @Getter
    protected Texture skillUseOutButtonBackground;
    
    private Texture textureOrDefault(FileHandle file) {
        try {
            return new Texture(file);
        } catch (Exception e) {
            return new Texture(Gdx.files.internal("badlogic.jpg"));
        }
    }
    
    private Texture textureOrDefault(String subName) {
        try {
            return new Texture(Gdx.files.internal("ui/" + ENV + "/" + subName));
        } catch (Exception e) {
            return new Texture(Gdx.files.internal("badlogic.jpg"));
        }
    }
    
    public TextureConfig() {


        menuTexture = textureOrDefault("menu.png");
        countdownClockTexture = textureOrDefault("countdownClock.png");
        skillButtonBackground = textureOrDefault("skillButtonBackground.png");
        skillUseOutButtonBackground = textureOrDefault("skillUseOutButtonBackground.png");
        questionResultCorrectAnimationSheet = textureOrDefault(Gdx.files.internal("sprite-animation4.png"));
        questionResultWrongAnimationSheet = textureOrDefault(Gdx.files.internal("sprite-animation4.png"));
        questionResultSkippedAnimationSheet = textureOrDefault(Gdx.files.internal("sprite-animation4.png"));
        questionResultTineoutAnimationSheet = textureOrDefault(Gdx.files.internal("sprite-animation4.png"));
    }

}
