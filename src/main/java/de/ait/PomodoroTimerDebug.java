package de.ait;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Andrej Reutow
 * created on 21.07.2023
 */
public class PomodoroTimerDebug {

    private static final int WORK_DURATION = 10; // минуты
    private static final int SHORT_BREAK_DURATION = 5; // минуты
    private static final int LONG_BREAK_DURATION = 15; // минуты

    private static final String BREAK_START_SOUND_FILE = "break-start";
    private static final String BREAK_END_SOUND_FILE = "break-end";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    private static final String SHORT_BRAKE_START = "SHORT-brake-start";
    private static final String SHORT_BRAKE_END = "SHORT-brake-end";

    private static final String LONG_BRAKE_START = "LONG-brake-start";
    private static final String LONG_BRAKE_END = "LONG-brake-end";

    public void start() {


        int loop = 1;
        long currentTime = System.currentTimeMillis() / 1000;
        System.out.println("\nPomodoro loop " + loop);
        while (true) {
            if (loop == 4) {
                currentTime = logging(LONG_BRAKE_START, currentTime);
                alert(BREAK_START_SOUND_FILE);
                waitNextAlert(LONG_BREAK_DURATION);

                currentTime = logging(LONG_BRAKE_END, currentTime);
                alert(BREAK_END_SOUND_FILE);

                loop = 1;
            } else {
                currentTime = logging(SHORT_BRAKE_END, currentTime);
                alert(BREAK_END_SOUND_FILE);
                waitNextAlert(WORK_DURATION);

                currentTime = logging(SHORT_BRAKE_START, currentTime);
                alert(BREAK_START_SOUND_FILE);
                waitNextAlert(SHORT_BREAK_DURATION);

                loop++;
            }
            System.out.println("\nPomodoro loop " + loop);
        }
    }

    private long logging(String pomodoroName, long currentTime) {
        System.out.println(LocalDateTime.now().format(FORMATTER));
        System.out.println("Different: " + ((System.currentTimeMillis() / 1000) - currentTime));
        System.out.println(pomodoroName);
        return System.currentTimeMillis() / 1000;
    }

    private void waitNextAlert(int workDuration) {
        try {
            Thread.sleep(workDuration * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void alert(String fileName) {
        try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(PomodoroTimerDebug.class.getResource("/sound/" + fileName + ".wav"))) {
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
