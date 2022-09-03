package hundun.gdxgame.quizgame.core.domain;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import hundun.quizlib.context.IFrontEnd;
import hundun.quizlib.context.QuizComponentContext;
import hundun.quizlib.exception.QuizgameException;
import hundun.quizlib.service.GameService;
import lombok.Getter;

/**
 * @author hundun
 * Created on 2022/08/30
 */
public class QuizLibBridge implements IFrontEnd {

    @Getter
    QuizComponentContext quizComponentContext;
    
    public QuizLibBridge() {

        try {
            this.quizComponentContext = QuizComponentContext.Factory.create(this);
        } catch (QuizgameException e) {
            Gdx.app.error(this.getClass().getSimpleName(), e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }
    
    
    
    @Override
    public String[] fileGetChilePathNames(String folder) {  
        FileHandle file = Gdx.files.internal(folder);
        FileHandle listFile = file.child("list.txt");
        String listContent = fileGetContent(listFile.path());
        String[] result = listContent.split("\r?\n|\r");
        Gdx.app.log(this.getClass().getSimpleName(), "fileGetChilePathNames result = " + Arrays.toString(result));
        return result;
    }

    @Override
    public String fileGetContent(String filePath) {
        FileHandle file = Gdx.files.internal(filePath);
        String result = file.readString();
        Gdx.app.log(this.getClass().getSimpleName(), "fileGetContent result.length = " + result.length());
        return result;
    }

}
