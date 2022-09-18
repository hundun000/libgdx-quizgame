package hundun.gdxgame.quizgame.core.config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
    @Getter
    @Setter
    private String currentEnv = DEFAULT_ENV;
    Map<String, EnvPackage> packageMap = new HashMap<>();
    
    private static class EnvPackage {
        
        private final String ENV;
        protected Texture menuTexture;
        protected Texture playScreenBackground;
        protected Texture countdownClockTexture;
        protected Texture currentTeamSignTexture;
        protected Texture questionResultCorrectAnimationSheet;
        protected Texture questionResultWrongAnimationSheet;
        protected Texture questionResultSkippedAnimationSheet;
        protected Texture questionResultTineoutAnimationSheet;
        protected Texture skillButtonBackground;
        protected Texture skillUseOutButtonBackground;
        protected Texture questionStemBackground;
        protected Texture optionButtonCorrectMask;
        protected Texture optionButtonWrongMask;
        //protected Texture optionButtonHidenMask;
        protected Texture optionButtonBackground;
        protected TextureAtlas animationsTextureAtlas;
        protected Map<SystemButtonType, TextureRegion> systemButtonIconMap = new HashMap<>();

        
        EnvPackage(String ENV) {
            this.ENV = ENV;
            
            
            menuTexture = textureOrDefault("menu.png");
            playScreenBackground = textureOrDefault("playscreen.png");
            countdownClockTexture = textureOrDefault("countdownClock.png");
            currentTeamSignTexture = textureOrDefault("currentTeamSignTexture.png");
            skillButtonBackground = textureOrDefault("skillButtonBackground.png");
            skillUseOutButtonBackground = textureOrDefault("skillUseOutButtonBackground.png");
            questionStemBackground = textureOrDefault("questionStemBackground.png");
            optionButtonCorrectMask = textureOrDefault("optionButtonCorrectMask.png");
            optionButtonWrongMask = textureOrDefault("optionButtonWrongMask.png");
            //optionButtonHidenMask = textureOrDefault("optionButtonHidenMask.png");
            optionButtonBackground = textureOrDefault("optionButtonBackground.png");
            questionResultCorrectAnimationSheet = textureOrDefault(Gdx.files.internal("sprite-animation4.png"));
            questionResultWrongAnimationSheet = textureOrDefault(Gdx.files.internal("sprite-animation4.png"));
            questionResultSkippedAnimationSheet = textureOrDefault(Gdx.files.internal("sprite-animation4.png"));
            questionResultTineoutAnimationSheet = textureOrDefault(Gdx.files.internal("sprite-animation4.png"));
        
            {
                Texture texture = textureOrDefault("systemButton.png");
                TextureRegion[][] regions = TextureRegion.split(texture, 32, 32);
                systemButtonIconMap.put(SystemButtonType.SHOW_MATCH_SITUATION, regions[0][0]);
                systemButtonIconMap.put(SystemButtonType.EXIT_AS_DISCARD_MATCH, regions[0][1]);
                systemButtonIconMap.put(SystemButtonType.EXIT_AS_FINISH_MATCH, regions[0][2]);
//                systemButtonIconMap.put(ConstructionId.QUEEN_BEEHIVE, regions[0][3]);
//                systemButtonIconMap.put(ConstructionId.WOOD_KEEPING, regions[0][4]);
            }
            
            animationsTextureAtlas = new TextureAtlas(fileOrDefault("quiz-animations.atlas"));
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
        
        private FileHandle fileOrDefault(String subName) {
            try {
                return Gdx.files.internal("ui/" + ENV + "/" + subName);
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

        Set<String> textureOrDefaultFailHistory = new HashSet<>();

    }
    
    
    
    
    
    
    public TextureConfig(QuizGdxGame game) {
        game.getSaveHandler().registerSubHandler(this);
        
        packageMap.put("dev", new EnvPackage("dev"));
        //packageMap.put("pro", new EnvPackage("pro"));
    }
    
    public Texture getMenuTexture() {
        return packageMap.get(currentEnv).menuTexture;
    }

    public Texture getPlayScreenBackground() {
        return packageMap.get(currentEnv).playScreenBackground;
    }

    public Texture getCountdownClockTexture() {
        return packageMap.get(currentEnv).countdownClockTexture;
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

    public Texture getSkillButtonBackground() {
        return packageMap.get(currentEnv).skillButtonBackground;
    }

    public Texture getSkillUseOutButtonBackground() {
        return packageMap.get(currentEnv).skillUseOutButtonBackground;
    }

    public Texture getQuestionStemBackground() {
        return packageMap.get(currentEnv).questionStemBackground;
    }

    public Map<SystemButtonType, TextureRegion> getSystemButtonIconMap() {
        return packageMap.get(currentEnv).systemButtonIconMap;
    }
    
    public Texture getCurrentTeamSignTexture() {
        return packageMap.get(currentEnv).currentTeamSignTexture;
    }
    
    public Texture getOptionButtonCorrectMask() {
        return packageMap.get(currentEnv).optionButtonCorrectMask;
    }

    public Texture getOptionButtonWrongMask() {
        return packageMap.get(currentEnv).optionButtonWrongMask;
    }
    
//    public Texture getOptionButtonHidenMask() {
//        return packageMap.get(currentEnv).optionButtonHidenMask;
//    }

    public Texture getOptionButtonBackground() {
        return packageMap.get(currentEnv).optionButtonBackground;
    }

    @Override
    public void applySystemSetting(SystemSetting systemSetting) {
        this.currentEnv = systemSetting.getEnv();
    }

    @Override
    public void currentSituationToSystemSetting(SystemSetting systemSetting) {
        systemSetting.setEnv(currentEnv);
    }
    
    public TextureAtlas getAnimationsTextureAtlas() {
        return packageMap.get(currentEnv).animationsTextureAtlas;
    }
}
