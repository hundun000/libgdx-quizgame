package hundun.quizgame.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import hundun.quizlib.context.IFrontEnd;
import hundun.quizlib.context.QuizComponentContext;
import hundun.quizlib.exception.QuizgameException;
import hundun.quizlib.model.domain.QuestionModel;
import hundun.quizlib.prototype.match.ClientActionType;
import hundun.quizlib.prototype.match.MatchConfig;
import hundun.quizlib.prototype.match.MatchStrategyType;
import hundun.quizlib.service.BuiltinDataConfiguration;
import hundun.quizlib.service.GameService;
import hundun.quizlib.service.QuestionLoaderService;
import hundun.quizlib.service.TeamService;
import hundun.quizlib.view.match.MatchSituationView;

import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.JLabel;

/**
 * @author hundun
 * Created on 2019/09/11
 */
public class MyFrame extends JFrame implements ISecondEventReceiver, IFrontEnd {

    GameService gameService;
    
    String matchId;
    MatchSituationView matchSituationView;
    
    private JPanel contentPane;
    
    private JTextArea matchSituationOutput;
    private JTextArea questionTextArea; 
    private JTextArea teamTextArea; 
    private JButton createAndStartButton;
    private JButton nextQuestionButton;
    private JButton answerAButton;
    private JButton answerBButton;
    private JButton answerCButton;
    private JButton answerDButton;
    JLabel lblTime;
    
    int timerCount;
    Timer secondTimer;
    //boolean ignoreSecondTimer;

    public MyFrame() {
        
    }
    
    /**
     * Create the frame.
     * @param match2 
     * @param teamService2 
     * @param questionService2 
     * @throws Exception 
     */
    public void lazyInit(QuizComponentContext context) {
        this.gameService = context.getGameService();
        
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 900);
        
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        matchSituationOutput = new JTextArea();
        matchSituationOutput.setBounds(452, 25, 347, 627);
        matchSituationOutput.setLineWrap(true);
        contentPane.add(matchSituationOutput);
        
        questionTextArea = new JTextArea();
        questionTextArea.setBounds(10, 50, 400, 200);
        questionTextArea.setLineWrap(true);
        contentPane.add(questionTextArea);
        
        teamTextArea = new JTextArea();
        teamTextArea.setBounds(10, 300, 400, 200);
        teamTextArea.setLineWrap(true);
        contentPane.add(teamTextArea);
        
        createAndStartButton = new JButton("createAndStart");
        createAndStartButton.setBounds(10, 660, 100, 29);
        createAndStartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    createAndStartMatch();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        contentPane.add(createAndStartButton);
        
