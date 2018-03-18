import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.web.WebView;
import javafx.util.Duration;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class MonitorViewController {

    @FXML
    private WebView koridorWebViewForm;

    @FXML
    void initialize() {

            URL page = getClass().getResource("aaa.html");
            koridorWebViewForm.getEngine().load(page.toExternalForm());

        Timeline clock = new Timeline(new KeyFrame(Duration.minutes(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                koridorWebViewForm.getEngine().load(page.toExternalForm());            }
        }));
        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();




    }
}
