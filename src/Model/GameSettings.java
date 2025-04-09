package Model;

public class GameSettings {
    private static boolean musicEnabled = true;
    private static boolean historyEnabled = true;

    public static boolean isMusicEnabled() {
        return musicEnabled;
    }

    public static void setMusicEnabled(boolean enabled) {
        musicEnabled = enabled;
    }

    public static boolean isHistoryEnabled() {
        return historyEnabled;
    }

    public static void setHistoryEnabled(boolean enabled) {
        historyEnabled = enabled;
    }
}
