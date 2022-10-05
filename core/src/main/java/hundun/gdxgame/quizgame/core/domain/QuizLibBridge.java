package hundun.gdxgame.quizgame.core.domain;

import java.util.Arrays;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import hundun.gdxgame.quizgame.core.QuizGdxGame;
import hundun.gdxgame.quizgame.core.domain.QuizRootSaveData.MyGameSaveData;
import hundun.gdxgame.quizgame.core.domain.QuizSaveHandler.ISubGameSaveHandler;
import hundun.gdxgame.share.base.util.JavaFeatureForGwt;
import hundun.quizlib.context.IFrontEnd;
import hundun.quizlib.context.QuizComponentContext;
import hundun.quizlib.exception.QuizgameException;
import hundun.quizlib.service.QuestionLoaderService;
import lombok.Getter;

/**
 * @author hundun
 * Created on 2022/08/30
 */
public class QuizLibBridge implements IFrontEnd, ISubGameSaveHandler{

    @Getter
    QuizComponentContext quizComponentContext;
    @Getter
    LibDataConfiguration libDataConfiguration;
    
    public QuizLibBridge(QuizGdxGame game) {

        try {
            this.quizComponentContext = QuizComponentContext.Factory.create(this);
            this.libDataConfiguration = new LibDataConfiguration(quizComponentContext);
        } catch (QuizgameException e) {
            Gdx.app.error(this.getClass().getSimpleName(), "QuizgameException", e);
        }
        
        game.getSaveHandler().registerSubHandler(this);
    }
    
    
    
    @Override
    public String[] fileGetChilePathNames(String folder) {  
        FileHandle file = Gdx.files.internal(folder);
        FileHandle listFile = file.child(QuestionLoaderService.FOLDER_CHILD_HINT_FILE_NAME);
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



    @Override
    public void applyGameSaveData(MyGameSaveData myGameSaveData) {
        try {
            libDataConfiguration.registerForSaveData(myGameSaveData.getTeamPrototypes());
            Gdx.app.log(this.getClass().getSimpleName(), JavaFeatureForGwt.stringFormat(
                    "applyGameSaveData TeamPrototypes.size = %s", 
                    myGameSaveData.getTeamPrototypes() != null ? myGameSaveData.getTeamPrototypes().size() : null
                    ));
        } catch (QuizgameException e) {
            Gdx.app.error(this.getClass().getSimpleName(), "error", e);
        }
    }



    @Override
    public void currentSituationToSaveData(MyGameSaveData myGameSaveData) {
        myGameSaveData.setTeamPrototypes(quizComponentContext.getTeamService().listTeams());
    }

}
