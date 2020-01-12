package streamUI;

import bt.Bt;
import bt.data.Storage;
import bt.data.file.FileSystemStorage;
import bt.dht.DHTConfig;
import bt.dht.DHTModule;
import bt.metainfo.TorrentFile;
import bt.runtime.BtClient;
import bt.runtime.Config;
import client.StreamClient;
import com.google.inject.Module;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.math.NumberUtils;
import support.Globals;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import static java.lang.String.format;
import static java.lang.String.join;
import static java.nio.file.Files.exists;
import static support.Globals.*;

public class UI_Controller_main_page implements Initializable {

    @FXML
    ComboBox<String> cboxSelectLanguage;
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
    private Button btnAbout;

    @FXML
    private Label lblAddTorrent;

    @FXML
    private Label lblFileLocation;

    @FXML
    private Label lblOptions;

    @FXML
    private Label lblDownloadAll;

    @FXML
    private Label lblSeedLater;

    @FXML
    private RadioButton rdoUseMagnetURI;

    @FXML
    private RadioButton rdoUseTorrentFile;

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
    private CheckBox chbDefaultPort;

    @FXML
    private Label lblDefaultDirectory;

    @FXML
    private CheckBox chbDownloadAll;

    @FXML
    private TextField txtDefaultDirectory;

    @FXML
    private ToggleButton togDarkMode;

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
    private TableView<Object> TVTorrentsList;

    @FXML
    private TableView<Object> TVDownloadingTorrentsList;

    @FXML
    private TableView<Object> TVUploadingTorrentsList;

    @FXML
    private TableView<Object> TVFinishedTorrentsList;

    @FXML
    private GridPane GPAddTorrent;

    @FXML
    private GridPane GPSettings;

    @FXML
    private GridPane GPAbout;

    @FXML
    private Label lblAbout;

    @FXML
    private Label lblTimeUntilRelease;

    @FXML
    private Label lblDays;

    @FXML
    private Label lblHours;

    @FXML
    private Label lblMinutes;

    @FXML
    private Label lblSeconds;

    @FXML
    private Label lblDaysUntilRelease;

    @FXML
    private Label lblHoursUntilRelease;

    @FXML
    private Label lblMinutesUntilRelease;

    @FXML
    private Label lblSecondsUntilRelease;

    @FXML
    private Label lblWhatIsStream;

    @FXML
    private Label lblAboutStream;

    @FXML
    private ScrollPane sPaneAboutStream;

    @FXML
    private AnchorPane aPaneAboutStream;

    private Map<String, Boolean> Controls = new HashMap<>();

    private Locale[] supportedLocales = {
            Locale.GERMAN,
            Locale.ENGLISH,
            Locale.FRENCH,
            new Locale("en")
    };

    @Deprecated
    public void putFileNameAndChoice(TorrentFile file) {
        livFiles.getItems().add(format("%s", join("/", file.getPathElements())));
        livFiles.getSelectionModel().select(format("%s", join("/", file.getPathElements())));
    }

