package testui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Die Main-Methode, von welcher aus die UI_Controller_main_page-Klasse angesteuert wird. Dieses Klassenkonstrukt
 * dient dem Zweck der Ertestung verschiedener Möglichkeiten welche die Bt-Library bietet.
 * <p>
 * Manche Methoden wurden übernommen, andere verändert und eine Vielzahl von Modifikationen wurde
 * vom Projektteam selbst gestaltet und programmiert.
 */
public class UI_Main extends Application {

    /**
     * Obligatorisches Main
     *
     * @param args: Kommandozeilenargumente, werden ignoriert, da es sich um eine Java-FX Anwendung
     *              handelt
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * start-Methode für das UI, der Verweis auf die XML-Datei ist intern zu finden
     *
     * @param primaryStage: PrimaryStage, die "Hauptbühne" der Anwendung, bezeichnet in
     *                      Java FX das uneterste Layer der Anwendung
     * @throws Exception: Exception welche im Falle des Falles geworfen wird
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
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
