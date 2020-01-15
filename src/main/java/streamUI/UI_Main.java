package streamUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class UI_Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/UI_stream_main_page.fxml"));
        Parent root = loader.load();

        root.setStyle("-fx-background-image: url('/images/stream_UI_background.png'); -fx-background-repeat: no-repeat; -fx-background-size: 1215 765");
        UI_Controller_main_page c = loader.getController();

        c.setStage(primaryStage);
        Scene s = new Scene(root, 1200, 750);

        primaryStage.setTitle("stream");

        primaryStage.getIcons().add(new Image("/images/stream_AppIcon_circle.png"));
        primaryStage.setScene(s);
        primaryStage.setResizable(false);

        c.setParentStage(primaryStage);
        primaryStage.show();
    }
}
