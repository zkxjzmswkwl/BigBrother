package software.carter;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import java.awt.image.BufferedImage;

// Singleton
public class Tess
{
    private static Tess tess;
    private static Tesseract tesseract;

    static {
        tess = new Tess();
        tesseract = new Tesseract();
        tesseract.setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata");
        tesseract.setTessVariable("user_defined_dpi", "70");
        tesseract.setTessVariable("psm", "7");
    }

    public static Tess getInstance() {
        return tess;
    }

    public void setTesseractVar(String key, String value) {
        tesseract.setTessVariable(key, value);
    }

    public String readSingleLine(BufferedImage input) {
        // Tess.getInstance().setTesseractVar("psm", "7");
        // Tess.getInstance().setTesseractVar("psm", "3");
        String ret = this.readImage(input);
        // Tess.getInstance().setTesseractVar("psm", "3");
        // Tess.getInstance().setTesseractVar("psm", "8");
        return ret.toLowerCase();
    }

    public String readImage(BufferedImage input) {
        try {
            return tesseract.doOCR(input);
        } catch (TesseractException e) {
            e.printStackTrace();
            return null;
        }
    }
}
