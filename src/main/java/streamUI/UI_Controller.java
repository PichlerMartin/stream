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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import meta.Globals;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static java.nio.file.Files.exists;
import static meta.Globals.*;

public class UI_Controller implements Initializable {

    @FXML
    private Stage parentStage;

    @FXML
    private Stage stage;

    @FXML
    private TextField txtDownloadLocation;

    @FXML
    private TextField txtMagnetURI;

    @FXML
    private TextField txtTorrentFile;

    @FXML
    private TextField txtPort;

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
    private Button btnAddPartsofTorrent;

    @FXML
    private Button btnSelectTorrentFile;

    @FXML
    private Button btnStartDownload;

    @FXML
    private Button btnStorageLocation;

    @FXML
    private CheckBox chbDefaultPort;

    @FXML
    private CheckBox chbDownloadAll;

    @FXML
    private CheckBox chbSeedAfterDownload;

    @FXML
    private CheckBox chbUseTorrentFile;

    @FXML
    private CheckBox chbUseMagnetURI;

    @FXML
    private ListView<String> livFiles;

    @FXML
    private VBox VBoxTorrents;

    @FXML
    ComboBox<String> cboxSelectLanguage;

    @FXML
    private TableView TVTorrentsList;


    @FXML
    private GridPane GPAddTorrent;

    @FXML
    private GridPane GPSettings;

    Locale[] supportedLocales = {
            Locale.GERMAN,
            Locale.ENGLISH
    };

    @Override
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        //  forces the field txtPort to be numeric only
        //  https://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx
        txtPort.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtPort.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

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

        if (this.isDirectoryValid(directory)){
            txtDownloadLocation.setText(directory.toString());
            DIRECTORY_SELECTED = true;

            this.onDirectorySelected(event);
        } else {
            this.showWarning("Bitte Verzeichnis wählen",
                    "Um fortzufahren wählen Sie bitte ein gültiges Verzeichnis.",
                    false);
        }
    }

    private void showWarning(String titel, String header, String message, boolean showOK) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titel);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK && showOK) {
                System.out.println("Pressed OK.");
            }
        });
    }

    private void showWarning(String header, String message, boolean showOK) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warnung");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK && showOK) {
                System.out.println("Pressed OK.");
            }
        });
    }

    private void resetVisibility() {
        VBoxTorrents.setVisible(false);
        GPAddTorrent.setVisible(false);
        GPSettings.setVisible(false);

        //  ToDo:   Implement disabled-property reset in ui
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
    public void handleOnMagnetURIEntered(ActionEvent ae){
        if (txtMagnetURI.getText().contains("magnet:?xt=urn:btih:")) {
            MAGNET_LINK = txtMagnetURI.getText();
            txtDownloadLocation.requestFocus();
        }
        else {
            showWarning("Magnet URI ungültig",
                    "Bitte geben Sie eine gültige Magnet URI in das Textfeld ein um fortzufahren",
                    false);
        }
    }

    @FXML
    public void handleOnDirectoryEntered(ActionEvent ae){
        File directory = new File(txtDownloadLocation.getText());

        if (this.isDirectoryValid(directory)){
            livFiles.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            txtDownloadLocation.setText(directory.toString());
            DIRECTORY_SELECTED = true;

            this.onDirectorySelected(ae);
        } else {
            this.showWarning("Bitte Verzeichnis wählen",
                    "Um fortzufahren wählen Sie bitte ein gültiges Verzeichnis.",
                    false);
        }
    }

    @FXML
    private void onDirectorySelected(ActionEvent ae){
        DOWNLOAD_DIRECTORY = txtDownloadLocation.getText();
        MAGNET_LINK = txtMagnetURI.getText();

        List<String> files = new ArrayList<>();
        List<String> folders = new ArrayList<>();

        for (final File fileEntry : Objects.requireNonNull(new File(DOWNLOAD_DIRECTORY).listFiles())) {
            if (this.isDirectoryValid(fileEntry)) {
                folders.add(fileEntry.getPath());
            } else {
                files.add(fileEntry.getPath());
            }
        }

        livFiles.getItems().addAll(folders);
        livFiles.getItems().addAll(files);

        //livFiles.getItems().clear();
        livFiles.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        if (txtMagnetURI.getText().contains("magnet:?xt=urn:btih:") && DIRECTORY_SELECTED)
            this.btnAddPartsofTorrent.setDisable(false);
    }

    @FXML
    private void handleOnAddSelectedParts(ActionEvent ae){
        if(chbDownloadAll.isSelected() || livFiles.getSelectionModel().getSelectedItems().size() >= 1){
            btnStartDownload.setDisable(false);
        } else {
            this.showWarning("Wählen Sie Elemente",
                    "Um Datein zum Download hinzuzufügen, müssen Sie zuerst Elemente aus der Listbox auswählen, bzw." +
                            "die rechte Box markieren.",
                    false);
        }
    }

    @FXML
    private void handleOnStartDownload (ActionEvent ae){
        //new Thread(this::ActualWorkingTorrentInvocation).start();
        //  Works sometimes, but needs review in class files
        //  FixMe:  Check multiple folders and task manager for downloads, also test upper implementation

        //new Thread(this::ownTorrentImplementation).start();
        //  Does not work, prints errors

        new Thread(this::AtomashpolskiyExample).start();
        //  Works, prints status warnings
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

    @FXML
    public void handleOnTorrentFileSelected(ActionEvent actionEvent) {
    }

    @FXML
    public void handleOnUseMagnetURI(ActionEvent actionEvent) {
        chbUseMagnetURI.setDisable(true);
        txtMagnetURI.setDisable(false);

        txtTorrentFile.setDisable(true);
        chbUseTorrentFile.setDisable(false);
        chbUseTorrentFile.setSelected(false);
    }

    @FXML
    public void handleOnUseTorrentFile(ActionEvent actionEvent) {
        chbUseTorrentFile.setDisable(true);
        txtTorrentFile.setDisable(false);

        txtMagnetURI.setDisable(true);
        chbUseMagnetURI.setDisable(false);
        chbUseMagnetURI.setSelected(false);
    }


    @FXML
    public void handleOnClickedbtnSelectTorrentFile(ActionEvent actionEvent) {
    }

    @FXML
    public void handleOnClickedUseDefaultPort(ActionEvent actionEvent) {
        if (chbDefaultPort.isSelected()){
            txtPort.setDisable(true);
        } else {
            txtPort.setDisable(false);
        }
    }

    @FXML
    public void handleOnClickedSeedAfterDownload(ActionEvent actionEvent){

    }

    @FXML
    public void handleOnDirectorySelected(ActionEvent actionEvent){
        if (isDirectoryValid(new File(txtDownloadLocation.getText()))){
            DOWNLOAD_DIRECTORY = txtDownloadLocation.getText();
            DIRECTORY_SELECTED = true;
            this.onDirectorySelected(actionEvent);
        } else {
            this.showWarning("Bitte Verzeichnis wählen",
                    "Um fortzufahren wählen Sie bitte ein gültiges Verzeichnis.",
                    false);
        }
    }

    private boolean isDirectoryValid(File directory){
        return directory.isDirectory() && exists(directory.toPath()) && exists(directory.toPath());
    }

    public Stage getParentStage() {
        return parentStage;
    }
    //endregion Pichler part
}
