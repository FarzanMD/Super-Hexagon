package Model;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class MusicPlayer {
    private static MusicPlayer INSTANCE;
    private static Clip clip;
    static boolean selectStop = false;
    public MusicPlayer() {
        try {
            File audioFile = new File("src/assets/theme.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.CLOSE && !selectStop) {
                    clip.setFramePosition(0); // بازگرداندن موقعیت به ابتدای فایل صوتی
                    clip.start(); // شروع دوباره پخش
                }
            });
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void play() {
        if (clip != null && !clip.isRunning()) {
            clip.start();
            selectStop = false;
        }
    }

    public static void stop() {
        if (clip != null && clip.isRunning()) {
            selectStop = true;
            clip.stop();
        }
    }

    public static MusicPlayer GET_INSTANCE() {
        if (INSTANCE == null) INSTANCE = new MusicPlayer();
        return INSTANCE;
    }
}
