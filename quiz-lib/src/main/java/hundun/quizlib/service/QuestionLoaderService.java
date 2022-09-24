package hundun.quizlib.service;

import java.io.File;
//import java.io.File;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import hundun.quizlib.context.IFrontEnd;
import hundun.quizlib.context.IQuizComponent;
import hundun.quizlib.context.QuizComponentContext;
import hundun.quizlib.exception.QuestionFormatException;
import hundun.quizlib.model.domain.QuestionModel;

public class QuestionLoaderService implements IQuizComponent {
	
//    public File RESOURCE_ICON_FOLDER;
//	private File PACKAGES_FOLDER;
    public static String PACKAGE_FOLDER = "quiz/question_packages/";
	public static String BUSINESS_PACKAGE_NAME = "questions";
	public static String PRELEASE_PACKAGE_NAME = "questions_small";
	public static String TEST_PACKAGE_NAME = "questions_test";
	public static String TEST_SMALL_PACKAGE_NAME = "questions_test_small";
	
	public static final String TAGS_SPLIT = ";";
	
	IFrontEnd frontEnd;
	
	@Override
    public void postConstruct(QuizComponentContext context) {
        this.frontEnd = context.getFrontEnd();
    }
	
//	public void lateInitFolder(File PACKAGES_FOLDER, File RESOURCE_ICON_FOLDER) {
//        this.PACKAGES_FOLDER = PACKAGES_FOLDER;
//        this.RESOURCE_ICON_FOLDER = RESOURCE_ICON_FOLDER;
//    }
	
	public static List<QuestionModel> loadQuestionsFromFile(String fileContent, String fileName) throws QuestionFormatException {
        List<String> lines = Arrays.asList(fileContent.split("\r?\n|\r"));  
        try {
        	return parseTextToQuestions(lines);
		} catch (QuestionFormatException e) {
			throw new QuestionFormatException(e, fileName);
		}
	}
	
//	/**
//	 * 将指定文件夹中所有文件的文件名进行指定替换的重命名。
//	 * @param folderPath
//	 * @param regex
//	 * @param replacement
//	 * @return
//	 */
//	public String replaceFileNamesInFolder(String folderPath, String regex, String replacement){
//		int renameNum = 0;
//		File folder = new File(folderPath);
//		File[] files = folder.listFiles();
//		for (File file:files) {
//			if(!file.isDirectory()) {
//				String newName = file.getName().replaceFirst(regex, replacement);
//				File newFile = new File(file.getParentFile(), newName);
//				file.renameTo(newFile);
//				renameNum++;
//			}
//		}
//		return "文件夹共有：" + files.length + "个文件,重命名了" + renameNum + "个文件。";
//	}
	
//	public String obfuscateFolder(String originFolderPath, String outputFolderPath, Set<String> parentTagNames) {
//	    String error = "";
//	    
//	    File originFolder = new File(originFolderPath);
//	    File outputFolder = new File(outputFolderPath);
//	    if (!outputFolder.exists()) {
//	        outputFolder.mkdir();
//        }
//	    
//	    File[] files = originFolder.listFiles();
//        for (File file:files) {
//            String thisTagName;
//            Set<String> newParentTagNames = new HashSet<>(parentTagNames);
//            String outputFilePath = outputFolder.getPath() + File.separator + file.getName();
//           
//            if(file.isDirectory()) {
//                thisTagName = file.getName();
//                newParentTagNames.add(thisTagName);
//                
//                File nestOriginFolder = file;
//                File nestOutputFolder = new File(outputFilePath);
//                if (!nestOutputFolder.exists()) {
//                    nestOutputFolder.mkdir();
//                }
//                error += obfuscateFolder(nestOriginFolder.getPath(), nestOutputFolder.getPath(), newParentTagNames);
//            } else {
//                thisTagName = file.getName().substring(0, file.getName().indexOf("."));
//                newParentTagNames.add(thisTagName);
//                List<Question> questions;
//                try {
//                    questions = loadQuestionsFromFile(file, newParentTagNames);
//                    parseQuestionsToFile(questions, outputFilePath, true);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    error += outputFilePath + " error:" + e.getMessage() + ";";
//                    continue;
//                }
//            }
//        }
//        return error;
//    }
	
