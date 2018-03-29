import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{


        Parent root = FXMLLoader.load(getClass().getResource("mainLaborantIputView.fxml"));
        primaryStage.setTitle("laborant");
        primaryStage.setScene(new Scene(root,1280,720 ));
        primaryStage.show();

//        Scene sceneTest = new Scene(FXMLLoader.load(getClass().getResource("testView.fxml")));
//        Stage stageTest = new Stage();
//        stageTest.setScene(sceneTest);
//        stageTest.show();

   }


    public static void main(String[] args) {
        launch(args);
    }
}
