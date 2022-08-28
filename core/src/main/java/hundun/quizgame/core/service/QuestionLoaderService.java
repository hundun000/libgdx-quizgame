package hundun.quizgame.core.service;

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
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import hundun.quizgame.core.context.IFrontEnd;
import hundun.quizgame.core.context.IQuizComponent;
import hundun.quizgame.core.context.QuizComponentContext;
import hundun.quizgame.core.exception.QuestionFormatException;
import hundun.quizgame.core.exception.QuizgameException;
import hundun.quizgame.core.model.domain.QuestionModel;
import hundun.quizgame.core.prototype.question.ResourceType;

public class QuestionLoaderService implements IQuizComponent {
	
//    public File RESOURCE_ICON_FOLDER;
//	private File PACKAGES_FOLDER;
    public static String PACKAGE_FOLDER = "quiz/question_packages/";
	public static String BUSINESS_PACKAGE_NAME = "questions";
	public static String PRELEASE_PACKAGE_NAME = "questions_small";
	public static String TEST_PACKAGE_NAME = "questions_test";
	public static String TEST_SMALL_PACKAGE_NAME = "questions_test_small";
	
	IFrontEnd frontEnd;
	
	@Override
    public void postConstruct(QuizComponentContext context) {
        this.frontEnd = context.getFrontEnd();
    }
	
//	public void lateInitFolder(File PACKAGES_FOLDER, File RESOURCE_ICON_FOLDER) {
//        this.PACKAGES_FOLDER = PACKAGES_FOLDER;
//        this.RESOURCE_ICON_FOLDER = RESOURCE_ICON_FOLDER;
//    }
	
	private List<QuestionModel> loadQuestionsFromFile(String fileContent, String fileName, Set<String> tagNames) throws QuestionFormatException {
        List<String> lines = Arrays.asList(fileContent.split("\r?\n|\r"));  
        try {
        	return parseTextToQuestions(lines, tagNames);
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
			Set<String> tagNames = new HashSet<>();
			// TODO 计算TagNames
			String fileContent = frontEnd.fileGetContent(PACKAGE_FOLDER + packageName + File.separator + chileName);
			questions.addAll(loadQuestionsFromFile(fileContent, chileName, tagNames));
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
	private List<QuestionModel> parseTextToQuestions(List<String> lines, Set<String> tagNames) throws QuestionFormatException { int size = lines.size();
		String numText = lines.get(0);
		
		if (numText.startsWith(UTF8_BOM)) {
		    numText = numText.substring(1);
		}
		int num = Integer.valueOf(numText);
		List<QuestionModel> questions = new ArrayList<>(num);
		
		for (int i = 2; i < size; ) {
			int i0 = i;
			try {
				
				String stem = lines.get(i++);
				String optionA = lines.get(i++);
				String optionB = lines.get(i++);
				String optionC = lines.get(i++);
				String optionD = lines.get(i++);
				String answer = lines.get(i++);
				String resourceText = lines.get(i++);
				
				boolean elementLost = stem.length() == 0 
						|| optionA.length() == 0
						|| optionB.length() == 0
						|| optionC.length() == 0
						|| optionD.length() == 0
						|| answer.length() == 0
						|| resourceText.length() == 0
						;
				if(elementLost) {
					throw new QuestionFormatException(i0, i+1, questions.size() + 1, "题目组成");
				}
				
				int numBlankLine;
				for (numBlankLine = 0; i + numBlankLine < size; numBlankLine++) {
					String line = lines.get(i + numBlankLine);
					if (line.length() != 0) {
						break;
					}
				}
				
				if (numBlankLine == 0 && i + numBlankLine < size) {
					throw new QuestionFormatException(i + 1, i + 1, questions.size() + 1, "空行");
				}
				i += numBlankLine;
				
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
				throw new QuestionFormatException(i + 1, i + 1, questions.size() + 1, "题目组成");
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
