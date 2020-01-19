package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * <p>this method is a executable main method which contains another main function, serving as
 * a program entry point in order to test the program. when main is called it builds a generic
 * java fx window with some simple controls. it was initially copied from a previous project and
 * served the purpose of helping the developers to have a reference model for a java fx app</p>
 *
 * <p>because of some problems and challenges when starting a java fx app in a maven environment this
 * class accompanied by the sample.fxml-model was copied and used as a template for the testing
 * application in {@link testui.UI_Main#main(String[])}</p>
 *
 * @author PichlerMartin
 * @since summer 2019
 */
public class Main extends Application {

    /**
     * <p>this is a simple main method which launches the start method below in this class, invoking
     * a new java fx window with the contents of the sample.fxml-file. there are no special directions
     * or usage instructions for this method</p>
     *
     * @param args generic string array with command line arguments
     *
     * @author PichlerMartin
     * @since summer 2019
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * {@inheritDoc}
     *
     * <hr><blockquote><pre>
     *     FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/sample.fxml"));
     * </pre></blockquote></hr>
     * <p>loads a new fxml window based on the fxml-directions found in the provided fxml file from
     * the resources/fxml directory</p>
     *
     * <hr><blockquote><pre>
     *     Scene s = new Scene(root, 300, 275);
     * </pre></blockquote></hr>
     * <p>sets the root scene with height and width for the fxml window</p>
     *
     * <hr><blockquote><pre>
     *     c.setParentStage(primaryStage);
     *     primaryStage.show();
     * </pre></blockquote></hr>
     * <p>launches the fxml window with the pre-set fxml-file and title</p>
     *
     * @param primaryStage
     * @throws Exception
     * @author PichlerMartin
     * @since summer 2019
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/sample.fxml"));
        Parent root = loader.load();
        Controller c = loader.getController();

        Scene s = new Scene(root, 300, 275);

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(s);

        c.setParentStage(primaryStage);
        primaryStage.show();
    }
}
