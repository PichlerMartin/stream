package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/sample.fxml"));
        Parent root = loader.load();
        Controller c = loader.getController();

        Scene s = new Scene(root, 300, 275);

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(s);

        c.setParentStage(primaryStage);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
