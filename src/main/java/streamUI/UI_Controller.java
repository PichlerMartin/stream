package streamUI;

import bt.Bt;
import bt.data.Storage;
import bt.data.file.FileSystemStorage;
import bt.dht.DHTConfig;
import bt.dht.DHTModule;
import bt.runtime.BtClient;
import bt.runtime.Config;
import bt.torrent.TorrentRegistry;
import client.StreamClient;
import com.google.inject.Module;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import meta.Globals;
import org.apache.commons.io.FilenameUtils;
import sun.util.locale.LocaleUtils;

import java.io.File;
import java.lang.reflect.Array;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;
import java.util.prefs.Preferences;

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
    private Label lblAddTorrent;

    @FXML
    private Label lblMagnetURI;

    @FXML
    private Label lblTorrentFile;

    @FXML
    private Label lblFileLocation;

    @FXML
    private Label lblOptions;

    @FXML
    private Label lblDownloadAll;

    @FXML
    private Label lblSeedLater;

    @FXML
    private Label lblUseDefaultPort;

    @FXML
    private Button btnStartDownload;

    @FXML
    private Button btnAddPartsofTorrent;

    @FXML
    private Button btnSelectTorrentFile;

    @FXML
    private Label lblSettings;

    @FXML
    private Label lblLanguage;

    @FXML
    private Label lblDarkMode;

    @FXML
    private Label lblDefaultDirectory;

    @FXML
    private TextField txtDefaultDirectory;

    @FXML
    private Button btnDefaultDirectory;

    @FXML
    private CheckBox chbDefaultPort;

    @FXML
    private CheckBox chbDownloadAll;

    @FXML
    private CheckBox chbUseTorrentFile;

    @FXML
    private CheckBox chbUseMagnetURI;

    @FXML
    private ListView<String> livFiles;

    @FXML
    private VBox VBoxTorrents;

    @FXML
    private VBox VBoxDownloadingTorrents;

    @FXML
    private VBox VBoxUploadingTorrents;

    @FXML
    private VBox VBoxFinishedTorrents;

    @FXML
    ComboBox<String> cboxSelectLanguage;

    @FXML
    private TableView TVTorrentsList;

    @FXML
    private TableView TVDownloadingTorrentsList;

    @FXML
    private TableView TVUploadingTorrentsList;

    @FXML
    private TableView TVFinishedTorrentsList;

    @FXML
    private GridPane GPAddTorrent;

    @FXML
    private GridPane GPSettings;

    private Map<String, Boolean> Controls = new HashMap<>();

    private Locale[] supportedLocales = {
            Locale.GERMAN,
            Locale.ENGLISH
    };

    private Preferences pref = Preferences.userRoot().node(this.getClass().getName());

    @SuppressWarnings("Duplicates")
    @Override
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        int maxLength = 6;

        //  forces the field txtPort to be numeric only
        //  https://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx
        txtPort.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtPort.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        //  https://stackoverflow.com/questions/15159988/javafx-2-2-textfield-maxlength
        txtPort.textProperty().addListener((ov, oldValue, newValue) -> {
            if (txtPort.getText().length() > maxLength) {
                String s = txtPort.getText().substring(0, maxLength);
                txtPort.setText(s);
            }
        });

        Controls.put(txtTorrentFile.getId(), txtTorrentFile.isDisabled());
        Controls.put(txtDownloadLocation.getId(), txtDownloadLocation.isDisabled());
        Controls.put(txtMagnetURI.getId(), txtMagnetURI.isDisabled());
        Controls.put(txtPort.getId(), txtPort.isDisabled());
        Controls.put(btnSelectTorrentFile.getId(), btnSelectTorrentFile.isDisabled());
        Controls.put(btnAddTorrents.getId(), btnAddTorrents.isDisabled());
        Controls.put(btnStartDownload.getId(), btnStartDownload.isDisabled());
        Controls.put(chbUseTorrentFile.getId(), chbUseTorrentFile.isDisabled());
        Controls.put(chbDefaultPort.getId(), chbDefaultPort.isDisabled());
        Controls.put(chbDownloadAll.getId(), chbDownloadAll.isDisabled());
        Controls.put(chbUseMagnetURI.getId(), chbUseMagnetURI.isDisabled());
        Controls.put(livFiles.getId(), livFiles.isDisabled());
    }

    @FXML
    public void handleOnClickedbtnTorrents() {
        resetVisibility();
        VBoxTorrents.setVisible(true);

        //TVTorrentsList.getItems().add(new Torrent("1", "done", "Torrent 1", "100%", "500 Mb"));
    }

    @FXML
    public void handleOnClickedbtnAddTorrents () {
        resetVisibility();
        GPAddTorrent.setVisible(true);
    }

    @FXML
    public void handleOnClickedbtnDownloading () {
        resetVisibility();
        VBoxDownloadingTorrents.setVisible(true);

        //TVDownloadingTorrentsList.getItems().add(new Torrent("2", "downloading", "Torrent 1", "100%", "500 Mb"));
    }

    @FXML
    public void handleOnClickedbtnUploading () {
        resetVisibility();
        VBoxUploadingTorrents.setVisible(true);

        //TVUploadingTorrentsList.getItems().add(new Torrent("3", "uploading", "Torrent 1", "100%", "500 Mb"));
    }

    @FXML
    public void handleOnClickedbtnFinished () {

        resetVisibility();
        VBoxFinishedTorrents.setVisible(true);

        //TVFinishedTorrentsList.getItems().add(new Torrent("4", "finished", "Torrent 1", "100%", "500 Mb"));
    }

    private void initializeTableView (TableView tableView, ResourceBundle labels) {

        tableView.setStyle("-fx-background-color: transparent; -fx-base: None; -fx-font-size: 17; -fx-alignment: center");

        if (tableView.getId().equals(TVTorrentsList.getId())) {
            tableView.setPlaceholder(new Label(labels.getString("TVTorrents_NoContent")));
        } else if (tableView.getId().equals(TVDownloadingTorrentsList.getId())) {
            tableView.setPlaceholder(new Label(labels.getString("TVDownloading_NoContent")));
        } else if (tableView.getId().equals(TVUploadingTorrentsList.getId())) {
            tableView.setPlaceholder(new Label(labels.getString("TVUploading_NoContent")));
        } else if (tableView.getId().equals(TVFinishedTorrentsList.getId())) {
            tableView.setPlaceholder(new Label(labels.getString("TVFinished_NoContent")));
        }

        tableView.setVisible(true);

        TableColumn t1 = new TableColumn(labels.getString("TVNumber"));
        TableColumn t2 = new TableColumn(labels.getString("TVStatus"));
        TableColumn t3 = new TableColumn(labels.getString("TVName"));
        TableColumn t4 = new TableColumn(labels.getString("TVProgress"));
        TableColumn t5 = new TableColumn(labels.getString("TVSize"));

        t1.setPrefWidth(80);
        t1.setStyle("-fx-alignment: center");
        t2.setPrefWidth(100);
        t2.setStyle("-fx-alignment: center");
        t3.setPrefWidth(220);
        t3.setStyle("-fx-alignment: center");
        t4.setPrefWidth(95);
        t4.setStyle("-fx-alignment: center");
        t5.setPrefWidth(105);
        t5.setStyle("-fx-alignment: center");

        tableView.getColumns().clear();
        tableView.getColumns().addAll(t1, t2, t3, t4,t5);

        t1.setCellValueFactory(new PropertyValueFactory<>("number"));
        t2.setCellValueFactory(new PropertyValueFactory<>("status"));
        t3.setCellValueFactory(new PropertyValueFactory<>("name"));
        t4.setCellValueFactory(new PropertyValueFactory<>("progress"));
        t5.setCellValueFactory(new PropertyValueFactory<>("size"));
    }

    @FXML
    public void handleOnClickedbtnSettings () {
        resetVisibility();
        GPSettings.setVisible(true);
        txtDefaultDirectory.setText(pref.get("DefaultDirectory", ""));
    }

    @FXML
    public void handleOnClickedbtnHelp () {
    }

    private void initalizecboxSelectLanguage () {
        for (Locale loc: supportedLocales) {
            cboxSelectLanguage.getItems().add(loc.getDisplayName());
        }

        Locale currentLoc = Locale.forLanguageTag(pref.get("language", Locale.GERMAN.toString()));
        cboxSelectLanguage.getSelectionModel().select(currentLoc.getDisplayName());
    }

    @FXML
    public void handleOnClickedCbox () {
        for (Locale loc: supportedLocales) {
            if (loc.getDisplayName().equals(cboxSelectLanguage.getValue())) {
                changeLanguage(loc);
                pref.put("language", loc.toString());
            }
        }
    }

    @FXML
    public void handleOnClickedbtnStorageLocation () {

        Locale currentLocale = Locale.forLanguageTag(pref.get("language", Locale.GERMAN.toString()));
        ResourceBundle labels = ResourceBundle.getBundle("ResourceBundle", currentLocale);

        File directory = this.openFileDialog(labels.getString("StorageLocSaveAs"), FileDialogType.DIRECTORY);

        if (this.isDirectoryValid(directory)){
            txtDownloadLocation.setText(directory.toString());

            if (this.checkIfDownloadCanBeInitialized()) prepareDownload();
        } else {
            this.showWarning(labels.getString("DirectoryWarningH"),
                    labels.getString("DirectoryWarningM"));
        }
    }

    private void showWarning(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        Locale currentLocale = Locale.forLanguageTag(pref.get("language", Locale.GERMAN.toString()));
        ResourceBundle labels = ResourceBundle.getBundle("ResourceBundle", currentLocale);
        alert.setTitle(labels.getString("WarningTitle"));
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.show();
    }

    private void resetVisibility() {
        VBoxTorrents.setVisible(false);
        VBoxDownloadingTorrents.setVisible(false);
        VBoxUploadingTorrents.setVisible(false);
        VBoxFinishedTorrents.setVisible(false);
        GPAddTorrent.setVisible(false);
        GPSettings.setVisible(false);

        GPAddTorrent.getChildren().forEach(node -> {
            if (Controls.containsKey((node.getId()))){
                node.setDisable(Controls.get(node.getId()));
            }
        });
    }

    void setStage(Stage CurrentStage) {
        this.stage = CurrentStage;
    }

    void setParentStage(Stage root) {

        String prefLanguage = pref.get("language", Locale.GERMAN.toString());
        Locale currentLocale = Locale.forLanguageTag(prefLanguage);

        this.parentStage = root;
        handleOnClickedbtnTorrents();
        initalizecboxSelectLanguage();

        changeLanguage(currentLocale);
    }

    /**
     * This method is called by buttons, which have been entered
     * It changes the style and the colour of the text in the caller button
     * @param event: is used to retrieve the source of the event
     */
    @FXML
    public void handleOnMenuEntryEntered (MouseEvent event) {

        Button btnEnteredBtn = (Button)event.getSource();
        btnEnteredBtn.setStyle(btnEnteredBtn.getStyle() + "; -fx-underline: true; -fx-text-fill:  #1b3957");

    }

    /**
     * This method changes the style and colour of the caller button back to the original state
     * If the mouse leaves one of the buttons, this method is called
     * @param event: is used to retrieve the source of the event
     */
    @FXML
    public void handleOnMenuEntryLeft (MouseEvent event) {

        Button btnLeftbtn = (Button)event.getSource();
        btnLeftbtn.setStyle(btnLeftbtn.getStyle() + "; -fx-underline: false; -fx-text-fill: white");
    }


    private void changeLanguage(Locale loc) {

        ResourceBundle labels = ResourceBundle.getBundle("ResourceBundle", loc);

        btnTorrents.setText(labels.getString("btnTorrents"));
        btnAddTorrents.setText(labels.getString("btnAddTorrents"));
        btnDownloading.setText(labels.getString("btnDownloading"));
        btnUploading.setText(labels.getString("btnUploading"));
        btnFinished.setText(labels.getString("btnFinished"));
        btnSettings.setText(labels.getString("btnSettings"));
        btnHelp.setText(labels.getString("btnHelp"));

        lblAddTorrent.setText(labels.getString("lblAddTorrent"));
        lblMagnetURI.setText(labels.getString("lblMagnetURI"));
        lblTorrentFile.setText(labels.getString("lblTorrentFile"));
        lblFileLocation.setText(labels.getString("lblFileLocation"));
        btnAddPartsofTorrent.setText(labels.getString("btnAddPartsofTorrent"));
        lblOptions.setText(labels.getString("lblOptions"));
        lblDownloadAll.setText(labels.getString("lblDownloadAll"));
        lblSeedLater.setText(labels.getString("lblSeedLater"));
        lblUseDefaultPort.setText(labels.getString("lblUseDefaultPort"));
        txtPort.setPromptText(labels.getString("phAlternativePort"));
        btnStartDownload.setText(labels.getString("btnStartDownload"));

        lblSettings.setText(labels.getString("lblSettings"));
        lblLanguage.setText(labels.getString("lblLanguage"));
        lblDarkMode.setText(labels.getString("lblDarkMode"));
        lblDefaultDirectory.setText(labels.getString("lblDefaultDirectory"));

        initializeTableView(TVTorrentsList, labels);
        initializeTableView(TVDownloadingTorrentsList, labels);
        initializeTableView(TVUploadingTorrentsList, labels);
        initializeTableView(TVFinishedTorrentsList, labels);
    }

    @FXML
    public void handleOnClickedbtnDefaultDirectory () {

        Locale currentLocale = Locale.forLanguageTag(pref.get("language", Locale.GERMAN.toString()));
        ResourceBundle labels = ResourceBundle.getBundle("ResourceBundle", currentLocale);
        File directory = this.openFileDialog(labels.getString("StorageLocSaveAs"), FileDialogType.DIRECTORY);

        if (this.isDirectoryValid(directory)){

            txtDefaultDirectory.setText(directory.toString());
            pref.put("DefaultDirectory", directory.toString());

        } else {

            this.showWarning(labels.getString("DirectoryWarningH"),
                    labels.getString("DirectoryWarningM"));

        }
    }

    //region Pichler part

    @FXML
    private void handleOnAddSelectedParts(){
        Locale currentLocale = Locale.forLanguageTag(pref.get("language", Locale.GERMAN.toString()));
        ResourceBundle labels = ResourceBundle.getBundle("ResourceBundle", currentLocale);

        if(chbDownloadAll.isSelected() || livFiles.getSelectionModel().getSelectedItems().size() >= 1){
            btnStartDownload.setDisable(false);
        } else {
            this.showWarning(labels.getString("SelectedPartsWarningH"),
                    labels.getString("SelectedPartsWarningM"));
        }
    }

    @FXML
    public void handleOnDirectorySelected(){
        Locale currentLocale = Locale.forLanguageTag(pref.get("language", Locale.GERMAN.toString()));
        ResourceBundle labels = ResourceBundle.getBundle("ResourceBundle", currentLocale);

        if (isDirectoryValid(new File(txtDownloadLocation.getText()))){
            if (checkIfDownloadCanBeInitialized())prepareDownload();
        } else {
            this.showWarning(labels.getString("DirectoryWarningH"),
                    labels.getString("DirectoryWarningM"));
        }
    }

    @FXML
    public void handleOnClickedbtnSelectTorrentFile() {

        Locale currentLocale = Locale.forLanguageTag(pref.get("language", Locale.GERMAN.toString()));
        ResourceBundle labels = ResourceBundle.getBundle("ResourceBundle", currentLocale);
        File torrentfile = this.openFileDialog(labels.getString("SelectTorrentFile"), FileDialogType.TORRENT);

        if (this.isTorrentFileValid(torrentfile)){
            txtTorrentFile.setText(torrentfile.toString());
            txtDownloadLocation.requestFocus();

            if (checkIfDownloadCanBeInitialized())prepareDownload();
        } else {
            this.showWarning(labels.getString("SelectTorrentWarningH"),
                    labels.getString("SelectTorrentWarningM"));
        }
    }

    @FXML
    public void handleOnTorrentFileSelected() {
        Locale currentLocale = Locale.forLanguageTag(pref.get("language", Locale.GERMAN.toString()));
        ResourceBundle labels = ResourceBundle.getBundle("ResourceBundle", currentLocale);
        File torrentfile = new File(txtTorrentFile.getText());

        if (this.isTorrentFileValid(torrentfile)) {
            txtDownloadLocation.requestFocus();

            if (checkIfDownloadCanBeInitialized())prepareDownload();
        }
        else {
            this.showWarning(labels.getString("SelectTorrentWarningH"),
                    labels.getString("SelectTorrentWarningM"));
        }
    }

    @FXML
    public void handleOnMagnetURIEntered(){
        Locale currentLocale = Locale.forLanguageTag(pref.get("language", Locale.GERMAN.toString()));
        ResourceBundle labels = ResourceBundle.getBundle("ResourceBundle", currentLocale);

        if (isMagnetLinkValid(txtMagnetURI.getText())) {
            txtDownloadLocation.requestFocus();

            if (checkIfDownloadCanBeInitialized())prepareDownload();
        }
        else {
            showWarning(labels.getString("MagnetURIWarningH"),
                    labels.getString("MagnetURIWarningM"));
        }
    }

    @FXML
    private void onDirectorySelected(){
        livFiles.getItems().clear();
        livFiles.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        this.btnAddPartsofTorrent.setDisable(false);
    }

    @FXML
    private void handleOnStartDownload (){
        // new Thread(this::ownTorrentImplementation).start();
        // Does not work, prints errors
        // Update 30.12.2019, 18:25: now works

        new Thread(this::AtomashpolskiyExample).start();
        // Works, prints status warnings

    }

    /**
     * Description
     * Diese Methode besteht aus dem Example welche von dem Ersteller der Bt-Bibliothek, atomashpolskiy,
     * zur Verf&uuml;gnug gestellt wurde.
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

        // create file system based backend for torrent data
        Storage storage = new FileSystemStorage(Paths.get(DOWNLOAD_DIRECTORY));

        BtClient client = Bt.client()
                .config(config)
                .storage(storage)
                .magnet(MAGNET_LINK)
                .autoLoadModules()
                .module(dhtModule)
                .stopWhenDownloaded()
                .build();

        client.startAsync().join();
    }

    @Deprecated
    public void ownTorrentImplementation() {
        StreamClient.main(new String[]{"-d", Globals.DOWNLOAD_DIRECTORY, "-m", Globals.MAGNET_LINK});
    }

    @FXML
    public void handleOnUseMagnetURI() {
        chbUseMagnetURI.setDisable(true);
        txtTorrentFile.setDisable(true);
        btnSelectTorrentFile.setDisable(true);
        USE_MAGNET_LINK =true;

        USE_TORRENT_FILE =false;
        txtMagnetURI.setDisable(false);
        chbUseTorrentFile.setDisable(false);
        chbUseTorrentFile.setSelected(false);

        if (checkIfDownloadCanBeInitialized())prepareDownload();
    }

    @FXML
    public void handleOnUseTorrentFile() {
        chbUseTorrentFile.setDisable(true);
        txtMagnetURI.setDisable(true);
        USE_TORRENT_FILE =true;

        USE_MAGNET_LINK =true;
        btnSelectTorrentFile.setDisable(false);
        txtTorrentFile.setDisable(false);
        chbUseMagnetURI.setDisable(false);
        chbUseMagnetURI.setSelected(false);

        if (checkIfDownloadCanBeInitialized())prepareDownload();
    }

    private File openFileDialog(String header, FileDialogType type) {
        if (type.equals(FileDialogType.DIRECTORY)){
            DirectoryChooser dirChooser = new DirectoryChooser();
            dirChooser.setTitle(header);

            File defaultDir = new File(pref.get("DefaultDirectory", "user.home"));
            if(!defaultDir.exists()) {
                defaultDir = new File("user.home");
            }

            dirChooser.setInitialDirectory(new File(pref.get("DefaultDirectory", "user.home")));
            return dirChooser.showDialog(stage);
        } else if (type.equals(FileDialogType.TORRENT)){
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Torrent files (*.torrent)", "*.torrent");
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(extFilter);

            fileChooser.setTitle(header);
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            return fileChooser.showOpenDialog(stage);
        } else {
            return null;
        }
    }

    enum FileDialogType{
        DIRECTORY,
        TORRENT
    }

    @FXML
    public void handleOnClickedUseDefaultPort() {
        if (chbDefaultPort.isSelected()){
            txtPort.setDisable(true);
            USE_DEFAULT_PORT = true;
        } else {
            txtPort.setDisable(false);
            USE_DEFAULT_PORT = false;
        }
    }

    @FXML
    public void handleOnClickedSeedAfterDownload(){
        SEED_AFTER_DOWNLOAD = !SEED_AFTER_DOWNLOAD;
    }

    private boolean isDirectoryValid(File directory){
        return directory != null && !directory.getPath().equals("") && directory.isDirectory() && exists(directory.toPath()) && exists(directory.toPath());
    }

    private boolean isTorrentFileValid(File torrentfile) {
        return torrentfile != null && torrentfile.exists() && (FilenameUtils.getExtension(torrentfile.getPath()).equals("torrent"));
    }

    private boolean isMagnetLinkValid(String magnetlink){
        return magnetlink.contains("magnet:?xt=urn:btih:");
    }

    private boolean checkIfDownloadCanBeInitialized(){
        String torrentfile = txtTorrentFile.getText();
        String magnetlink = txtMagnetURI.getText();
        String directory = txtDownloadLocation.getText();

        if (this.isTorrentFileValid(new File(torrentfile)) && this.isDirectoryValid(new File(directory))){
            return true;
        } else return this.isMagnetLinkValid(magnetlink) && this.isDirectoryValid(new File(directory));

    }

    private void prepareDownload(){
        MAGNET_LINK = txtMagnetURI.getText();
        TORRENT_FILE = txtTorrentFile.getText();
        DOWNLOAD_DIRECTORY = txtDownloadLocation.getText();
        DIRECTORY_SELECTED = true;

        this.onDirectorySelected();
    }
    //endregion Pichler part
}
