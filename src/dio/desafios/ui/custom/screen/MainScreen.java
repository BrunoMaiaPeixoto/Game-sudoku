package dio.desafios.ui.custom.screen;

import dio.desafios.model.Space;
import dio.desafios.service.BoardService;
import dio.desafios.service.NotifierService;
import dio.desafios.ui.custom.button.CheckGameStatus;
import dio.desafios.ui.custom.button.FinishGameButton;
import dio.desafios.ui.custom.button.ResetButton;
import dio.desafios.ui.custom.frame.MainFrame;
import dio.desafios.ui.custom.input.NumberText;
import dio.desafios.ui.custom.panel.MainPanel;
import dio.desafios.ui.custom.panel.SudokuSector;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static dio.desafios.service.EventEnum.CLEAR_SPACE;
import static javax.swing.JOptionPane.*;

public class MainScreen {

    private final static Dimension dimension = new Dimension(600,600);

    private final BoardService boardService;
    private final NotifierService notifierService;

    private JButton resetButton;
    private JButton finishGameButton;
    private JButton checkGameButton;

    public MainScreen(final Map<String, String> gameConfig) {
        this.boardService = new BoardService(gameConfig);
        this.notifierService = new NotifierService();
    }

    public void buildMainScreen(){
        JPanel mainPanel = new MainPanel(dimension);
        JFrame mainFrame = new MainFrame(dimension, mainPanel);
        for (int r = 0; r < 9; r+=3) {
            var endRow = r + 2;
            for (int c = 0; c < 9; c+=3) {
                var endCol = c + 2;
                var spaces = getSpacesFromSector(boardService.getSpaces(),c, endCol,r,endRow);
                mainPanel.add(generateSection(spaces));
            };

        }

        addResetButton(mainPanel);
        addCheckGameStatusButton(mainPanel);
        addFinishGame(mainPanel);


        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private List<Space> getSpacesFromSector(List<List<Space>>spaces,
                                            final int initCol, final int endCol,
                                            final int initRow,
                                            final int endRow){
        List<Space> spaceSector = new ArrayList<>();
        for (int r = initRow; r <= endRow ;r++) {
            for (int c = initCol; c <= endCol ;c++) {
                spaceSector.add(spaces.get(c).get(r));
            }
        }
        return spaceSector;
    }


    private JPanel generateSection(final List<Space> spaces){
        List<NumberText> fields = new ArrayList<>(spaces.stream().map(NumberText::new).toList());
        fields.forEach(t -> notifierService.subscriber(CLEAR_SPACE,t));
        return new SudokuSector(fields);
    }


    private void addFinishGame(JPanel mainPanel) {
         finishGameButton = new FinishGameButton(e -> {

            if (boardService.gameIsFinished()) {
                showMessageDialog(null, "Parabéns você completou o jogo");
                resetButton.setEnabled(false);
                checkGameButton.setEnabled(false);
                finishGameButton.setEnabled(false);
            } else {

                showMessageDialog(null, "Seu jogo tem alguma inconsistencia, ajuste e tente novamente");
            }

        });
        mainPanel.add(finishGameButton);
    }

    private void addCheckGameStatusButton(JPanel mainPanel) {
        checkGameButton = new CheckGameStatus(e ->{
            var hasErros = boardService.hasErros();
            var gameStatus = boardService.getStatus();
            var message = switch (gameStatus){
                case NON_STARTED -> "O jogo nçao foi iniciado" ;
                case INCOMPLETE ->"O jogo está incompleto";
                case COMPLETE ->"jogo Completo";
            };
            message += hasErros? " e contém erros" : " e não contem erros";
            showMessageDialog(null, message);
        });
        mainPanel.add(checkGameButton);    }

    private void addResetButton(JPanel mainPanel) {
        resetButton = new ResetButton(e ->{
            var dialogResult = showConfirmDialog(
                    null,
                    "Deseja realmente reiniciar o jogo?",
                    "Limpar o jogo",
                    YES_NO_OPTION,
                    QUESTION_MESSAGE
            );
            if(dialogResult == 0){
            boardService.reset();
            notifierService.notify(CLEAR_SPACE);

            }
        });
        mainPanel.add(resetButton);
    }

}
