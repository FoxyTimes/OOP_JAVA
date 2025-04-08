package ru.minesweeper.view.gui.sounds;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class PlaySounds {
    Clip clip;
    boolean isOn = true;

    public void setFile(String path) {
        try {
            // Закрываем предыдущий clip
            if (clip != null) {
                clip.close();
            }

            // Читаем весь файл в память
            byte[] audioData = readFully(path);

            try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(
                    new ByteArrayInputStream(audioData))) {
                clip = AudioSystem.getClip();
                clip.open(audioStream);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error loading sound: " + path, e);
        }
    }

    private byte[] readFully(String path) throws IOException {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is == null) {
                throw new IOException("Sound file not found: " + path);
            }
            return is.readAllBytes();
        }
    }

    public void play() {
        if (clip != null&&isOn) {
            clip.setFramePosition(0);
            clip.start();
        }
    }

    public void loop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
        }
    }

    public void toggleBase() {
        isOn = !isOn;
    }

    public void toggleLoop() {
        if (clip != null) {
            if (clip.isRunning()) {
                stop();
            }
            else {
                loop();
            }
        }
    }

}