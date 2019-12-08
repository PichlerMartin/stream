package testui;

import bt.Bt;
import bt.cli.CliClient;
import bt.data.Storage;
import bt.data.file.FileSystemStorage;
import bt.dht.DHTConfig;
import bt.dht.DHTModule;
import bt.runtime.BtClient;
import bt.runtime.Config;
import client.StreamClient;
import client.StreamOptions;
import com.google.inject.Module;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import meta.Globals;
import org.slf4j.LoggerFactory;
import support.StreamContext;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import static support.SupportMethods.*;

public class Select_Controller implements Initializable {
    @FXML
    private ListView<String> lv_files;
    @FXML
    private Button btn_confirm;
    @FXML
    private TextField txt_dldirectory;
    @FXML
    private TextField txt_magnetlink;

    private StreamClient GlobalClient;

    /**
     * Description
     * Bestätigt die aufgelistete Menge an Dateien, welche dann in das Download-Verzeichnis
     * geladen werden. Dieser Vorgang findet in einem neuen Thread statt.
     * @param ms: Action Event des Button(-Clicks)
     */
    public void Click_ConfirmFileList(ActionEvent ms) throws IOException {
        Stage stage = (Stage) btn_confirm.getScene().getWindow();

        //this.ActualWorkingTorrentInvocation();
        //this.ownTorrentImplementation();
        //this.ownGlobalTorrentImplementation();
        this.AtomashpolskiyExample();

        stage.close();
    }

    /**
     * Description
     * Diese Methode besteht aus dem Example welche von dem Ersteller der Bt-Bibliothek, atomashpolskiy,
     * zur Verfügnug gestellt wurde.
     *
     * https://github.com/atomashpolskiy/bittorrent
     */
    private void AtomashpolskiyExample() {
        // enable multithreaded verification of torrent data
        Config config = new Config() {
            @Override
            public int getNumOfHashingThreads() {
                return Runtime.getRuntime().availableProcessors() * 2;
            }
        };

        // enable bootstrapping from public routers
        Module dhtModule = new DHTModule(new DHTConfig() {
            @Override
            public boolean shouldUseRouterBootstrap() {
                return true;
            }
        });

        // get download directory
        Path targetDirectory = Paths.get(System.getProperty("user.home"), "Downloads");

        // create file system based backend for torrent data
        Storage storage = new FileSystemStorage(targetDirectory);

        // create client with a private runtime
        BtClient client = Bt.client()
                .config(config)
                .storage(storage)
                .magnet(Globals.MAGNET_LINK)
                .autoLoadModules()
                .module(dhtModule)
                .stopWhenDownloaded()
                .build();

        // launch
        client.startAsync().join();
    }

    private void ownGlobalTorrentImplementation() {

        StreamOptions options = new StreamOptions(Globals.MAGNET_LINK, new File(Globals.DOWNLOAD_DIRECTORY));

        configureLogging(options.getLogLevel());
        configureSecurity(LoggerFactory.getLogger(StreamClient.class));
        registerLog4jShutdownHook();

        //GlobalClient.start();
    }

    private void ownTorrentImplementation() {

        StreamOptions options = new StreamOptions(Globals.MAGNET_LINK, new File(Globals.DOWNLOAD_DIRECTORY));

        configureLogging(options.getLogLevel());
        configureSecurity(LoggerFactory.getLogger(StreamClient.class));
        registerLog4jShutdownHook();

        //StreamClient.main(new String[]{"-d", Globals.DOWNLOAD_DIRECTORY, "-m", Globals.MAGNET_LINK});
    }

    public void ActualWorkingTorrentInvocation(){
        try{
            CliClient.main(new String[]{"-d", Globals.DOWNLOAD_DIRECTORY, "-m", Globals.MAGNET_LINK});
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     */
    @Override
    @FXML
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * Lädt zu Testzwecken Standardeinstellungen in die Variablen Globals.DOWNLOAD_DIRECTORY und MagnetLink,
     * diese zwei Variablen dienen zur konkretisierung der Mindest-Daten welche zum Download einer
     * Torrent-Datei notwendig sind. In den nachfolgenden If-Statements wird überprüft ob eine andere
     * Magnet-URI bzw. ein Verzeichnis gewünscht ist. Anschließend werden Objekte der wichtigen Klassen
     * erzeugt und die Dateien-Liste in die List-Box geladen.
     *
     * @param actionEvent: Action-Event der Java FX Klasse
     * @throws MalformedURLException: wird geworfen, wenn eine URL nicht dem Standardformat entspricht
     */
    public void Click_LoadFiles(ActionEvent actionEvent) throws MalformedURLException {
        if (txt_dldirectory.getText().contains("\\")) {
            Globals.DOWNLOAD_DIRECTORY = txt_dldirectory.getText();
        }

        if (txt_magnetlink.getText().contains("magnet:?xt=urn:btih:")) {
            Globals.MAGNET_LINK = txt_magnetlink.getText();
        }

        StreamContext.getInstance().currentController().setLabel(new Label("Torrent fetched"));
    }
}
