

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.ImageView;

public class QR {
    private StringProperty name;
    private ImageView code;

    public QR(String name) {
        this.name = new SimpleStringProperty(name);
        this.code = new ImageView(QrGenerator.generateQr(name));
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

    @Override
    public String toString() {
        return "QR{" +
                "name=" + name.toString() +
                ", code=" + code.toString() +
                '}';
    }
}