	public List<QuestionModel> loadAllQuestions(String packageName) throws QuestionFormatException {
		
		String[] chileNames = frontEnd.fileGetChilePathNames(PACKAGE_FOLDER + packageName);
		List<QuestionModel> questions = new ArrayList<>();
		
		for (String chileName : chileNames) {
			String fileContent = frontEnd.fileGetContent(PACKAGE_FOLDER + packageName + File.separator + chileName);
			questions.addAll(loadQuestionsFromFile(fileContent, chileName));
		}
		return questions;
	}
	
//	public static String mergeTags(String tags1, String tags2) {
//		if (tags1.length() == 0) {
//			return tags2;
//		} else {
//			return tags1 + ";" + tags2;
//		}
//	}
	
	
	// FEFF because this is the Unicode char represented by the UTF-8 byte order mark (EF BB BF).
    public static final String UTF8_BOM = "\uFEFF";
	/**
	 * 每一题后通过至少一个空白行分割；最后一题后可以无空行
	 * @param lines
	 * @param singleTagName
	 * @return
	 * @throws QuestionFormatException
	 */
	private static List<QuestionModel> parseTextToQuestions(List<String> lines) throws QuestionFormatException { 
	    
		int currentLine = 0;
		
	    String numText = lines.get(currentLine++);
		if (numText.startsWith(UTF8_BOM)) {
		    numText = numText.substring(1);
		}
		
		String tagsText = lines.get(currentLine++);
		Set<String> tagNames;
		if (tagsText.trim().length() != 0) {
		    tagNames = Stream.of(tagsText.split(TAGS_SPLIT))
	                .map(it -> it.trim())
	                .collect(Collectors.toSet())
	                ;
		    currentLine++;
		} else {
		    tagNames = new HashSet<>(0);
		}
		
		
		int num = Integer.valueOf(numText);
		List<QuestionModel> questions = new ArrayList<>(num);
		
		while (currentLine < lines.size()) {
			int i0 = currentLine;
			try {
				
				String stem = lines.get(currentLine++);
				String optionA = lines.get(currentLine++);
				String optionB = lines.get(currentLine++);
				String optionC = lines.get(currentLine++);
				String optionD = lines.get(currentLine++);
				String answer = lines.get(currentLine++);
				String resourceText = lines.get(currentLine++);
				
				while (currentLine < lines.size()) {
				    String next = lines.get(currentLine);
				    if (next.trim().length() == 0) {
				        currentLine++;
				    } else {
				        break;
				    }
				}
				
				boolean elementLost = stem.length() == 0 
						|| optionA.length() == 0
						|| optionB.length() == 0
						|| optionC.length() == 0
						|| optionD.length() == 0
						|| answer.length() == 0
						|| resourceText.length() == 0
						;
				if(elementLost) {
					throw new QuestionFormatException(i0, currentLine, questions.size() + 1, "题目组成");
				}
				
				QuestionModel question = new QuestionModel(stem, optionA, optionB, optionC, optionD, answer, resourceText, tagNames);
				
//				if (question.getResource().getData() != null) {
//				    String filePathName = RESOURCE_ICON_FOLDER.getAbsolutePath() + File.separator + question.getResource().getData();
//			        File file = new File(filePathName);
//			        if (!file.exists()) {
//			            question.getResource().setType(ResourceType.IMAGE);
//			            question.getResource().setData("default.png");
//			        }
//				}
				
				questions.add(question);
			} catch (IndexOutOfBoundsException e) {
				throw new QuestionFormatException(currentLine, currentLine, questions.size() + 1, "下标不应越界");
			}
			
		}
		return questions;
	}
	
//	private static void parseQuestionsToFile(List<QuestionModel> questions, String filePath, boolean obfuscate) throws Exception  { 
//	    int size = questions.size();
//    	PrintWriter writer = new PrintWriter(filePath, "UTF-8");
//    	
//        writer.println(size);
//        writer.println();
//        for (QuestionModel question : questions) {
//            String stem = obfuscate?obfuscate(question.getStem()):question.getStem();
//            String optionA = obfuscate?obfuscate(question.getOptions()[0]):question.getOptions()[0];
//            String optionB = obfuscate?obfuscate(question.getOptions()[1]):question.getOptions()[1];
//            String optionC = obfuscate?obfuscate(question.getOptions()[2]):question.getOptions()[2];
//            String optionD = obfuscate?obfuscate(question.getOptions()[3]):question.getOptions()[3];
//            String answer = question.getAnswerChar();
//            String resourceText = question.getResource().getData();
//            
//            if (obfuscate) {
//                stem = question.getTags() + " " + stem + " " + answer;
//            }
//            
//            writer.println(stem);
//            writer.println(optionA);
//            writer.println(optionB);
//            writer.println(optionC);
//            writer.println(optionD);
//            writer.println(answer);
//            writer.println(resourceText);
//            writer.println();
//        }
//        writer.close();
//    }
//	
//	/**
//	 * 混淆一个字符串的内容
//	 * @param origin
//	 * @return
//	 */
//	private static String obfuscate(String origin) {
//        return String.valueOf(origin.hashCode());
//    }

    
	
	
}
