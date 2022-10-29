package hundun.gdxgame.quizgame.core.viewmodel.playscreen;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Music.OnCompletionListener;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;

import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.gdxgame.quizgame.core.config.TextureAtlasKeys;
import hundun.gdxgame.corelib.base.util.DrawableFactory;
import hundun.gdxgame.corelib.base.util.JavaFeatureForGwt.NumberFormat;
import hundun.quizlib.prototype.question.ResourceType;
import hundun.quizlib.view.question.QuestionView;

/**
 * @author hundun
 * Created on 2022/09/01
 */
public class QuestionResourceAreaVM extends Table {
    
    final QuizGdxGame game;
    final IAudioCallback callback;
    
    Label stemPart;

    Map<ResourceType, Drawable> backgroundMap = new HashMap<>();
    final ImageResourceNode imageResourceNode;
    final AudioResourceNode audioResourceNode;
    
    public QuestionResourceAreaVM(
            QuizGdxGame game,
            IAudioCallback callback,
            TextureAtlas alAtlasRegion
            ) {
        this.game = game;
        this.callback = callback;
        
        Drawable background = new TextureRegionDrawable(alAtlasRegion.findRegion(
                TextureAtlasKeys.PLAYSCREEN_ZACAMUSUME_Q
                ));
        backgroundMap.put(ResourceType.NONE, background);
        backgroundMap.put(ResourceType.IMAGE, background);
        backgroundMap.put(ResourceType.VOICE, background);
        
        imageResourceNode = new ImageResourceNode();
        audioResourceNode = new AudioResourceNode(game, callback);
        
    }
    
    public void stopAudio() {
        audioResourceNode.stopAudio();
        this.clear();
    }
    
    public void updateQuestion(QuestionView questionView) {
        Drawable newBackground = backgroundMap.get(questionView.getResource().getType());
        this.setBackground(newBackground);
        
        this.clear();
        if (questionView.getResource().getType() == ResourceType.IMAGE) {
            imageResourceNode.updateResource(questionView.getResource().getData());
            this.add(imageResourceNode);
        } else if (questionView.getResource().getType() == ResourceType.VOICE) {
            audioResourceNode.updateResource(questionView.getResource().getData());
            this.add(audioResourceNode);
            callback.onPlayReady();
        }
    }
    
    public static interface IAudioCallback {
        void onFirstPlayDone();
        void onPlayReady();
    }

    private static class AudioResourceNode extends Table {
        
        private Music music;
        final OnCompletionListener onCompletionListener;
        int playCount;
        Label timeLabel;
        TextButton playButton;
        Image animationImage;
        List<TextureRegionDrawable> animations;
        
        private final NumberFormat format;
        
        private FileHandle getFile(String key) {
            String path = "quiz/audios/" + key;
            return Gdx.files.internal(path);
        }
        
        public void stopAudio() {
            if (music != null) {
                music.stop();
                music.dispose();
            }
            music = null;
            playCount = 0;
        }

        public AudioResourceNode(
                QuizGdxGame game, 
                IAudioCallback callback
                ) {
            this.format = NumberFormat.getFormat(1, 1);
            this.timeLabel = new Label("TEMP", game.getMainSkin());
            timeLabel.setFontScale(1.5f);
            this.playButton = new TextButton("播放", game.getMainSkin());
            playButton.getLabel().setFontScale(1.5f);
            this.animationImage = new Image();
            AtlasRegion[] animationAtlasRegions = game.getTextureConfig().getPlayScreenUITextureAtlas().findRegions(TextureAtlasKeys.PLAYSCREEN_AUDIO).items;
            this.animations = Stream.of(animationAtlasRegions)
                    .filter(it -> it != null)
                    .map(it -> new TextureRegionDrawable(it))
                    .collect(Collectors.toList());
            
            playButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    if (!music.isPlaying()) {
                        music.play();
                        playCount++;
                        Gdx.app.log(this.getClass().getSimpleName(), "playCount change to " + playCount);
                    }
                }
            });
            this.onCompletionListener = new OnCompletionListener() {
                @Override
                public void onCompletion(Music music) {
                    if (AudioResourceNode.this.playCount >= 1) {
                        callback.onFirstPlayDone();
                    }
                }
            };
            
            this.add(animationImage);
            this.row();
            this.add(timeLabel);
            this.row();
            this.add(playButton).fill();
            
            this.background(DrawableFactory.getSimpleBoardBackground(10, 10));
        }
        
        public void updateTimer() {
            String text;
            if (!music.isPlaying()) {
                text = "";
                animationImage.setDrawable(animations.get(0));
            } else {
                text = format.format(music.getPosition());
                int imageIndex = ((int)music.getPosition()) % animations.size();
                animationImage.setDrawable(animations.get(imageIndex));
            }
            timeLabel.setText(text);
        }
        
        @Override
        public void draw(Batch batch, float parentAlpha) {
            updateTimer();
            super.draw(batch, parentAlpha);
        }
        
        
        public void updateResource(String data) {
            stopAudio();
            music = Gdx.audio.newMusic(getFile(data));
            music.setOnCompletionListener(onCompletionListener);
        } 
    }
    
    private static class ImageResourceNode extends Image {
        
        private FileHandle getFile(String key) {
            String path = "quiz/pictures/" + key;
            return Gdx.files.internal(path);
        }
        
        public ImageResourceNode() {
            this.setScaling(Scaling.fit);
        }
        
        public void updateResource(String data) {
            this.setDrawable(new TextureRegionDrawable(new Texture(getFile(data))));
        }
    }
}
