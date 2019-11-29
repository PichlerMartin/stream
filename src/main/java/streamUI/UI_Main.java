package streamUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class UI_Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/UI_stream.fxml"));
        Parent root = loader.load();
        root.setStyle("-fx-background-image: url('/images/stream_UI_background.png'); -fx-background-repeat: no-repeat; -fx-background-size: 1215 765");
        UI_Controller c = loader.getController();

        Scene s = new Scene(root, 1200, 750);
        primaryStage.setTitle("stream");
        primaryStage.getIcons().add(new Image("/images/streamAppIcon.png"));
        primaryStage.setScene(s);
        primaryStage.setResizable(false);

        c.setParentStage(primaryStage);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
