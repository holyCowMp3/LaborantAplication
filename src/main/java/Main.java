import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{


        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("laborant");
        primaryStage.setScene(new Scene(root));
        primaryStage.setFullScreen(true);
        primaryStage.show();
        ObservableList<Screen> screen = Screen.getScreens();
////        if (screen);
//        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("monitorView.fxml")));
//        Stage stage = new Stage();
//        stage.setScene(scene);
//        stage.setFullScreen(true);
//        stage.setX(screen.get(1).getBounds().getMinX()+100);
//        stage.setY(screen.get(1).getBounds().getMinY()+100);
//        stage.setTitle("view");
//        stage.setScene(scene);
//        stage.show();
   }


    public static void main(String[] args) {
        launch(args);
    }
}
