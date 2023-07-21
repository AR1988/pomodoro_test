package de.ait;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.time.format.DateTimeFormatter;

/**
 * @author Andrej Reutow
 * created on 21.07.2023
 */
public class PomodoroTimer {

    private static final int WORK_DURATION = 25; // минуты
    private static final int SHORT_BREAK_DURATION = 5; // минуты
    private static final int LONG_BREAK_DURATION = 20; // минуты
    private static final String BREAK_START_SOUND_FILE = "break-start";
    private static final String BREAK_END_SOUND_FILE = "break-end";

    public void start() {
        int loop = 0;

        while (true) {
            if (loop == 4) {
                alert(BREAK_START_SOUND_FILE);
                waitNextAlert(LONG_BREAK_DURATION);
                loop = 0;
            } else {
                alert(BREAK_END_SOUND_FILE);
                waitNextAlert(WORK_DURATION);
                alert(BREAK_START_SOUND_FILE);
                waitNextAlert(SHORT_BREAK_DURATION);
                loop++;
            }
        }
    }

    private void waitNextAlert(int workDuration) {
        try {
            Thread.sleep(workDuration * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void alert(String fileName) {
        try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(PomodoroTimer.class.getResource("/sound/" + fileName + ".wav"))) {
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
