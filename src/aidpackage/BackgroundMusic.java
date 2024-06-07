package aidpackage;

import java.net.URL;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class BackgroundMusic {
    private static BackgroundMusic instance;
    private MediaPlayer mediaPlayer;

    private BackgroundMusic() {
        URL resource = getClass().getResource("/musicAssets/夜葉罪と罰の螺旋.mp3");
        if (resource != null) {
            Media media = new Media(resource.toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop indefinitely
            mediaPlayer.setVolume(0.1); // Default volume level (50%)
        }
    }

    public static BackgroundMusic getInstance() {
        if (instance == null) {
            instance = new BackgroundMusic();
        }
        return instance;
    }

    public void play() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    public void setVolume(double volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }
    }
}
