package hundun.gdxgame.quizgame.desktop;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import hundun.quizlib.model.domain.QuestionModel;
import hundun.quizlib.prototype.question.ResourceType;
import hundun.quizlib.service.QuestionLoaderService;

/**
 * @author hundun
 * Created on 2022/09/28
 */
public class OldVersionDataConvertTool {
    
    public static void main(String[] args) throws Exception {
        
        String oldQuestionFolder = "OldVersionDataConvert/old/questions";
        File folder = new File(oldQuestionFolder);
        List<String> newQuestionFileNames = new ArrayList<>();
        for (File file: folder.listFiles()) {
            Path path = Paths.get(file.getPath());
            String fileContent = new String(Files.readAllBytes(path));
            List<QuestionModel> questions = QuestionLoaderService.loadQuestionsFromFile(fileContent, file.getName());
            
            String[] tags = getTags(file.getName());
            String newQuestionFileName = toNewResourceId(file.getName());
            String newQuestionFilePath = "OldVersionDataConvert/new/questions" + File.separator + newQuestionFileName;
            PrintWriter writer = new PrintWriter(newQuestionFilePath, "UTF-8");;
            try {
                convertQuestions(
                        writer,
                        questions, 
                        tags,
                        newQuestionFilePath, 
                        "OldVersionDataConvert/old/pictures", 
                        "OldVersionDataConvert/old/audios", 
                        "OldVersionDataConvert/new/pictures", 
                        "OldVersionDataConvert/new/audios"
                        );
            } catch (Exception e) {
                throw e;
            } finally {
                writer.close();
            }
            
            newQuestionFileNames.add(newQuestionFileName);
            System.out.println(file.getName() + " -> " + newQuestionFileName);
        }
        
        PrintWriter writer = new PrintWriter("OldVersionDataConvert/new/questions/" + QuestionLoaderService.FOLDER_CHILD_HINT_FILE_NAME, "UTF-8");
        newQuestionFileNames.forEach(line -> writer.println(line));
        writer.close();
        
    }
    
    
    private static void convertQuestions(PrintWriter writer, List<QuestionModel> questions, 
            String[] tags, 
            String newQuestionFile, 
            String oldImageFolder,
            String oldAudioFolder,
            String newImageFolder, 
            String newAudioFolder
            ) throws Exception { 
        int size = questions.size();
        
        
        writer.println(size);
        writer.println(Stream.of(tags).collect(Collectors.joining(QuestionLoaderService.TAGS_SPLIT)));
        writer.println();
        
        for (QuestionModel question : questions) {
            String stem = question.getStem();
            String optionA = question.getOptions()[0];
            String optionB = question.getOptions()[1];
            String optionC = question.getOptions()[2];
            String optionD = question.getOptions()[3];
            String answer = question.getAnswerChar();
            String resourceId = question.getResource().getData();
            
            if (question.getResource().getType() != ResourceType.NONE) {
                String newResourceId = toNewResourceId(question.getResource().getData());
                String oldPathName;
                String newPathName;
                if (question.getResource().getType() == ResourceType.IMAGE) {
                    oldPathName = oldImageFolder + File.separator + question.getResource().getData();
                    newPathName = newImageFolder + File.separator + newResourceId;
                } else if (question.getResource().getType() == ResourceType.VOICE) {
                    oldPathName = oldAudioFolder + File.separator + question.getResource().getData();
                    newPathName = newAudioFolder + File.separator + newResourceId;
                } else {
                    throw new UnsupportedOperationException("question.getResource().getType() = " + question.getResource().getType());
                }
                
                
                File oldFile = new File(oldPathName);
                File newFile = new File(newPathName);
                if (!newFile.exists()) {
                    System.out.println(oldFile.getName() + " -> " + newFile.getName());
                    Files.copy(oldFile.toPath(),
                            newFile.toPath(),
                            StandardCopyOption.REPLACE_EXISTING);
                }
                resourceId = newResourceId;
            }

            writer.println(stem);
            writer.println(optionA);
            writer.println(optionB);
            writer.println(optionC);
            writer.println(optionD);
            writer.println(answer);
            if (resourceId != null) {
                writer.println(resourceId);
            } else {
                writer.println("无资源");
            }
            writer.println();
            
            
        }
        
        
    }
    
    private static String[] getTags(String fileName) {
        String[] parts = fileName.split("\\.");
        String[] tags = parts[0].split("_");
        return tags;
    }
    
    private static String toNewResourceId(String data) {
        String[] parts = data.split("\\.");
        String namePart = Stream.of(parts)
                .limit(parts.length - 1)
                .map(str -> {
                    StringBuilder newText = new StringBuilder();
                    for (int i = 0; i < str.length(); i++) {
                        char c = str.charAt(i);
                        newText.append(Integer.toHexString(c));
                    }
                    return newText.toString();
                })
                .collect(Collectors.joining("."));
        return namePart + "." + parts[parts.length - 1];
    }
}

