package hundun.gdxgame.quizgame.core.config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.gdxgame.quizgame.core.domain.QuizRootSaveData.SystemSetting;
import hundun.gdxgame.quizgame.core.domain.QuizSaveHandler.ISubSystemSettingHandler;
import hundun.gdxgame.quizgame.core.viewmodel.playscreen.SystemBoardVM.SystemButtonType;
import hundun.gdxgame.share.base.util.JavaFeatureForGwt;
import lombok.Getter;
//import lombok.Getter;
import lombok.Setter;

public class TextureConfig implements ISubSystemSettingHandler {
    public static String DEFAULT_ENV = "dev";

    private String currentEnv = DEFAULT_ENV;
    Map<String, EnvPackage> packageMap = new HashMap<>();
    
    private static class EnvPackage {
        
        private final String ENV;
        Set<String> textureOrDefaultFailHistory = new HashSet<>();
        
        protected final AtlasRegion menuTexture;
        protected final AtlasRegion playScreenBackground;
        
        protected final Texture questionResultCorrectAnimationSheet;
        protected final Texture questionResultWrongAnimationSheet;
        protected final Texture questionResultSkippedAnimationSheet;
        protected final Texture questionResultTineoutAnimationSheet;

        protected final Texture optionButtonCorrectMask;
        protected final Texture optionButtonWrongMask;

        protected final TextureAtlas animationsTextureAtlas;
        protected final TextureAtlas playScreenUITextureAtlas;
        protected final Map<SystemButtonType, AtlasRegion> systemButtonIconMap = new HashMap<>();

        
        EnvPackage(String ENV) {
            this.ENV = ENV;
            

            optionButtonCorrectMask = textureOrDefault("optionButtonCorrectMask.png");
            optionButtonWrongMask = textureOrDefault("optionButtonWrongMask.png");

            questionResultCorrectAnimationSheet = textureOrDefault(Gdx.files.internal("sprite-animation4.png"));
            questionResultWrongAnimationSheet = textureOrDefault(Gdx.files.internal("sprite-animation4.png"));
            questionResultSkippedAnimationSheet = textureOrDefault(Gdx.files.internal("sprite-animation4.png"));
            questionResultTineoutAnimationSheet = textureOrDefault(Gdx.files.internal("sprite-animation4.png"));
        
            animationsTextureAtlas = new TextureAtlas(fileOrDefault("quiz-animations.atlas"));
            playScreenUITextureAtlas = new TextureAtlas(fileOrDefault("playScreenUI.atlas"));
            
            TextureAtlas screensTextureAtlas = new TextureAtlas(fileOrDefault("screens.atlas"));
            menuTexture = screensTextureAtlas.findRegion(TextureAtlasKeys.SCREEN_MEMU);
            playScreenBackground = screensTextureAtlas.findRegion(TextureAtlasKeys.SCREEN_MEMU);
            
            {
                AtlasRegion texture = playScreenUITextureAtlas.findRegion(TextureAtlasKeys.PLAYSCREEN_SYSTEMBUTTON);
                systemButtonIconMap.put(SystemButtonType.SHOW_MATCH_SITUATION, texture);
                systemButtonIconMap.put(SystemButtonType.EXIT_AS_DISCARD_MATCH, texture);
                systemButtonIconMap.put(SystemButtonType.EXIT_AS_FINISH_MATCH, texture);
//                systemButtonIconMap.put(ConstructionId.QUEEN_BEEHIVE, regions[0][3]);
//                systemButtonIconMap.put(ConstructionId.WOOD_KEEPING, regions[0][4]);
            }
            
            
        }
        
        
        
        private Texture textureOrDefault(FileHandle file) {
            try {
                return new Texture(file);
            } catch (Exception e) {
                return new Texture(Gdx.files.internal("badlogic.jpg"));
            }
        }
        
        private Texture textureOrDefault(String subName) {
            return new Texture(fileOrDefault(subName));
        }
        
        private FileHandle envfile(String subName) {
            return Gdx.files.internal("ui/" + ENV + "/" + subName);
        }
        
        private FileHandle fileOrDefault(String subName) {
            try {
                return envfile(subName);
            } catch (Exception e) {
                if (!textureOrDefaultFailHistory.contains(subName)) {
                    textureOrDefaultFailHistory.add(subName);
                    Gdx.app.error(this.getClass().getSimpleName(), 
                            JavaFeatureForGwt.stringFormat(
                                    "env = %s, subName = %s, fail: %s", 
                                    ENV,
                                    subName,
                                    e.getMessage()
                                    )
                            );
                }
                return Gdx.files.internal("badlogic.jpg");
            }
        }

        

    }
    
    
    
    
    
    
    public TextureConfig(QuizGdxGame game) {
        game.getSaveHandler().registerSubHandler(this);
        
        packageMap.put("dev", new EnvPackage("dev"));
        packageMap.put("pro", new EnvPackage("pro"));
    }
    
    public AtlasRegion getMenuTexture() {
        return packageMap.get(currentEnv).menuTexture;
    }

    public AtlasRegion getPlayScreenBackground() {
        return packageMap.get(currentEnv).playScreenBackground;
    }
    
    public Texture getQuestionResultCorrectAnimationSheet() {
        return packageMap.get(currentEnv).questionResultCorrectAnimationSheet;
    }

    public Texture getQuestionResultWrongAnimationSheet() {
        return packageMap.get(currentEnv).questionResultWrongAnimationSheet;
    }

    public Texture getQuestionResultSkippedAnimationSheet() {
        return packageMap.get(currentEnv).questionResultSkippedAnimationSheet;
    }

    public Texture getQuestionResultTineoutAnimationSheet() {
        return packageMap.get(currentEnv).questionResultTineoutAnimationSheet;
    }

    public Map<SystemButtonType, AtlasRegion> getSystemButtonIconMap() {
        return packageMap.get(currentEnv).systemButtonIconMap;
    }

    
    public Texture getOptionButtonCorrectMask() {
        return packageMap.get(currentEnv).optionButtonCorrectMask;
    }

    public Texture getOptionButtonWrongMask() {
        return packageMap.get(currentEnv).optionButtonWrongMask;
    }
    
    @Override
    public void applySystemSetting(SystemSetting systemSetting) {
        // FIXME temp disable when develop
        //this.currentEnv = systemSetting.getEnv();
    }

    @Override
    public void currentSituationToSystemSetting(SystemSetting systemSetting) {
        // FIXME temp disable when develop
        //systemSetting.setEnv(currentEnv);
    }
    
    public TextureAtlas getAnimationsTextureAtlas() {
        return packageMap.get(currentEnv).animationsTextureAtlas;
    }
    
    public TextureAtlas getPlayScreenUITextureAtlas() {
        return packageMap.get(currentEnv).playScreenUITextureAtlas;
    }
}
