package testui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.Controller;

public class UI_Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/UI_Network_Test.fxml"));
        Parent root = loader.load();
        UI_Controller c = loader.getController();

        Scene s = new Scene(root, 600, 500);

        primaryStage.setScene(s);

        c.setParentStage(primaryStage);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
