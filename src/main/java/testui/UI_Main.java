package testui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * <p>this is the main method for invoking the fxml window of this package (testui). as every other class
 * in this package this class solely served the purpose of testing the capabilities of the bittorrent protocol
 * and its frameworks.</p>
 * <p>Die Main-Methode, von welcher aus die UI_Controller_main_page-Klasse angesteuert wird. Dieses Klassenkonstrukt
 * dient dem Zweck der Ertestung verschiedener M&ouml;glichkeiten welche die Bt-Library bietet.</p>
 * <p>Manche Methoden wurden &uuml;bernommen, andere ver&auml;ndert und eine Vielzahl von Modifikationen wurde
 * vom Projektteam selbst gestaltet und programmiert.</p>
 * @author PichlerMartin
 * @since july 2019
 * @see Application
 */
public class UI_Main extends Application {

    /**
     * <p>obligatory main method for the invocation of the new fxml windows with the controller specified in
     * the fxml-file, of which the path can be found in the first few lines of {@link UI_Main#start(Stage)}</p>
     * <p>Obligatorisches Main</p>
     * @param args: command line arguments, are being ignored because this is a java fx application
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * {@inheritDoc}
     * <p>start method for the user interface, the link to the fxml page can be found within</p>
     * <p>start-Methode für das UI, der Verweis auf die XML-Datei ist intern zu finden</p>
     * @param primaryStage: PrimaryStage, the "main-stage" of every java fx application, names
     *                    the absolute sub-layer of an java fx application
     */
    @SuppressWarnings("deprecation")
    @Override
    public void start(Stage primaryStage) throws Exception {

        /**
         * as you can see the code below up until the two line comment is not used, because
         * a different fxml window start-method is called {@link streamUI.UI_Main}. the code
         * below calls the testing page {@link UI_Controller}
         */
        @Deprecated
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/UI_Network_Test.fxml"));
        Parent root = loader.load();
        UI_Controller c = loader.getController();

        Scene s = new Scene(root, 600, 500);

        primaryStage.setScene(s);

        c.setParentStage(primaryStage);
        primaryStage.show();

        //  Above code is deprecated
        //  Below is executed

        streamUI.UI_Main ui = new streamUI.UI_Main();
        ui.start(primaryStage);
    }
}
