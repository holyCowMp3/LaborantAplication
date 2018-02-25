

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.ImageView;

public class QR {
    private StringProperty name;
    private StringProperty summary;
    private ImageView code;

    public QR(String name) {
        this.name = new SimpleStringProperty(name);
        this.summary = new SimpleStringProperty(Controller.getCalendarIdMap().get(name.toString()));
        this.code = new ImageView(QrGenerator.generateQr(this.summary.getValue()));

    }

    public StringProperty getName() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public ImageView getCode() {
        return code;
    }

    public void setCode(ImageView code) {
        this.code.setImage(code.getImage());
    }

    public String getSummary() {
        return summary.get();
    }

    public StringProperty summaryProperty() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary.set(summary);
    }

    @Override
    public String toString() {
        return "QR{" +
                "name=" + name.toString() +
                ", code=" + code.toString() +
                '}';
    }
}

