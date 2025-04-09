package View;

import Model.MusicPlayer;

import javax.swing.*;
import java.awt.*;
import static Model.GameSettings.*;

public class SettingsWindow extends JFrame {
    public SettingsWindow() {
        setTitle("Settings");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JCheckBox musicCheckbox = new JCheckBox("Theme Music");
        musicCheckbox.setSelected(isMusicEnabled());

        JCheckBox historyCheckbox = new JCheckBox("Save Game History");
        historyCheckbox.setSelected(isHistoryEnabled());


        musicCheckbox.addActionListener(e -> {
            boolean enabled = musicCheckbox.isSelected();
            setMusicEnabled(enabled);

            if (enabled) {
                MusicPlayer.GET_INSTANCE().play();
            } else {
                MusicPlayer.GET_INSTANCE().stop();
            }
        });
        historyCheckbox.addActionListener(e -> setHistoryEnabled(historyCheckbox.isSelected()));

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 10, 10));
        panel.add(musicCheckbox);
        panel.add(historyCheckbox);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        panel.add(closeButton);

        add(panel);
    }
}
