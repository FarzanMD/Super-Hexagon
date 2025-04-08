package View;

import Model.HistoryRecord;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static Model.HistoryRecord.loadHistory;

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

        exitButton.addActionListener(e -> {
            dispose();
            System.exit(0);
        });

        startButton.addActionListener(e -> {
            dispose();
            new GameSetup();
        });


        historyButton.addActionListener(e -> {
            List<HistoryRecord> records = loadHistory();
            new HistoryWindow(records).setVisible(true);
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
