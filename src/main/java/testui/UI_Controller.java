package testui;

import bt.Bt;
import bt.data.Storage;
import bt.data.file.FileSystemStorage;
import bt.dht.DHTConfig;
import bt.dht.DHTModule;
import bt.runtime.BtClient;
import bt.runtime.Config;
import client.Client;
import client.StreamClient;
import client.StreamOptions;
import com.google.inject.Module;
import filelibrary.Library;
import filelibrary.PublicLibrary;
import filelibrary.TorrentInFileSystem;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import joptsimple.OptionException;
import support.SupportMethods;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
    /**
     * Progressbar-Test to overlook the functionality and ability of the use-case
     */
    @FXML
    private ProgressBar prog_m;

    private Library torrents = new PublicLibrary();

    public void setParentStage (Stage root) {
        this.parentStage = root;
    }

    public void Enter_Action(ActionEvent actionEvent) throws MalformedURLException {
        final String DownloadDirectory = "C:\\";
        String MagnetLink = "magnet:?xt=urn:btih:d1eb2b5cf80e286a7f848ab0c31638856db102d4&dn=Beethoven+-+The+Very+Best+Of+Beethoven+%282005%29+%5BFLAC%5D+dussin";

        if(lbl_filelib.getSelectionModel().getSelectedItem() != null){
            MagnetLink = lbl_filelib.getSelectionModel().getSelectedItems().get(0);
        }

        //  ToDo:   find lambda expression for selected element in list
        //  torrents.getContents().get(torrents.getContents().forEach(x -> )


        StreamOptions options = new StreamOptions(MagnetLink, new File(DownloadDirectory));

        StreamClient client = new StreamClient(options);
        client.start();

		
		//	ToDo:	Check sample from bt github page

        /*
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
                .magnet("magnet:?xt=urn:btih:af0d9aa01a9ae123a73802cfa58ccaf355eb19f1")
                .autoLoadModules()
                .module(dhtModule)
                .stopWhenDownloaded()
                .build();

        // launch
        client.startAsync().join();
        */
    }

    /**
     * Loads all files from the resources/torrents directory at startup, for testing purposes
     * @param actionEvent:  ActionEvent-Parameter, currently not in use
     * @throws IOException: In case the files won't load properly
     */
    public void LoadStartConfiguration(ActionEvent actionEvent) throws IOException {
        try (Stream<Path> paths = Files.walk(Paths.get("target\\classes\\torrents"))) {


            Object[] directoryContent = paths.toArray();

            for (Object s: directoryContent
                 ) {
                String content = s.toString();
                String contentname = content.substring(content.lastIndexOf('\\') + 1);

                if (!contentname.equals("torrents")){
                    torrents.addTorrent(new TorrentInFileSystem(content, contentname.substring(0, contentname.length() - 8)));
                }
            }

            torrents.getContents().forEach(x -> lbl_filelib.getItems().add(x.getName()));
        }
    }

    /**
     * @param mouseEvent: mouse event get thrown when clicked
     * desc:    https://stackoverflow.com/questions/32757069/updating-progressbar-value-within-a-for-loop
     */
    public void click_makeProgress(MouseEvent mouseEvent) {
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
    }
}
