package testui;

import bt.Bt;
import bt.data.Storage;
import bt.data.file.FileSystemStorage;
import bt.dht.DHTConfig;
import bt.dht.DHTModule;
import bt.runtime.BtClient;
import bt.runtime.Config;
import com.google.inject.Module;
import com.turn.ttorrent.common.Torrent;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import com.turn.ttorrent.client.*;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.stream.Stream;

/**
 * Controller for testing class UI_Main
 */
public class UI_Controller {

    @FXML
    private Stage parentStage;

    @FXML
    private ListView<String> lbl_filelib;
    @FXML
    private Button cmd_enter;
    @FXML
    private Button cmd_start;
    @FXML
    private ProgressBar prog_m;

    /**
     * Progressbar-Test to overlook the functionality and ability of the use-case
     */
    @FXML
    private ProgressBar prog_n;

    private void setParentStage (Stage root) {
        this.parentStage = root;
    }

    public void Enter_Action(ActionEvent actionEvent){

    }

    /**
     * Loads all files from the resources/torrents directory at startup, for testing purposes
     * @param actionEvent:  ActionEvent-Parameter, currently not in use
     * @throws IOException
     *
     * desc:    https://stackoverflow.com/questions/32757069/updating-progressbar-value-within-a-for-loop
     */
    public void LoadStartConfiguration(ActionEvent actionEvent) throws IOException {

        try (Stream<Path> paths = Files.walk(Paths.get("target\\classes\\torrents"))) {

            new Thread(() -> {
                for(int i = 0; i <=100; i++){
                    final int position = i;
                    Platform.runLater(() -> {
                        prog_m.setProgress(position/100.0);
                    });
                    try{
                        Thread.sleep(100);
                    }catch(Exception e){ System.err.println(e); }
                }
            }).start();

            paths.filter(Files::isRegularFile).forEach(x -> lbl_filelib.getItems().add(x.toString().substring(24, x.toString().length() - 8)));
        }
    }
}
