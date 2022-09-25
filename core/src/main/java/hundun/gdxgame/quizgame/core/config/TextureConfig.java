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
import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.gdxgame.quizgame.core.domain.QuizRootSaveData.SystemSetting;
import hundun.gdxgame.quizgame.core.domain.QuizSaveHandler.ISubSystemSettingHandler;
import hundun.gdxgame.share.base.util.JavaFeatureForGwt;

public class TextureConfig implements ISubSystemSettingHandler {
    public static String DEFAULT_ENV = "pro";

    private String currentEnv = DEFAULT_ENV;
    Map<String, EnvPackage> packageMap = new HashMap<>();
    
    private static class EnvPackage {
        
        private final String ENV;
        Set<String> textureOrDefaultFailHistory = new HashSet<>();
        
        protected final AtlasRegion menuTitle;
        protected final AtlasRegion menuScreenBackground;
        protected final AtlasRegion prepareScreenBackground;
        protected final AtlasRegion playScreenBackground;
        
        protected final Texture tempAnimationSheet;


        protected final TextureAtlas maskTextureAtlas;
        protected final TextureAtlas animationsTextureAtlas;
        protected final TextureAtlas playScreenUITextureAtlas;
        
        
        EnvPackage(String ENV) {
            this.ENV = ENV;
            

            
            tempAnimationSheet = textureOrDefault(Gdx.files.internal("sprite-animation4.png"));
            
            animationsTextureAtlas = new TextureAtlas(fileOrDefault("playScreenAnimation.atlas"));
            maskTextureAtlas = new TextureAtlas(fileOrDefault("maskUI.atlas"));
            playScreenUITextureAtlas = new TextureAtlas(fileOrDefault("playScreenUI.atlas"));
            
            
            
            TextureAtlas screensTextureAtlas = new TextureAtlas(fileOrDefault("screens.atlas"));
            menuTitle = screensTextureAtlas.findRegion(TextureAtlasKeys.SCREEN_MEMU_TITLE);
            menuScreenBackground = screensTextureAtlas.findRegion(TextureAtlasKeys.SCREEN_MEMU);
            prepareScreenBackground = screensTextureAtlas.findRegion(TextureAtlasKeys.SCREEN_PREPARE);
            playScreenBackground = screensTextureAtlas.findRegion(TextureAtlasKeys.SCREEN_PLAY);
            
            
        }
        
        private Texture textureOrDefault(FileHandle file) {
            try {
                return new Texture(file);
            } catch (Exception e) {
                return new Texture(Gdx.files.internal("badlogic.jpg"));
            }
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
        
        //packageMap.put("dev", new EnvPackage("dev"));
        packageMap.put("pro", new EnvPackage("pro"));
        
//        if (game.debugMode) {
//            currentEnv = "dev";
//        } else {
//            currentEnv = "pro";
//        }
    }
    
    public AtlasRegion getMenuTitle() {
        return packageMap.get(currentEnv).menuTitle;
    }
    
    public AtlasRegion getMenuScreenBackground() {
        return packageMap.get(currentEnv).menuScreenBackground;
    }

    public AtlasRegion getPlayScreenBackground() {
        return packageMap.get(currentEnv).playScreenBackground;
    }
    
    public AtlasRegion getPrepareScreenBackground() {
        return packageMap.get(currentEnv).prepareScreenBackground;
    }

    public AtlasRegion getHistoryScreenBackground() {
        return packageMap.get(currentEnv).playScreenBackground;
    }
    
    public TextureAtlas getMaskTextureAtlas() {
        return packageMap.get(currentEnv).maskTextureAtlas;
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
    
    public Texture getTempAnimationSheet() {
        return packageMap.get(currentEnv).tempAnimationSheet;
    }
}
