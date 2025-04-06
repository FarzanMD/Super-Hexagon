package View;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {
    public MainMenu() {
        setTitle("Super Hexagon");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton startButton = new JButton("Start New Game");
        JButton historyButton = new JButton("Game History");
        JButton settingsButton = new JButton("Settings");
        JButton exitButton = new JButton("Exit");

        startButton.addActionListener(e -> {
            dispose();
            new GameSetup();
        });

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.add(startButton);
        panel.add(historyButton);
        panel.add(settingsButton);
        panel.add(exitButton);

        add(panel);
        setVisible(true);
    }
}
