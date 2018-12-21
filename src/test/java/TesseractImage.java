import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.*;
import java.io.File;

public class TesseractImage {
    public static void main(String[] args) {
        File imageFile = new File("0001 dec.pdf");
        ITesseract instance = new Tesseract1();
        instance.setLanguage("por");
        instance.setDatapath("tessdata"); // path to tessdata directory
        try {
            String result = instance.doOCR(imageFile);
            System.out.println(result);
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
    }
}
