package testui;

import bt.torrent.TorrentSessionState;
import client.StreamClient;
import client.StreamFileSelector;
import client.StreamOptions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import support.StreamContext;

import javax.annotation.Resources;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Stream;

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
    public void Click_ConfirmFileList(ActionEvent ms) {
        Stage stage = (Stage) btn_confirm.getScene().getWindow();

        new Thread(() -> {
            GlobalClient.start();
        }).start();

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
        String MagnetLink = "magnet:?xt=urn:btih:d1eb2b5cf80e286a7f848ab0c31638856db102d4";
        MagnetLink = "magnet:?xt=urn:btih:223f7484d326ad8efd3cf1e548ded524833cb77e" /* + "&dn=Avengers.Endgame.2019.1080p.BRRip.x264-MP4&tr=udp%3A%2F%2Ftracker.leechers-paradise.org%3A6969&tr=udp%3A%2F%2Ftracker.openbittorrent.com%3A80&tr=udp%3A%2F%2Fopen.demonii.com%3A1337&tr=udp%3A%2F%2Ftracker.coppersurfer.tk%3A6969&tr=udp%3A%2F%2Fexodus.desync.com%3A6969" */;
        String TorrentFile = "C:\\Users\\Pichler Martin\\Downloads\\Torrents\\VanBeethoven.torrent";

        if (txt_dldirectory.getText().contains("\\")) {
            DownloadDirectory = txt_dldirectory.getText();
        }

        if (txt_magnetlink.getText().contains("magnet:?xt=urn:btih:")) {
            MagnetLink = txt_magnetlink.getText();
        }

        StreamOptions options = new StreamOptions(MagnetLink, new File(DownloadDirectory));

        StreamClient streamClient = new StreamClient(options, null);

        StreamContext.getInstance().currentController().setLabel(new Label("Torrent fetched"));

        GlobalClient = streamClient;
    }
}
