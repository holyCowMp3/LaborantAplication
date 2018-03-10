

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.ImageView;

/**
 * This class using the zxing library, to generate QR codes from the links of the calendars;
 */

public class QR {
    /**
     * Summary of the calendar;
     */
    private StringProperty name;
    /**
     * ID of the calendar;
     */
    private StringProperty idCalendar;
    /**
     * {@link ImageView generates from the ID, using @link StringProperty idCalendar}
     */
    private ImageView code;

    /**
     * {@link Override QR standart constructor}
     * Generate QR using {@link QrGenerator} method generateQr;
     *
     * @param name - name of the Calendar;
     * @return QR;
     */
    public QR(String name) {
        this.name = new SimpleStringProperty(name);
        this.idCalendar = new SimpleStringProperty(Controller.getCalendarIdMap().get(name.toString()));
        this.code = new ImageView(QrGenerator.generateQr(this.idCalendar.getValue()));

    }

    /**
     * Getter for @param name
     *
     * @return name
     */
    public StringProperty getName() {
        return name;
    }

    /**
     * Getter for @param code
     * @return code
     */
    public ImageView getCode() {
        return code;
    }

    /**
     * Getter for @param idCalendar
     *
     * @return idCalendar
     */
    public StringProperty idCalendarProperty() {
        return idCalendar;
    }

    /**
     * {@link Override the standart toString {@link Object} method}
     */
    @Override
    public String toString() {
        return "QR{" +
                "name=" + name.toString() +
                ", code=" + code.toString() +
                '}';
    }
}

