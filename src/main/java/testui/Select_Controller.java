package testui;

import bt.cli.CliClient;
import client.StreamClient;
import client.StreamOptions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.LoggerFactory;
import support.StreamContext;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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

        CliClient cli = new CliClient()

        //GlobalClient.start();
        CliClient.main(new String[]{"-d", "C:\\", "-m", "magnet:?xt=urn:btih:7f34612e0fac5e7b051b78bdf1060113350ebfe0&dn=big_buck_bunny_1080p_h264.mov"});
        //StreamClient.main(new String[]{"-d", "C:\\", "-m", "magnet:?xt=urn:btih:7f34612e0fac5e7b051b78bdf1060113350ebfe0&dn=big_buck_bunny_1080p_h264.mov"});

        stage.close();
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
     * Lädt zu Testzwecken Standardeinstellungen in die Variablen DownloadDirectory und MagnetLink,
     * diese zwei Variablen dienen zur konkretisierung der Mindest-Daten welche zum Download einer
     * Torrent-Datei notwendig sind. In den nachfolgenden If-Statements wird überprüft ob eine andere
     * Magnet-URI bzw. ein Verzeichnis gewünscht ist. Anschließend werden Objekte der wichtigen Klassen
     * erzeugt und die Dateien-Liste in die List-Box geladen.
     *
     * @param actionEvent: Action-Event der Java FX Klasse
     * @throws MalformedURLException: wird geworfen, wenn eine URL nicht dem Standardformat entspricht
     */
    public void Click_LoadFiles(ActionEvent actionEvent) throws MalformedURLException {
        String DownloadDirectory = "C:\\";
        String MagnetLink = "magnet:?xt=urn:btih:7f34612e0fac5e7b051b78bdf1060113350ebfe0&dn=big_buck_bunny_1080p_h264.mov";
        MagnetLink = "magnet:?xt=urn:btih:7f34612e0fac5e7b051b78bdf1060113350ebfe0&dn=big_buck_bunny_1080p_h264.mov";
        String TorrentFile = "C:\\Users\\Pichler Martin\\Downloads\\Torrents\\Big Buck Bunny (1920x1080 h.264).torrent";

        if (txt_dldirectory.getText().contains("\\")) {
            DownloadDirectory = txt_dldirectory.getText();
        }

        if (txt_magnetlink.getText().contains("magnet:?xt=urn:btih:")) {
            MagnetLink = txt_magnetlink.getText();
        }

        StreamOptions options = new StreamOptions(MagnetLink, new File(DownloadDirectory));

        configureLogging(options.getLogLevel());
        configureSecurity(LoggerFactory.getLogger(StreamClient.class));
        registerLog4jShutdownHook();

        //StreamClient streamClient = new StreamClient(options/*, null*/);

        StreamContext.getInstance().currentController().setLabel(new Label("Torrent fetched"));

        //GlobalClient = streamClient;
    }
}
