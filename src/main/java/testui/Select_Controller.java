package testui;

import client.StreamClient;
import client.StreamOptions;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.LoggerFactory;
import support.Globals;
import support.StreamContext;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import static support.SupportMethods.*;

/**
 * <p>this controller is assigned to the fxml page which serves to select single torrent parts inside the testing
 * environment of testui. as every other class in this package this class solely served the purpose of testing the
 * capabilities of the bittorrent protocol and its frameworks.</p>
 * @author PichlerMartin
 * @since august 2019
 * @see Initializable
 */
public class Select_Controller implements Initializable {
    @SuppressWarnings("unused")
    @FXML
    private ListView<String> lv_files;
    @FXML
    private Button btn_confirm;
    @FXML
    private TextField txt_dldirectory;
    @FXML
    private TextField txt_magnetlink;

    @SuppressWarnings("unused")
    private StreamClient GlobalClient;

    /**
     * <p>Bestätigt die aufgelistete Menge an Dateien, welche dann in das Download-Verzeichnis
     * geladen werden. Dieser Vorgang findet in einem neuen Thread statt.</p>
     * <p>this method serves as a handler for the button of this fxml-window, which confirms
     * which files should be loaded into the download directory. it invokes a new thread. via a
     * lambda function.</p>
     * @author PichlerMartin
     * @since august 2019
     * @see Thread
     */
    public void Click_ConfirmFileList() {
        Stage stage = (Stage) btn_confirm.getScene().getWindow();

        new Thread(this::ownGlobalTorrentImplementation).start();

        //  the method calls below are now realized in streamUI/UI_controller_main_page.java

        //  the method calls below served for testing purposes and are now commented out because
        //  the actual working code was implemented in streamUI/UI_controller_main_page.java

        //this.ActualWorkingTorrentInvocation();
        //this.ownTorrentImplementation();
        //this.AtomashpolskiyExample();

        stage.close();
    }

    /**
     * <p>Called to initialize a controller after its root element has been
     * completely processed.</p>
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     * @see URL
     * @see ResourceBundle
     * @see Initializable
     * @author PichlerMartin
     * @since august 2019
     */
    @Override
    @FXML
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * <p>this method creates a object of {@link StreamOptions} which provides the necessary data to initialise a
     * torrent download Afterwards the cofiguration-methods from {@link support.SupportMethods} are called to configure
     * the environment. after that the local field {@link Select_Controller#GlobalClient} is being started (it provides
     * a new instance of the torrent-downloader).</p>
     *
     * @see StreamClient
     * @author PichlerMartin
     * @since august 2019
     */
    private void ownGlobalTorrentImplementation() {

        StreamOptions options = new StreamOptions(Globals.MAGNET_LINK, new File(Globals.DOWNLOAD_DIRECTORY));

        configureLogging(options.getLogLevel());
        configureSecurity(LoggerFactory.getLogger(StreamClient.class));
        registerLog4jShutdownHook();

        GlobalClient.start();
    }

    /**
     * <p>Loads for testing purposes the default download-directory from {@link Globals#DOWNLOAD_DIRECTORY} and the
     * according magnet lin/uri which specify the minimum required data to initialise the download of an torrent
     * file. In the following if-statement it is being checked whether a specific download directory or magnet link/uri
     * is proposed by the user. Then important objects of the relevant classes are being created and the file list is
     * loaded inside the list-box, displayed on the user interface.</p>
     * <p>L&auml;dt zu Testzwecken Standardeinstellungen in die Variablen Globals.DOWNLOAD_DIRECTORY und MagnetLink,
     * diese zwei Variablen dienen zur konkretisierung der Mindest-Daten welche zum Download einer
     * Torrent-Datei notwendig sind. In den nachfolgenden If-Statements wird &uuml;berpr&uuml;ft ob eine andere
     * Magnet-URI bzw. ein Verzeichnis gew&uuml;nscht ist. Anschlie&szlig;end werden Objekte der wichtigen Klassen
     * erzeugt und die Dateien-Liste in die List-Box geladen.</p>
     *
     * @see Globals
     * @author PichlerMartin
     * @since august 2019
     */
    public void Click_LoadFiles() {
        if (txt_dldirectory.getText().contains("\\")) {
            Globals.DOWNLOAD_DIRECTORY = txt_dldirectory.getText();
        }

        if (txt_magnetlink.getText().contains("magnet:?xt=urn:btih:")) {
            Globals.MAGNET_LINK = txt_magnetlink.getText();
        }

        StreamContext.getInstance().currentController().setLabel(new Label("Torrent fetched"));
    }
}
