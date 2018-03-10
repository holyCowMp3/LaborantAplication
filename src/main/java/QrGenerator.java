import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * This class create {@link WritableImage} with QR which include link to the calendar
 */
public class QrGenerator {
/**Generate QRCode for the table in the laborant App;
 * @param myWeb - string with the ID of calendar
 * @return WritableImage with QR*/
public static WritableImage generateQr(String myWeb) {
    try {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        int width = 300;
        int height = 300;
        myWeb = "https://calendar.google.com/calendar/embed?src=" + myWeb.split("@")[0] + "%40group.calendar.google.com&ctz=Europe%2FKiev";
        BufferedImage bufferedImage = null;
        try {
                BitMatrix byteMatrix = qrCodeWriter.encode(myWeb, BarcodeFormat.QR_CODE, width, height);
                bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                bufferedImage.createGraphics();
                Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
                graphics.setColor(Color.WHITE);
                graphics.fillRect(0, 0, width, height);
                graphics.setColor(Color.BLACK);

                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        if (byteMatrix.get(i, j)) {
                            graphics.fillRect(i, j, 1, 1);
                        }
                    }
                }
            } catch (WriterException ex) {
                ex.printStackTrace();
            }
            return SwingFXUtils.toFXImage(bufferedImage, null);
    } catch (Exception e) {
        e.printStackTrace();
        }
    return null;
}
}