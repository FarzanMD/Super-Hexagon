package View;

import Controler.GamePanel;
import Model.Player;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    public GameFrame(Player player) {
        setTitle("Super Hexagon - Game");
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);



        GamePanel panel = new GamePanel(player);
//        panel.setBackground(Color.magenta);
        add(panel);
        setVisible(true);
        panel.start();
    }
}

