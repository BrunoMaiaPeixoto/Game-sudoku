package dio.desafios;

import dio.desafios.ui.custom.frame.MainFrame;
import dio.desafios.ui.custom.panel.MainPanel;
import dio.desafios.ui.custom.screen.MainScreen;

import javax.swing.*;
import java.awt.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UIMain {

    public static void main(String[] args) {
        final var gameConfig = Stream.of(args)
       .collect(Collectors.toMap(k -> k.split(";")[0], v -> v.split(";")[1]));


        var mainsScreen = new MainScreen(gameConfig);
        mainsScreen.buildMainScreen();
}
}
