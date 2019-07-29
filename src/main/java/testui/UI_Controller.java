package testui;

import bt.Bt;
import bt.data.Storage;
import bt.data.file.FileSystemStorage;
import bt.dht.DHTConfig;
import bt.dht.DHTModule;
import bt.runtime.BtClient;
import bt.runtime.Config;
import com.google.inject.Module;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    public void setParentStage (Stage root) {
        this.parentStage = root;
    }

    public void Enter_Action(ActionEvent actionEvent) {
        if(lbl_filelib.getSelectionModel().getSelectedItem() != null){
            String item = lbl_filelib.getSelectionModel().getSelectedItem();

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
            Path targetDirectory = new File("~/Downloads").toPath();

            // create file system based backend for torrent data
            Storage storage = new FileSystemStorage(targetDirectory);

            // create client with a private runtime
            BtClient client = Bt.client()
                    .config(config)
                    .storage(storage)
                    .magnet("magnet:?xt=urn:btih:d1eb2b5cf80e286a7f848ab0c31638856db102d4&dn=Beethoven+-+The+Very+Best+Of+Beethoven+%282005%29+%5BFLAC%5D+dussin")
                    .autoLoadModules()
                    .module(dhtModule)
                    .stopWhenDownloaded()
                    .build();

            // launch
            client.startAsync().join();
        }
    }

    /**
     * Loads all files from the resources/torrents directory at startup, for testing purposes
     * @param actionEvent
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
