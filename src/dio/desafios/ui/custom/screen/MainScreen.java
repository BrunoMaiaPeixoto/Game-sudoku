package dio.desafios.ui.custom.screen;

import dio.desafios.service.BoardService;
import dio.desafios.ui.custom.button.FinishGameButton;
import dio.desafios.ui.custom.button.ResetButton;
import dio.desafios.ui.custom.frame.MainFrame;
import dio.desafios.ui.custom.panel.MainPanel;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

import static javax.swing.JOptionPane.QUESTION_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;

public class MainScreen {

    private final static Dimension dimension = new Dimension(600,600);

    private final BoardService boardService;

    private JButton resetButton;
    private JButton finishGameButton;
    private JButton checkGameButton;

    public MainScreen(final Map<String, String> gameConfig) {
        this.boardService = new BoardService(gameConfig);
    }

    public void buildMainScreen(){
        JPanel mainPanel = new MainPanel(dimension);
        JFrame mainFrame = new MainFrame(dimension, mainPanel);
        addResetButton(mainPanel);
        addCheckGameStatusButton(mainPanel);
        addFinishGame(mainPanel);


        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private void addFinishGame(JPanel mainPanel) {
         finishGameButton = new FinishGameButton(e -> {

            if (boardService.gameIsFinished()) {
                JOptionPane.showMessageDialog(null, "Parabéns você completou o jogo");
                resetButton.setEnabled(false);
                checkGameButton.setEnabled(false);
                finishGameButton.setEnabled(false);
            } else {

                JOptionPane.showMessageDialog(null, "Seu jogo tem alguma inconsistencia, ajuste e tente novamente");
            }

        });
        mainPanel.add(finishGameButton);
    }

    private void addCheckGameStatusButton(JPanel mainPanel) {
        checkGameButton = new FinishGameButton(e ->{
            var hasErros = boardService.hasErros();
            var gameStatus = boardService.getStatus();
            var message = switch (gameStatus){
                case NON_STARTED -> "O jogo nçao foi iniciado" ;
                case INCOMPLETE ->"O jogo está incompleto";
                case COMPLETE ->"jogo Completo";
            };
            message += hasErros? " e contém erros" : " e não contem erros";
            JOptionPane.showMessageDialog(null, message);
        });
        mainPanel.add(checkGameButton);    }

    private void addResetButton(JPanel mainPanel) {
        resetButton = new ResetButton(e ->{
            var dialogResult = JOptionPane.showConfirmDialog(
                    null,
                    "Deseja realmente reiniciar o jogo?",
                    "Limpar o jogo",
                    YES_NO_OPTION,
                    QUESTION_MESSAGE
            );
            if(dialogResult == 0){
            boardService.reset();

            }
        });
        mainPanel.add(resetButton);
    }

}
