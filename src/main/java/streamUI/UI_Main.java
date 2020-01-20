package streamUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * <p>this main class serves as a entry point for the java_fx_main_page, in order to invoke
 * a new instance of the stream torrent-client. it was created in order to test the
 * UI_stream_main_page.fxml, which displays the main user interface of the stream application.</p>
 *
 * <p>as you can see in the first lines of the code the fxml file gets loaded according to
 * the preset template from {@link sample.Main}, in the following instructions ui elements like
 * the title, icon and the window measuring is being set.</p>
 *
 * @see Application
 * @author TopeinerMarcel
 * @since 2019
 */
public class UI_Main extends Application {

    /**
     * <p>the launch command in this method causes the method {@link UI_Main#start(Stage)} to be invoked
     * which triggers the process of loading the UI_stream_main_page.fxml which displays the ui of stream.</p>
     * @param args standard parameter for command line passings
     * @author TopeinerMarcel
     * @since 2019
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * {@inheritDoc}
     * @see Stage
     *
     * <p>in this start method the main user interface from UI_stream_main_page.fxml gets loaded. this
     * method is inherited by the {@link Application} class and overriden by this class. after the assigment
     * of the different values and customisations for this window the primary stage gets shown in the last
     * line of code found in this method</p>
     *
     * @param primaryStage
     * @throws Exception
     * @author TopeinerMarcel
     * @since 2019
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/UI_stream_main_page.fxml"));
        Parent root = loader.load();

        //root.setStyle("-fx-background-image: url('/images/stream_LightModeUI_background.png'); -fx-background-repeat: no-repeat; -fx-background-size: 1215 765");
        UI_Controller_main_page c = loader.getController();

        //  set the primary stage
        c.setStage(primaryStage);
        Scene s = new Scene(root, 1200, 750);

        //  set the title
        primaryStage.setTitle("stream");

        //  set the window icon
        primaryStage.getIcons().add(new Image("/images/stream_AppIcon_circle.png"));
        primaryStage.setScene(s);
        primaryStage.setResizable(false);

        c.setParentStage(primaryStage);
        primaryStage.show();
    }
}
