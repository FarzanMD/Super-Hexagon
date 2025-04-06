package View;

import Model.Player;

import javax.swing.*;

public class GameSetup extends JFrame {
    public GameSetup() {
        setTitle("Game Setup");
        setSize(300, 200);
        setLocationRelativeTo(null);

        JTextField nameField = new JTextField(15);
        JButton startGame = new JButton("Start Game");

        startGame.addActionListener(e -> {
            String playerName = nameField.getText();
            if (!playerName.isEmpty()) {
                dispose();
                new GameFrame(new Player(playerName));
            }
        });

        JPanel panel = new JPanel();
        panel.add(new JLabel("Enter Name: "));
        panel.add(nameField);
        panel.add(startGame);

        add(panel);
        setVisible(true);
    }
}
