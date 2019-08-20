package testui;

import client.StreamClient;
import client.StreamFileSelector;
import client.StreamOptions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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

    StreamClient GlobalClient;

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

    public void Click_LoadFiles(ActionEvent actionEvent) throws MalformedURLException, InterruptedException {
        String DownloadDirectory = "C:\\";
        String MagnetLink = "magnet:?xt=urn:btih:d1eb2b5cf80e286a7f848ab0c31638856db102d4";
        String TorrentFile = "C:\\Users\\Pichler Martin\\Downloads\\Torrents\\VanBeethoven.torrent";

        if (txt_dldirectory.getText().contains("\\")) {
            DownloadDirectory = txt_dldirectory.getText();
        }

        if (txt_magnetlink.getText().contains("magnet:?xt=urn:btih:")) {
            MagnetLink = txt_magnetlink.getText();
        }

        StreamOptions options = new StreamOptions(MagnetLink, new File(DownloadDirectory));

        StreamClient streamClient = new StreamClient(options);

        GlobalClient = streamClient;

        //StreamFileSelector();
    }
}
