package hundun.gdxgame.quizgame.core.viewmodel.playscreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Music.OnCompletionListener;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.gdxgame.share.base.util.JavaFeatureForGwt;
import hundun.gdxgame.share.base.util.JavaFeatureForGwt.NumberFormat;
import hundun.quizlib.prototype.question.ResourceType;
import hundun.quizlib.view.question.QuestionView;
import lombok.Getter;

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
            Drawable noneResourceBackground,
            Drawable imageResourceBackground,
            Drawable audioResourceBackground
            ) {
        this.game = game;
        this.callback = callback;
        
        backgroundMap.put(ResourceType.NONE, noneResourceBackground);
        backgroundMap.put(ResourceType.IMAGE, imageResourceBackground);
        backgroundMap.put(ResourceType.VOICE, audioResourceBackground);
        
        imageResourceNode = new ImageResourceNode();
        audioResourceNode = new AudioResourceNode(game, callback);
        
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
        Button playButton;
        
        private FileHandle getFile(String key) {
            String path = "quiz/audios/" + key;
            return Gdx.files.internal(path);
        }
        
        public AudioResourceNode(
                QuizGdxGame game, 
                IAudioCallback callback
                ) {
            this.timeLabel = new Label("TEMP", game.getMainSkin());
            this.playButton = new TextButton("play", game.getMainSkin());
            
            playButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    if (!music.isPlaying()) {
                        music.play();
                        playCount++;
                    }
                }
            });
            this.onCompletionListener = new OnCompletionListener() {
                @Override
                public void onCompletion(Music music) {
                    if (AudioResourceNode.this.playCount > 1) {
                        callback.onFirstPlayDone();
                    }
                }
            };
            
            this.add(timeLabel);
            this.row();
            this.add(playButton);
        }
        
        public void updateTimer() {
            String text;
            if (!music.isPlaying()) {
                text = "wait play";
            } else {
                text = JavaFeatureForGwt.stringFormat(
                        "Position: %s", 
                        music.getPosition()
                        );
            }
            timeLabel.setText(text);
        }
        
        @Override
        public void draw(Batch batch, float parentAlpha) {
            updateTimer();
            super.draw(batch, parentAlpha);
        }
        
        public void updateResource(String data) {
            if (music != null) {
                music.dispose();
            }
            music = Gdx.audio.newMusic(getFile(data));
            playCount = 0;
            music.setOnCompletionListener(onCompletionListener);
        } 
    }
    
    private static class ImageResourceNode extends Image {
        
        private FileHandle getFile(String key) {
            String path = "quiz/pictures/" + key;
            return Gdx.files.internal(path);
        }
        
        public ImageResourceNode() {
            
        }
        
        public void updateResource(String data) {
            this.setDrawable(new TextureRegionDrawable(new Texture(getFile(data))));
        }
    }
}