        nextQuestionButton = new JButton("nextQuestion");
        nextQuestionButton.setBounds(120, 660, 100, 29);
        nextQuestionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    matchSituationView = gameService.nextQustion(matchId);
                } catch (QuizgameException e1) {
                    e1.printStackTrace();
                }
                updateByNewMatchSituation();
            }
        });
        contentPane.add(nextQuestionButton);
        
        answerAButton = new JButton("A");
        answerAButton.setBounds(10, 690, 50, 29);
        answerAButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    matchSituationView = gameService.teamAnswer(matchId, "A");
                    updateByNewMatchSituation();
                } catch (QuizgameException e1) {
                    e1.printStackTrace();
                }
            }
        });
        contentPane.add(answerAButton);

        answerBButton = new JButton("B");
        answerBButton.setBounds(70, 690, 50, 29);
        answerBButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    matchSituationView = gameService.teamAnswer(matchId, "B");
                    updateByNewMatchSituation();
                } catch (QuizgameException e1) {
                    e1.printStackTrace();
                }
            }
        });
        contentPane.add(answerBButton);
        
        answerCButton = new JButton("C");
        answerCButton.setBounds(130, 690, 50, 29);
        answerCButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    matchSituationView = gameService.teamAnswer(matchId, "C");
                    updateByNewMatchSituation();
                } catch (QuizgameException e1) {
                    e1.printStackTrace();
                }
            }
        });
        contentPane.add(answerCButton);
        
        answerDButton = new JButton("D");
        answerDButton.setBounds(190, 690, 50, 29);
        answerDButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    matchSituationView = gameService.teamAnswer(matchId, "D");
                    updateByNewMatchSituation();
                } catch (QuizgameException e1) {
                    e1.printStackTrace();
                }
            }
        });
        contentPane.add(answerDButton);
        
        
        
        lblTime = new JLabel("Time:");
        lblTime.setBounds(22, 23, 108, 29);
        contentPane.add(lblTime);
        
        secondTimer = new Timer();
        secondTimer.schedule(new SecondTimerTask(this), 1000, 1000);
        
        
    }
    
    private void createAndStartMatch() throws Exception {
        MatchConfig matchConfig = new MatchConfig();
        matchConfig.setTeamNames(Arrays.asList(BuiltinDataConfiguration.DEMO_LIST_TEAM_NAME_0));
        matchConfig.setQuestionPackageName(QuestionLoaderService.PRELEASE_PACKAGE_NAME);
        matchConfig.setMatchStrategyType(MatchStrategyType.ENDLESS);
        
        matchSituationView = gameService.createMatch(matchConfig);
        this.matchId = matchSituationView.getId();
        
        matchSituationView = gameService.startMatch(matchId);
        
        updateByNewMatchSituation();
    }
    
    private void updateByNewMatchSituation() {
        
        matchSituationOutput.setText(matchSituationView.toString());
        
        if (matchSituationView.getQuestion() != null) {
            questionTextArea.setText(matchSituationView.getQuestion().toString());
        }
        
        
        teamTextArea.setText(matchSituationView.getCurrentTeamRuntimeInfo().toString());

        
        if (matchSituationView.getSwitchQuestionEvent() != null) {
            int newTimeCount = matchSituationView.getSwitchQuestionEvent().getTime();

            timerCount = newTimeCount;
            lblTime.setText(String.valueOf(timerCount));
        }
        
        if (matchSituationView.getAnswerResultEvent() != null) {
            timerCount = 0;
            lblTime.setText(String.valueOf(timerCount));
        }
        
        if (matchSituationView.getActionAdvices().contains(ClientActionType.ANSWER)) {
            answerAButton.setEnabled(true);
            answerBButton.setEnabled(true);
            answerCButton.setEnabled(true);
            answerDButton.setEnabled(true);
        } else {
            answerAButton.setEnabled(false);
            answerBButton.setEnabled(false);
            answerCButton.setEnabled(false);
            answerDButton.setEnabled(false);
        }
        
        if (matchSituationView.getActionAdvices().contains(ClientActionType.NEXT_QUESTION)) {
            nextQuestionButton.setEnabled(true);
        } else {
            nextQuestionButton.setEnabled(false);
        }
        
        if (matchSituationView.getActionAdvices().contains(ClientActionType.START_MATCH)) {
            createAndStartButton.setEnabled(true);
        } else {
            createAndStartButton.setEnabled(false);
        }
    }


    @Override
    public void whenReceiveSecondClock() {

            if (timerCount > 0) {
                lblTime.setForeground(Color.BLACK);
                timerCount -= 1;
                lblTime.setText(String.valueOf(timerCount));
                
                if (timerCount == 0) {
                    lblTime.setForeground(Color.RED);
                    try {
                        matchSituationView = gameService.teamAnswer(matchId, QuestionModel.TIMEOUT_ANSWER_TEXT);
                    } catch (QuizgameException e) {
                        e.printStackTrace();
                    }
                    updateByNewMatchSituation();
                }
            } else {
                lblTime.setForeground(Color.BLUE);
            }
    }

    @Override
    public String[] fileGetChilePathNames(String folderName) {
        String actualPath = "../assets/" + folderName;
        File folder = new File(actualPath);
        System.out.println("fileGetChilePathNames folder " + folder.getAbsolutePath() + ", exist = " + folder.exists());
        String[] result = folder.list();
        System.out.println("fileGetChilePathNames result = " + Arrays.toString(result));
        return result;
    }

    @Override
    public String fileGetContent(String fileName) {
        String actualPath = "../assets/" + fileName;
        File file = new File(actualPath);
        Path path = Paths.get(file.getPath());
        try {
            String result = Files.readString(path);
            System.out.println("fileGetContent result.length = " + result.length());
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }  
    }
    

    

}
