package streamUI;

import bt.Bt;
import bt.cli.CliClient;
import bt.data.Storage;
import bt.data.file.FileSystemStorage;
import bt.dht.DHTConfig;
import bt.dht.DHTModule;
import bt.runtime.BtClient;
import bt.runtime.Config;
import client.StreamClient;
import com.google.inject.Module;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import meta.Globals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static meta.Globals.DOWNLOAD_DIRECTORY;

public class UI_Controller {

    @FXML
    private Stage parentStage;

    @FXML
    private Button btnTorrents;

    @FXML
    private Button btnAddTorrents;

    @FXML
    private Button btnDownloading;

    @FXML
    private Button btnUploading;

    @FXML
    private Button btnFinished;

    @FXML
    private Button btnSettings;

    @FXML
    private Button btnHelp;

    @FXML
    private Button btnStorageLocation;

    @FXML
    private TextField txtDownloadLocation;

    @FXML
    private TextField txtMagnetURI;

    @FXML
    private ListView<String> livFiles;

    @FXML
    private VBox VBoxTorrents;

    @FXML
    private TableView TVTorrentsList;

    @FXML
    private GridPane GPAddTorrent;

    @FXML
    private GridPane GPSettings;

    //  ToDo:   Continue with working on the user interface

    @FXML
    private CheckBox chbDefaultPort;

    @FXML
    private CheckBox chbDownloadAll;

    @FXML
    private CheckBox chbSeedAfterDownload;

    @FXML
    ComboBox<String> cboxSelectLanguage;

    @FXML
    private Stage stage;

    Locale[] supportedLocales = {
            Locale.GERMAN,
            Locale.ENGLISH
    };



    @FXML
    public void handleOnClickedbtnTorrents (ActionEvent event) {
        resetVisibility();
        VBoxTorrents.setVisible(true);
        TVTorrentsList.setPlaceholder(new Label("No Torrents found!"));
    }

    @FXML
    public void handleOnClickedbtnAddTorrents (ActionEvent event) {
        resetVisibility();
        GPAddTorrent.setVisible(true);
    }

    @FXML
    public void handleOnClickedbtnDownloading (ActionEvent event) {
        resetVisibility();
        VBoxTorrents.setVisible(true);
        TVTorrentsList.setPlaceholder(new Label("No downloading Torrents found!"));
    }

    @FXML
    public void handleOnClickedbtnUploading (ActionEvent event) {
        resetVisibility();
        VBoxTorrents.setVisible(true);
        TVTorrentsList.setPlaceholder(new Label("No uploading Torrents found!"));
    }

    @FXML
    public void handleOnClickedbtnFinished (ActionEvent event) {
        resetVisibility();
        VBoxTorrents.setVisible(true);
        TVTorrentsList.setPlaceholder(new Label("No Torrents found!"));
    }

    @FXML
    public void handleOnClickedbtnSettings (ActionEvent event) {

        resetVisibility();
        GPSettings.setVisible(true);

        for (Locale loc: supportedLocales) {
            cboxSelectLanguage.getItems().add(loc.getDisplayName());
        }
    }

    @FXML
    public void handleOnClickedCbox (ActionEvent event) {
        for (Locale loc: supportedLocales) {
            if (loc.getDisplayName().equals(cboxSelectLanguage.getValue())) {
                changeLanguage(loc);
            }
        }
    }

    @FXML
    public void handleOnClickedbtnStorageLocation (ActionEvent event) {

        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Speichern unter");
        dirChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File directory = dirChooser.showDialog(stage);

        txtDownloadLocation.setText(directory.toString());

        this.onDirectorySelected(event);
    }

    private void resetVisibility() {
        VBoxTorrents.setVisible(false);
        GPAddTorrent.setVisible(false);
        GPSettings.setVisible(false);
    }

    public void setStage (Stage CurrentStage) {
        this.stage = CurrentStage;
    }

    public void setParentStage (Stage root) {

        Locale currentLocale = Locale.GERMAN;

        this.parentStage = root;
        resetVisibility();
        VBoxTorrents.setVisible(true);
        TVTorrentsList.setPlaceholder(new Label("No Torrents found!"));

        changeLanguage(currentLocale);
    }

    /**
     * This method is called by buttons, which have been entered
     * It changes the style and the colour of the text in the caller button
     * @param event
     */
    @FXML
    public void handleOnMenuEntryEntered (MouseEvent event) {

        Button btnEnteredBtn = (Button)event.getSource();
        btnEnteredBtn.setStyle(btnEnteredBtn.getStyle() + "; -fx-underline: true; -fx-text-fill:  #1b3957");

    }

    /**
     * This method changes the style and colour of the caller button back to the original state
     * If the mouse leaves one of the buttons, this method is called
     * @param event
     */
    @FXML
    public void handleOnMenuEntryLeft (MouseEvent event) {

        Button btnLeftbtn = (Button)event.getSource();
        btnLeftbtn.setStyle(btnLeftbtn.getStyle() + "; -fx-underline: false; -fx-text-fill: white");
    }


    public void changeLanguage(Locale loc) {

        ResourceBundle labels = ResourceBundle.getBundle("ResourceBundle", loc);
        btnTorrents.setText(labels.getString("btnTorrents"));
        btnAddTorrents.setText(labels.getString("btnAddTorrents"));
        btnDownloading.setText(labels.getString("btnDownloading"));
        btnUploading.setText(labels.getString("btnUploading"));
        btnFinished.setText(labels.getString("btnFinished"));
        btnSettings.setText(labels.getString("btnSettings"));
        btnHelp.setText(labels.getString("btnHelp"));

    }

    //region Pichler part
    @FXML
    public void onEnter(ActionEvent ae){


        //new Thread(this::ActualWorkingTorrentInvocation).start();
        //  Works sometimes, but needs review in class files
        //  FixMe:  Check multiple folders and task manager for downloads, also test upper implementation

        //new Thread(this::ownTorrentImplementation).start();
        //  Does not work, prints errors

        new Thread(this::AtomashpolskiyExample).start();
        //  Works, prints status warnings
    }

    @FXML
    private void onDirectorySelected(ActionEvent ae){
        DOWNLOAD_DIRECTORY = txtDownloadLocation.getText();

        List<String> files = new ArrayList<>();
        List<String> folders = new ArrayList<>();

        for (final File fileEntry : Objects.requireNonNull(new File(DOWNLOAD_DIRECTORY).listFiles())) {
            if (fileEntry.isDirectory()) {
                folders.add(fileEntry.getPath());
            } else {
                files.add(fileEntry.getPath());
            }
        }

        livFiles.getItems().addAll(folders);
        livFiles.getItems().addAll(files);
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


        Path targetDirectory = Paths.get(DOWNLOAD_DIRECTORY);

        if (txtMagnetURI.getText().contains("magnet:?xt=urn:btih:")) {
            Globals.MAGNET_LINK = txtMagnetURI.getText();
        }

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

    private void ownTorrentImplementation() {
        try{
            StreamClient.main(new String[]{"-d", Globals.DOWNLOAD_DIRECTORY, "-m", Globals.MAGNET_LINK});
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void ActualWorkingTorrentInvocation(){
        try{
            CliClient.main(new String[]{"-d", Globals.DOWNLOAD_DIRECTORY, "-m", Globals.MAGNET_LINK});
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    //endregion Pichler part
}
