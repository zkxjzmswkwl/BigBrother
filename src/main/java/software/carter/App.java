package software.carter;

import com.sun.jna.ptr.PointerByReference;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

public class App
{
    private static Rectangle clientRect = new Rectangle(0, 0, 1600, 900);
    private static Rectangle bluePlayers = new Rectangle(0, 120, 1600, 20);
    private static HashMap<String, Mat> heroMap = new HashMap<>();

    public static void main( String[] args )
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        // Future reference: It is VERY important that there is NEVER a process handle to Overwatch.exe
        // being obtained. It is NOT something that is needed and can only lead to potentially bad things.
        // We have no want nor need to interact with the process that, or, really, any level.
        // Window handle (HWND) is not the same thing as a process handle, and is fine to grab.
        // It enables things like repositioning of the window, getting title, etc.
        PointerByReference hwnd = BoofPack.INSTANCE.getHWNDByTitle("Overwatch");
//        BoofPack.INSTANCE.setFgWindow(hwnd);

        // New Windows versions include the shadow of the window in the total window size.
        // Hence the -7 offset.
        BoofPack.INSTANCE.setWindowLoc(hwnd, -7, 0, 1600, 900);

        List<Path> heroSubmats = Computer.walkHeroDir();

        for (Path p : heroSubmats) {
            Mat submat = Imgcodecs.imread(p.getFileName().toAbsolutePath().toString());
            Mat dst = new Mat();

            System.out.println(p.getFileName());
        }

        Mat brigEyes = Imgcodecs.imread("C:\\Users\\Carter\\Downloads\\brig.jpg");
        Mat dst = new Mat();

        Rectangle test = new Rectangle(113, 93, 22, 26);
        BufferedImage brigShit = SRobot.getRobot().createScreenCapture(test);
        Mat convertedMat = Imaging.convertToMat(brigShit);
        Imgproc.matchTemplate(convertedMat, brigEyes, dst, Imgproc.TM_CCOEFF);
        Imaging.writeImageToDisk(Imaging.convertToBufferedImage(dst), "brigtest.jpg");

        Core.MinMaxLocResult mmr = Core.minMaxLoc(dst);
        Point loc = mmr.maxLoc;
        Imgproc.rectangle(convertedMat, loc, new Point(loc.x + brigEyes.cols(),
                loc.y + brigEyes.rows()), new Scalar(0, 255, 0));

        Imaging.writeImageToDisk(Imaging.convertToBufferedImage(convertedMat), "processed.jpg");


        BufferedImage client = SRobot.getRobot().createScreenCapture(clientRect);
        Imaging.writeImageToDisk(client, "client.jpg");

        BufferedImage blue = SRobot.getRobot().createScreenCapture(bluePlayers);
        Imaging.writeImageToDisk(blue, "blue_players.jpg");

//        int startX = 62;
//        for (int i = 0; i <= 11; i++) {
//            if (i != 0)
//                startX += 85;
//            if (i == 5)
//                startX = 1020;
//
//            BufferedImage player = Imaging.cropImage(blue, new Rect(startX, 1, 80, 19));
//            player = Imaging.scaleImage(player, 200,40);
//            player = Imaging.thresh(player, 40, 255, 255, 255, 80, 80, 50);
//            player = Imaging.medianBlur(player, 3);
//
//            String ocrOut = Tess.getInstance().readImage(player);
//            DadeMurphy.shout("Testing", ocrOut);
//
//            Imaging.writeImageToDisk(player, String.valueOf(i) + ".jpg");
//        }
    }
}
