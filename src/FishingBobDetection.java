import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.awt.image.BufferedImage;
import java.nio.file.Paths;

public class FishingBobDetection {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    // Make bobAbove public or provide a getter method
    public Mat bobAbove;

    public FishingBobDetection() {
        String basePath = Paths.get("").toAbsolutePath().toString();
        bobAbove = Imgcodecs.imread(basePath + "/images/bob_above.png");
    }

    public Point matchTemplate(Mat img, Mat template) {
        int resultCols = img.cols() - template.cols() + 1;
        int resultRows = img.rows() - template.rows() + 1;
        Mat result = new Mat(resultRows, resultCols, CvType.CV_32FC1);

        Imgproc.matchTemplate(img, template, result, Imgproc.TM_CCOEFF_NORMED);
        Core.MinMaxLocResult mmr = Core.minMaxLoc(result);
        Point matchLoc = mmr.maxLoc;

        System.out.println("Template matching result: maxVal = " + mmr.maxVal);

        if (mmr.maxVal >= 0.42) { // Adjust the threshold as necessary
            return matchLoc;
        }
        return null;
    }

    public boolean isBobAboveWater(Mat gameScreen) {
        return matchTemplate(gameScreen, bobAbove) != null;
    }

    public static Mat bufferedImageToMat(BufferedImage bi) {
        Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
        for (int y = 0; y < bi.getHeight(); y++) {
            for (int x = 0; x < bi.getWidth(); x++) {
                int rgb = bi.getRGB(x, y);
                byte[] data = new byte[]{
                        (byte) (rgb & 0xFF),
                        (byte) ((rgb >> 8) & 0xFF),
                        (byte) ((rgb >> 16) & 0xFF)
                };
                mat.put(y, x, data);
            }
        }
        return mat;
    }
}
