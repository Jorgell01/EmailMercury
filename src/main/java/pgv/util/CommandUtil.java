package pgv.util;

import java.io.IOException;

public class CommandUtil {

    public static void restartMercuryService() {
        new Thread(() -> {
            try {
                // Stop Mercury service
                ProcessBuilder stopBuilder = new ProcessBuilder("cmd", "/c", "taskkill /F /IM mercury.exe");
                Process stopProcess = stopBuilder.start();
                stopProcess.waitFor();

                // Start Mercury service
                ProcessBuilder startBuilder = new ProcessBuilder("cmd", "/c", "C:\\xampp\\MercuryMail\\mercury.exe");
                Process startProcess = startBuilder.start();
                startProcess.waitFor();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}