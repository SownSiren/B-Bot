import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class FishingBot {
    public static void main(String[] args) {
        try {
            FishingBobDetection detection = new FishingBobDetection();
            Robot robot = new Robot();
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            String basePath = Paths.get("").toAbsolutePath().toString();

            int captureCounter = 0;
            boolean casted = false; // Flag to track if we have casted

            System.out.println("Giving time to enter minecraft...");
            TimeUnit.MILLISECONDS.sleep(2000);

            while (true) {
                // Capture the screen
                BufferedImage screenImage = robot.createScreenCapture(screenRect);
                Mat gameScreen = FishingBobDetection.bufferedImageToMat(screenImage);

                // Save captured screen for debugging
                // Imgcodecs.imwrite(basePath + "/debug/screen" + captureCounter + ".png", gameScreen);

                // Detect bob state
                boolean isBobAboveWater = detection.isBobAboveWater(gameScreen);
                Point matchLoc = detection.matchTemplate(gameScreen, detection.bobAbove);

                if (isBobAboveWater) {
                    System.out.println("Bob is above water at location: " + matchLoc);
                    // System.out.println("Current Capture Counter: " + captureCounter);
                    // make a boolean for capture rate so we dont do it just on castsw

                } else {

                    System.out.println("Bob not found");

                    robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
                    robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
                    System.out.println("Casting...");

                    // Wait for a moment after casting before checking again
                    TimeUnit.MILLISECONDS.sleep(2500); // Adjust the delay as needed
                }

                // Increment capture counter
                //captureCounter++;

                // Add a short delay before the next check
                TimeUnit.MILLISECONDS.sleep(200); // Adjust the delay as needed
            }
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }
}