    private Preferences pref = Preferences.userNodeForPackage(getClass());

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
        Controls.put(rdoUseTorrentFile.getId(), rdoUseTorrentFile.isDisabled());
        Controls.put(chbDefaultPort.getId(), chbDefaultPort.isDisabled());
        Controls.put(chbDownloadAll.getId(), chbDownloadAll.isDisabled());
        Controls.put(rdoUseMagnetURI.getId(), rdoUseMagnetURI.isDisabled());
        Controls.put(livFiles.getId(), livFiles.isDisabled());
    }

    /**
     * function is called if user clicks on menu entry 'Torrents'
     * sets the visibility of the relevant Vbox true
     */
    @FXML
    public void handleOnClickedbtnTorrents() {
        resetVisibility();
        VBoxTorrents.setVisible(true);

        //TVTorrentsList.getItems().add(new Torrent("1", "done", "Torrent 1", "100%", "500 Mb"));
    }

    /**
     * function is called if user clicks on menu entry 'Add Torrents'
     * sets the visibility of the relevant Vbox true
     */
    @FXML
    public void handleOnClickedbtnAddTorrents() {
        resetVisibility();
        GPAddTorrent.setVisible(true);
    }

    /**
     * function is called if user clicks on menu entry 'Downloading'
     * sets the visibility of the relevant Vbox true
     */
    @FXML
    public void handleOnClickedbtnDownloading() {
        resetVisibility();
        VBoxDownloadingTorrents.setVisible(true);

        //TVDownloadingTorrentsList.getItems().add(new Torrent("2", "downloading", "Torrent 1", "100%", "500 Mb"));
    }

    /**
     * function is called if user clicks on menu entry 'Uploading'
     * sets the visibility of the relevant Vbox true
     */
    @FXML
    public void handleOnClickedbtnUploading() {
        resetVisibility();
        VBoxUploadingTorrents.setVisible(true);

        //TVUploadingTorrentsList.getItems().add(new Torrent("3", "uploading", "Torrent 1", "100%", "500 Mb"));
    }

    /**
     * function is called if user clicks on menu entry 'Finished'
     * sets the visibility of the relevant Vbox true
     */
    @FXML
    public void handleOnClickedbtnFinished () {

        resetVisibility();
        VBoxFinishedTorrents.setVisible(true);

        //TVFinishedTorrentsList.getItems().add(new Torrent("4", "finished", "Torrent 1", "100%", "500 Mb"));
    }

    /**
     * function is used for initializing the given tableView
     * can also be used for changing the language of columns and the placeholder
     * @param tableView given tableView which is modified in this method
     * @param labels contains the values in a given language for the columns and placeholders
     */
    private void initializeTableView (TableView<Object> tableView, ResourceBundle labels) {

        tableView.setStyle("-fx-background-color: transparent; -fx-base: None; -fx-font-size: 17; -fx-alignment: center");

        //checks which tableView is given and sets the matching placeholder
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

        //create new columns in the given language
        TableColumn<Object, Object> t1 = new TableColumn<>(labels.getString("TVNumber"));
        TableColumn<Object, Object> t2 = new TableColumn<>(labels.getString("TVStatus"));
        TableColumn<Object, Object> t3 = new TableColumn<>(labels.getString("TVName"));
        TableColumn<Object, Object> t4 = new TableColumn<>(labels.getString("TVProgress"));
        TableColumn<Object, Object> t5 = new TableColumn<>(labels.getString("TVSize"));

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
        //noinspection unchecked
        tableView.getColumns().addAll(t1, t2, t3, t4, t5);

        /*
            ToDo:   add functionality of checkboxes in torrent window
         */

        t1.setCellValueFactory(new PropertyValueFactory<>("number"));
        t2.setCellValueFactory(new PropertyValueFactory<>("status"));
        t3.setCellValueFactory(new PropertyValueFactory<>("name"));
        t4.setCellValueFactory(new PropertyValueFactory<>("progress"));
        t5.setCellValueFactory(new PropertyValueFactory<>("size"));
    }

    /**
     * function is called if user clicks on menu entry 'Settings'
     * sets the visibility of the relevant Gridpane true
     * writes the default download directory in the textbox
     */
    @FXML
    public void handleOnClickedbtnSettings() {

        resetVisibility();
        GPSettings.setVisible(true);
        txtDefaultDirectory.setText(pref.get("DefaultDirectory", ""));
    }

    /**
     * function is called if user clicks on toggleButton 'Dark mode'
     * ToDo: implement .css files for light and dark mode
     */
    @FXML
    public void handleOnClickedtogDarkMode() {
        Locale currentLocale = Locale.forLanguageTag(pref.get("language", Locale.GERMAN.toString()));
        ResourceBundle labels = ResourceBundle.getBundle("ResourceBundle", currentLocale);

        if (togDarkMode.isSelected()) {
            //enable dark mode
            togDarkMode.setText(labels.getString("togDarkModeOn"));
        } else {
            //disable dark mode
            togDarkMode.setText(labels.getString("togDarkModeOff"));
        }
    }

    /**
     * function is called if user clicks on menu entry 'About'
     * ToDo: give a short overview over the application and describe how a torrent works and how it can be downloaded or seeded
     */
    @FXML
    public void handleOnClickedbtnAbout () {
        resetVisibility();
        GPAbout.setVisible(true);
    }

    /**
     * function fills the comboBox with the supported languages
     * supported languages are stored in the the array supportedLocales
     * the function also automatically selects the language which has been selected last time
     */
    private void initalizecboxSelectLanguage () {
        for (Locale loc: supportedLocales) {
            cboxSelectLanguage.getItems().add(loc.getDisplayName());
        }

        Locale currentLoc = Locale.forLanguageTag(pref.get("language", Locale.GERMAN.toString()));
        cboxSelectLanguage.getSelectionModel().select(currentLoc.getDisplayName());
    }

    /**
     * function is called if user decides to change language and selects a different language
     * checks which language has been selected and then calls the function changeLanguage()
     * writes the language which is currently used in the preferences
     */
    @FXML
    public void handleOnClickedCbox() {
        for (Locale loc : supportedLocales) {
            if (loc.getDisplayName().equals(cboxSelectLanguage.getValue())) {
                changeLanguage(loc);
                pref.put("language", loc.toString());
            }
        }
    }

    /**
     * function is called if user clicks on the button '...' to change the download directory
     * if the user selects a valid directory the function prepareDownload() is called
     * otherwise the function showWarning() is called, which, surprise surprise, shows a given warning
     */
    @FXML
    public void handleOnClickedbtnStorageLocation() {

        Locale currentLocale = Locale.forLanguageTag(pref.get("language", Locale.GERMAN.toString()));
        ResourceBundle labels = ResourceBundle.getBundle("ResourceBundle", currentLocale);

        File directory = this.openFileDialog(labels.getString("StorageLocSaveAs"), FileDialogType.DIRECTORY);

        if (this.isDirectoryValid(directory)) {
            txtDownloadLocation.setText(directory.toString());

            if (this.checkIfDownloadCanBeInitialized()) prepareDownload();
        } else {
            this.showWarning(labels.getString("DirectoryWarningH"),
                    labels.getString("DirectoryWarningM"));
        }
    }

    /**
     * function is called if the user selects a invalid directory or if he closes the
     * @param header contains the header, which should be shown in the alert
     * @param message contains the message, which should be shown in the alert
     */
    private void showWarning(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        Locale currentLocale = Locale.forLanguageTag(pref.get("language", Locale.GERMAN.toString()));
        ResourceBundle labels = ResourceBundle.getBundle("ResourceBundle", currentLocale);
        alert.setTitle(labels.getString("WarningTitle"));
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.show();
    }

    /**
     * function sets the visibility of all Vboxes and GridPanes to false
     */
    private void resetVisibility() {
        VBoxTorrents.setVisible(false);
        VBoxDownloadingTorrents.setVisible(false);
        VBoxUploadingTorrents.setVisible(false);
        VBoxFinishedTorrents.setVisible(false);
        GPAddTorrent.setVisible(false);
        GPSettings.setVisible(false);
        GPAbout.setVisible(false);

        GPAddTorrent.getChildren().forEach(node -> {
            if (Controls.containsKey((node.getId()))) {
                node.setDisable(Controls.get(node.getId()));
            }
        });
    }

    void setStage(Stage CurrentStage) {
        this.stage = CurrentStage;
    }

    /**
     * function sets the parent stage, gets the preferred language and changes the text of the
     * ToDo: comment, modify resourceBundles for clock
     * @param root: root stage
     */
    void setParentStage(Stage root) {
        String prefLanguage = pref.get("language", Locale.GERMAN.toString());
        Locale currentLocale = Locale.forLanguageTag(prefLanguage);

        this.parentStage = root;
        handleOnClickedbtnTorrents();
        initalizecboxSelectLanguage();
        initializeSPane(sPaneAboutStream);
        root.setOnShown(e ->
                sPaneAboutStream.lookup(".scroll-pane").lookup(".viewport").setStyle("-fx-background-color:  #transparent; -fx-blend-mode: src-over"));
        aPaneAboutStream.setStyle("-fx-background-color:  #transparent;");

        changeLanguage(currentLocale);

        //changes the timer until final release
        Timeline timerUntilRelease = new Timeline(new KeyFrame(Duration.seconds(1), event -> {

            LocalDateTime localDateTime = LocalDateTime.now();
            LocalDateTime releaseDate = LocalDateTime.of(2020, 4, 3, 10, 0, 0);

            LocalDateTime tempDateTime = LocalDateTime.from(localDateTime);

            String days = String.valueOf(tempDateTime.until(releaseDate, ChronoUnit.DAYS));
            tempDateTime = tempDateTime.plusDays(Long.valueOf(days));

            String hours = String.valueOf(tempDateTime.until(releaseDate, ChronoUnit.HOURS));
            tempDateTime = tempDateTime.plusHours(Long.valueOf(hours));

            String minutes = String.valueOf(tempDateTime.until(releaseDate, ChronoUnit.MINUTES));
            tempDateTime = tempDateTime.plusMinutes(Long.valueOf(minutes));

            String seconds = String.valueOf(tempDateTime.until(releaseDate, ChronoUnit.SECONDS));

            if (days.length() < 2) { days = "0" + days;}
            if (hours.length() < 2) { hours = "0" + hours;}
            if (minutes.length() < 2) { minutes = "0" + minutes;}
            if (seconds.length() < 2) { seconds = "0" + seconds;}

            lblDaysUntilRelease.setText(days);
            lblHoursUntilRelease.setText(hours);
            lblMinutesUntilRelease.setText(minutes);
            lblSecondsUntilRelease.setText(seconds);

        }));
        timerUntilRelease.setCycleCount(Timeline.INDEFINITE);
        timerUntilRelease.play();
    }

    private void initializeSPane(ScrollPane sPane) {
        sPane.setPrefSize(650, 380);
        AnchorPane.setTopAnchor(sPane, 0.);
        AnchorPane.setRightAnchor(sPane, 0.);
        AnchorPane.setBottomAnchor(sPane, 0.);
        AnchorPane.setLeftAnchor(sPane, 0.);
    }

    /**
     * This method is called by buttons, which have been entered
     * It changes the style and the colour of the text in the caller button
     *
     * @param event: is used to retrieve the source of the event
     */
    @FXML
    public void handleOnMenuEntryEntered(MouseEvent event) {

        Button btnEnteredBtn = (Button) event.getSource();
        btnEnteredBtn.setStyle(btnEnteredBtn.getStyle() + "; -fx-underline: true; -fx-text-fill:  #1b3957");

    }

    /**
     * This method changes the style and colour of the caller button back to the original state
     * If the mouse leaves one of the buttons, this method is called
     *
     * @param event: is used to retrieve the source of the event
     */
    @FXML
    public void handleOnMenuEntryLeft(MouseEvent event) {

        Button btnLeftbtn = (Button) event.getSource();
        btnLeftbtn.setStyle(btnLeftbtn.getStyle() + "; -fx-underline: false; -fx-text-fill: white");
    }

    @FXML
    public void handleOnSelectFileEntered (MouseEvent event) {

        Button btnEnteredBtn = (Button)event.getSource();
        btnEnteredBtn.setStyle(btnEnteredBtn.getStyle() + "; -fx-background-color:  #1b3957; -fx-text-fill: white");

    }

    @FXML
    public void handleOnSelectFileLeft (MouseEvent event) {
        Button btnLeftbtn = (Button)event.getSource();
        btnLeftbtn.setStyle(btnLeftbtn.getStyle() + "; -fx-background-color: transparent; -fx-text-fill: black");
    }


    private void changeLanguage(Locale loc) {

        ResourceBundle labels = ResourceBundle.getBundle("ResourceBundle", loc);

        btnTorrents.setText(labels.getString("btnTorrents"));
        btnAddTorrents.setText(labels.getString("btnAddTorrents"));
        btnDownloading.setText(labels.getString("btnDownloading"));
        btnUploading.setText(labels.getString("btnUploading"));
        btnFinished.setText(labels.getString("btnFinished"));
        btnSettings.setText(labels.getString("btnSettings"));
        btnAbout.setText(labels.getString("btnAbout"));

        lblAddTorrent.setText(labels.getString("lblAddTorrent"));
        rdoUseMagnetURI.setText(labels.getString("rdoMagnetURI"));
        rdoUseTorrentFile.setText(labels.getString("rdoTorrentFile"));
        lblFileLocation.setText(labels.getString("lblFileLocation"));
        btnAddPartsofTorrent.setText(labels.getString("btnAddPartsofTorrent"));
        lblOptions.setText(labels.getString("lblOptions"));
        lblDownloadAll.setText(labels.getString("lblDownloadAll"));
        lblSeedLater.setText(labels.getString("lblSeedLater"));
        lblUseDefaultPort.setText(labels.getString("lblUseDefaultPort"));
        txtPort.setPromptText(labels.getString("phAlternativePort"));
        btnStartDownload.setText(labels.getString("btnStartDownload"));
        lblAbout.setText(labels.getString("btnAbout"));

        lblSettings.setText(labels.getString("lblSettings"));
        lblLanguage.setText(labels.getString("lblLanguage"));
        lblDarkMode.setText(labels.getString("lblDarkMode"));
        if (togDarkMode.isSelected()) {togDarkMode.setText(labels.getString("togDarkModeOn"));}
        else {togDarkMode.setText(labels.getString("togDarkModeOff"));}
        lblDefaultDirectory.setText(labels.getString("lblDefaultDirectory"));

        lblAbout.setText(labels.getString("lblAbout"));
        lblTimeUntilRelease.setText(labels.getString("lblTimeUntilRelease"));
        lblDays.setText(labels.getString("lblDays"));
        lblHours.setText(labels.getString("lblHours"));
        lblMinutes.setText(labels.getString("lblMinutes"));
        lblSeconds.setText(labels.getString("lblSeconds"));

        lblWhatIsStream.setText(labels.getString("lblWhatIsStream"));
        lblAboutStream.setText(labels.getString("lblAboutStream"));

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

        if(chbDownloadAll.isSelected() || livFiles.getSelectionModel().getSelectedItems().size() >= 1) {
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

        if (this.isTorrentFileValid(torrentfile)) {
            txtTorrentFile.setText(torrentfile.toString());
            txtDownloadLocation.requestFocus();

            if (checkIfDownloadCanBeInitialized()) prepareDownload();
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
    private void onDirectorySelected() {
        livFiles.getItems().clear();

        if(livFiles.isDisabled()){
            this.btnStartDownload.setDisable(false);
        } else {
            livFiles.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

            this.btnAddPartsofTorrent.setDisable(false);
        }
    }

    @FXML
    private void handleOnStartDownload() {
        new Thread(() -> StreamClient.main(new String[]{Globals.DOWNLOAD_DIRECTORY, Globals.MAGNET_LINK})).start();
        /*
        Code below calls showTorrentPartsStage(), new window is displayed
        FIXME:  Implement new window with torrent parts

        try {
            this.showTorrentPartsStage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
    }

    @Deprecated
    public void showTorrentPartsStage() throws IOException {

        Stage secondStage = new Stage();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/UI_stream_parts_page.fxml"));
        Parent root = loader.load();

        root.setStyle("-fx-background-image: url('/images/stream_UI_background.png'); -fx-background-repeat: no-repeat; -fx-background-size: 1215 765");
        UI_Controller_parts_page c = loader.getController();

        c.setStage(secondStage);
        Scene s = new Scene(root, 800, 300);
        secondStage.setTitle("stream");
        secondStage.getIcons().add(new Image("/images/streamAppIcon_blue.PNG"));
        secondStage.setScene(s);
        secondStage.setResizable(false);

        c.setParentStage(secondStage);
        secondStage.show();
    }

    /**
     * Description
     * example method provided by github user atomashpolskiy, creator of bt-library,
     * used to demonstrate the basic capabilities of the java torrent implementation
     *
     * https://github.com/atomashpolskiy/bittorrent
     */
    @Deprecated
    public void AtomashpolskiyExample() {
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

    @FXML
    public void handleOnUseMagnetURI() {

        txtTorrentFile.setDisable(true);
        btnSelectTorrentFile.setDisable(true);
        USE_MAGNET_LINK = true;

        USE_TORRENT_FILE = false;
        txtMagnetURI.setDisable(false);

        if (checkIfDownloadCanBeInitialized()) prepareDownload();
    }

    @FXML
    public void handleOnUseTorrentFile() {

        txtMagnetURI.setDisable(true);
        USE_TORRENT_FILE = true;

        USE_MAGNET_LINK = true;
        btnSelectTorrentFile.setDisable(false);
        txtTorrentFile.setDisable(false);

        if (checkIfDownloadCanBeInitialized()) prepareDownload();
    }

    private File openFileDialog(String header, FileDialogType type) {
        if (type.equals(FileDialogType.DIRECTORY)) {
            DirectoryChooser dirChooser = new DirectoryChooser();
            dirChooser.setTitle(header);

            File defaultDir = new File(pref.get("DefaultDirectory", "user.home"));
            if(!isDirectoryValid(defaultDir)) {
                defaultDir = new File(System.getProperty("user.home"));
            }

            dirChooser.setInitialDirectory(defaultDir);
            return dirChooser.showDialog(stage);

        } else if (type.equals(FileDialogType.TORRENT)) {
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

    @FXML
    public void handleOnClickedUseDefaultPort() {
        if (chbDefaultPort.isSelected()) {
            txtPort.setDisable(true);
            USE_DEFAULT_PORT = true;
        } else {
            txtPort.setDisable(false);
            USE_DEFAULT_PORT = false;
        }
    }

    @FXML
    public void handleOnClickedSeedAfterDownload() {
        SEED_AFTER_DOWNLOAD = !SEED_AFTER_DOWNLOAD;
    }

    private boolean isDirectoryValid(File directory) {
        return directory != null && !directory.getPath().equals("") && directory.isDirectory() && exists(directory.toPath()) && exists(directory.toPath());
    }

    private boolean isTorrentFileValid(File torrentfile) {
        return torrentfile != null && torrentfile.exists() && (FilenameUtils.getExtension(torrentfile.getPath()).equals("torrent"));
    }

    private boolean isMagnetLinkValid(String magnetlink) {
        return magnetlink.contains("magnet:?xt=urn:btih:");
    }

    private boolean checkIfDownloadCanBeInitialized() {
        String torrentfile = txtTorrentFile.getText();
        String magnetlink = txtMagnetURI.getText();
        String directory = txtDownloadLocation.getText();


        /*
        if-statement below is for showing the window where a list-view is displayed, so
        the user can select the files they want to download, but does not work properly
        because access of torrent parts is locked
        FIXME:  retrieve torrent parts from StreamClient.java-class

        if (this.isMagnetLinkValid(magnetlink)) {
            this.listTorrentPartsFromLink(magnetlink);
        } else if (this.isTorrentFileValid(new File(torrentfile))) {
            this.listTorrentPartsFromFile(torrentfile);
        }
        */

        if (this.isTorrentFileValid(new File(torrentfile)) && this.isDirectoryValid(new File(directory))) {
            return true;
        } else return this.isMagnetLinkValid(magnetlink) && this.isDirectoryValid(new File(directory));

    }

    private void prepareDownload() {
        MAGNET_LINK = txtMagnetURI.getText();
        TORRENT_FILE = txtTorrentFile.getText();
        DOWNLOAD_DIRECTORY = txtDownloadLocation.getText();
        DIRECTORY_SELECTED = true;

        DOWNLOAD_ALL = chbDownloadAll.isSelected();
        USE_DEFAULT_PORT = chbDefaultPort.isSelected();

        if(!USE_DEFAULT_PORT){
            int port;
            if (NumberUtils.isDigits(txtPort.getText())){
                if (1 <= Integer.parseInt(txtPort.getText()) && (port = Integer.parseInt(txtPort.getText())) < 65535){
                    PORT = port;
                }
            }
        }

        USE_MAGNET_LINK = rdoUseMagnetURI.isSelected();
        USE_TORRENT_FILE = rdoUseTorrentFile.isSelected();

        this.onDirectorySelected();
    }

    enum FileDialogType {
        DIRECTORY,
        TORRENT
    }
    //endregion Pichler part
}