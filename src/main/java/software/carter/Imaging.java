package software.carter;

/*
 * --------------------
 * Authored by: Carter
 * Timestamp: 5/19/2022
 * --------------------
 * Edit by: No one, yet.
 * Timestamp: nil
 */

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public final class Imaging {
    // Utility/Helper class
    private Imaging() {}

    /**
     * Crops image based on pos' x, y, width and height fields.
     * @param in
     * @return
     * cropped BufferedImage
     */
    public static BufferedImage cropImage(BufferedImage in, Rect rect) {
        return convertToBufferedImage(convertToMat(in).submat(rect));
    }

    public static Mat convertToMat(BufferedImage image) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", byteArrayOutputStream);
            byteArrayOutputStream.flush();
            return Imgcodecs.imdecode(new MatOfByte(byteArrayOutputStream.toByteArray()), Imgcodecs.IMREAD_UNCHANGED);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static BufferedImage convertToBufferedImage(Mat matrix) {
        try {
            MatOfByte mob = new MatOfByte();
            Imgcodecs.imencode(".jpg", matrix, mob);
            return ImageIO.read(new ByteArrayInputStream(mob.toArray()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void writeImageToDisk(BufferedImage image, String path) {
        try {
            System.out.println("Writing image " + path);
            ImageIO.write(image, "jpg", new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // TODO: Profile this because it's most certainly not performant.
    public static < T > T scaleImage(T image, int width, int height) {
        T ret;
        if (image instanceof BufferedImage bi) {
            ret = (T)convertToMat(bi);
        } else {
            ret = (T)image;
        }

        Size size = new Size(width, height);
        Imgproc.resize((Mat)ret, (Mat)ret, size, 2, 2, Imgproc.INTER_AREA);

        if (image instanceof BufferedImage) {
            return (T)Imaging.convertToBufferedImage((Mat)ret);
        } else {
            return ret;
        }
    }

    public static BufferedImage imageScreen(Rectangle screenRect, boolean useGray, boolean useThreshold) {
        // Capture rectangle of screen using rectangle object we just constructed
        BufferedImage capture = SRobot.getRobot().createScreenCapture(screenRect);
        // Declare & Define new Mat object for any preprocessing/resizing
        Mat flippedBuffer = convertToMat(capture);
        // cvtColor tends to help Tesseract output proper readings
        if (useGray)
            Imgproc.cvtColor(flippedBuffer, flippedBuffer, Imgproc.COLOR_BGR2GRAY);
        else
            Imgproc.cvtColor(flippedBuffer, flippedBuffer, Imgproc.COLOR_BGR2RGB);

        if (useThreshold) {
            Imgproc.threshold(flippedBuffer, flippedBuffer, 0, 255, Imgproc.THRESH_OTSU);
        }

        // medianBlur greatly increases Tesseract's output accuracy
        Imgproc.medianBlur(flippedBuffer, flippedBuffer, 5);

        // Flip the resized Mat back to a BufferedImage and return it to the caller
        return convertToBufferedImage(flippedBuffer);
    }

    public static BufferedImage medianBlur(BufferedImage image, int amount) {
        Mat flippedBuffer = convertToMat(image);
        Imgproc.medianBlur(flippedBuffer, flippedBuffer, amount);
        return convertToBufferedImage(flippedBuffer);
    }

    /**
     * <p>
     *     Applies `Imgproc.blur` to buffer with `Size` of `3,3`.
     *     Calling this is <b>EXPENSIVE,</b> as it must flip the buffer to a `Mat` and back.
     * </p>
     * @param image
     * @return
     */
    public static BufferedImage blur(BufferedImage image) {
        Mat flippedBuffer = convertToMat(image);
        Imgproc.blur(flippedBuffer, flippedBuffer, new Size(3, 3));
        return convertToBufferedImage(flippedBuffer);
    }

    /**
     * Determines whether r, g, and b values are within
     * @param color
     * @param range
     * @param r
     * @param g
     * @param b
     * @return
     */
    public static boolean inColorRange(Color color, int range, int r, int g, int b) {
        return (
                color.getRed()   >= r -  range  &&
                color.getRed()   <= r +  range  &&
                color.getGreen() >= g -  range  &&
                color.getGreen() <= g +  range  &&
                color.getBlue()  >= b -  range  &&
                color.getBlue()  <= b +  range);
    }

    public static BufferedImage customThreshold(BufferedImage image, int range, int... rgb) {
        for (int x = 0; x <= image.getWidth() - 1; x++)
        {
            for (int y = 0; y <= image.getHeight() - 1; y++)
            {
                int pixel = image.getRGB(x, y);
                Color color = new Color(pixel, true);

                // If the current pixel matches, set it to black.
                // If not, set it to green. This produces **great** ocr results.
                if (rgb.length < 5) {
                    if (inColorRange(color, range, rgb[0], rgb[1], rgb[2])) {
                        image.setRGB(x, y, 0x000000);
                    } else {
                        image.setRGB(x, y, 0x00FF00);
                    }
                } else {
                    if (inColorRange(color, range, rgb[0], rgb[1], rgb[2]) ||
                            inColorRange(color, range, rgb[3], rgb[4], rgb[5])) {
                        image.setRGB(x, y, 0x000000);
                    } else {
                        image.setRGB(x, y, 0x00FF00);
                    }
                }
            }
        }
        return image;
    }

    public static Color getColorAt(BufferedImage image, int x, int y) {
        int pixel = image.getRGB(x, y);
        return new Color(pixel, true);
    }
}