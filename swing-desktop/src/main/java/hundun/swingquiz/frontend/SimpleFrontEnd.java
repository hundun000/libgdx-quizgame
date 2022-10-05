package hundun.swingquiz.frontend;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import hundun.quizlib.context.IFrontEnd;
import hundun.quizlib.exception.QuizgameException;
import hundun.quizlib.model.domain.QuestionModel;
import hundun.quizlib.service.QuestionLoaderService;

/**
 * @author hundun
 * Created on 2022/10/21
 */
public class SimpleFrontEnd implements IFrontEnd {


    @Override
    public String[] fileGetChilePathNames(String folderName) {
        String actualPath = "../assets/" + folderName;
        File folder = new File(actualPath);
        System.out.println("fileGetChilePathNames folder " + folder.getAbsolutePath() + ", exist = " + folder.exists());
        String[] result = Stream.of(folder.list())
            .filter(it -> !it.equals(QuestionLoaderService.FOLDER_CHILD_HINT_FILE_NAME))
            .collect(Collectors.toList())
            .toArray(new String[0]);
        System.out.println("fileGetChilePathNames result = " + Arrays.toString(result));
        return result;
    }

    @Override
    public String fileGetContent(String fileName) {
        String actualPath = "../assets/" + fileName;
        File file = new File(actualPath);
        Path path = Paths.get(file.getPath());
        try {
            String result = new String(Files.readAllBytes(path));
            System.out.println("fileGetContent result.length = " + result.length());
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }  
    }
}
