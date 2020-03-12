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
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import static java.lang.String.format;
import static java.lang.String.join;
import static java.nio.file.Files.exists;
import static support.Globals.*;

/**
 * <p>main controler page, it contains all relevant controls such as all the stages and
 * toggle-groups. for detailed documentation please view the official documentation-book
 * under the section "implementation"</p>
 */
public class UI_Controller_main_page implements Initializable {

    @FXML
    public GridPane GProot;
    @FXML
    public ToggleGroup TGAddTorrent;
    @FXML
    ComboBox<String> cboxSelectLanguage;
    @SuppressWarnings("unused")
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
    public Button btnDefaultDirectory;

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
    private Label lblDefaultDirectory;

    @FXML
    private CheckBox chbDefaultPort;

    @FXML
    private CheckBox chbSeedAfterDownload;

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

    @SuppressWarnings("unused")
    @FXML
    private AnchorPane aPaneAboutStream;

    @FXML
    private Rectangle rectDays;

    @FXML
    private Rectangle rectHours;

    @FXML
    private Rectangle rectMinutes;

    @FXML
    private Rectangle rectSeconds;

    private Locale[] supportedLocales = {
            Locale.GERMAN,
            Locale.ENGLISH,
            Locale.FRENCH,
            new Locale("es")
    };

    private String lightMode = getClass().getResource("/css/lightMode.css").toExternalForm();
    private String darkMode = getClass().getResource("/css/darkMode.css").toExternalForm();

    private boolean timerVar = true;

    /**
     * @deprecated unused method should be removed in the near future
     * @param file a generic torrent file
     *
     * @author PichlerMartin
     */
    @Deprecated
    public void putFileNameAndChoice(TorrentFile file) {
        livFiles.getItems().add(format("%s", join("/", file.getPathElements())));
        livFiles.getSelectionModel().select(format("%s", join("/", file.getPathElements())));
    }

    private Preferences pref = Preferences.userNodeForPackage(getClass());

    /**
     * <p>pre-loading method (initializer), which assigns certain attributes to the custom-port
     * textfield (it only allows 5-digit numbers)</p>
     * @param location location of the resource files
     * @param resources the language packs
     * @author TopeinerMarcel
     */
    @SuppressWarnings("Duplicates")
    @Override
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        int maxLength = 5;

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
    private void initializeTableView (TableView tableView, ResourceBundle labels) {

        Label placeholderLabel = null;

        //checks which tableView is given and sets the matching placeholder
        if (tableView.getId().equals(TVTorrentsList.getId())) {
            placeholderLabel = new Label(labels.getString("TVTorrents_NoContent"));
        } else if (tableView.getId().equals(TVDownloadingTorrentsList.getId())) {
            placeholderLabel = new Label(labels.getString("TVDownloading_NoContent"));
        } else if (tableView.getId().equals(TVUploadingTorrentsList.getId())) {
            placeholderLabel = new Label(labels.getString("TVUploading_NoContent"));
        } else if (tableView.getId().equals(TVFinishedTorrentsList.getId())) {
            placeholderLabel = new Label(labels.getString("TVFinished_NoContent"));
        }

        tableView.setPlaceholder(placeholderLabel);
        if (togDarkMode.isSelected()) {
            //noinspection ConstantConditions
            placeholderLabel.setTextFill(Color.WHITE);
        } else {
            //noinspection ConstantConditions
            placeholderLabel.setTextFill(Color.BLACK);
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
     * writes the decision of the user in the java preferences
     */
    @FXML
    public void handleOnClickedtogDarkMode() {
        Locale currentLocale = Locale.forLanguageTag(pref.get("language", Locale.GERMAN.toString()));
        ResourceBundle labels = ResourceBundle.getBundle("ResourceBundle", currentLocale);

        if (togDarkMode.isSelected()) {
            GProot.getStylesheets().clear();
            GProot.getStylesheets().add(darkMode);
            togDarkMode.setText(labels.getString("togDarkModeOn"));
            pref.put("darkMode", "true");
        } else {
            GProot.getStylesheets().clear();
            GProot.getStylesheets().add(lightMode);
            togDarkMode.setText(labels.getString("togDarkModeOff"));
            pref.put("darkMode", "false");
        }

        initializeTableView(TVTorrentsList, labels);
        initializeTableView(TVDownloadingTorrentsList, labels);
        initializeTableView(TVUploadingTorrentsList, labels);
        initializeTableView(TVFinishedTorrentsList, labels);
    }

    /**
     * function is called if user clicks on menu entry 'About'
     * gives a short overview over the application and describe how a torrent works and how it can be downloaded or seeded
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
    private void initializecboxSelectLanguage() {
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
     * function is called if the user selects a invalid directory or if he closes the dialog
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

        this.resetControls();
    }

    private void resetControls() {
        txtMagnetURI.setText("");
        txtMagnetURI.setDisable(false);
        USE_MAGNET_LINK = true;
        txtTorrentFile.setText("");
        txtTorrentFile.setDisable(true);
        txtDownloadLocation.setText("");
        livFiles.getItems().clear();
        livFiles.setDisable(true);
        btnAddPartsofTorrent.setDisable(true);
        rdoUseMagnetURI.setSelected(true);
        rdoUseTorrentFile.setSelected(false);
        DOWNLOAD_ALL = true;
        SEED_AFTER_DOWNLOAD = false;
        USE_DEFAULT_PORT = true;
        chbDownloadAll.setSelected(true);
        chbSeedAfterDownload.setSelected(false);
        chbDefaultPort.setSelected(true);
        txtPort.setText("");
        txtPort.setDisable(true);
        btnStartDownload.setDisable(true);
        btnSelectTorrentFile.setDisable(true);
    }

    void setStage(Stage CurrentStage) {
        this.stage = CurrentStage;
    }

    /**
     * function sets the parent stage, gets the preferred language and changes the text of all objects depending on the language
     * calls several functions which are initializing objects like checkboxes and Scrollpane
     * this function calls every second a other function, which updates the timer until release
     * @param root: the root stage
     */
    void setParentStage(Stage root) {
        String prefLanguage = pref.get("language", Locale.GERMAN.toString());
        Locale currentLocale = Locale.forLanguageTag(prefLanguage);

        if (pref.get("darkMode", "false").equals("true")) {
            togDarkMode.setSelected(true);
            handleOnClickedtogDarkMode();

        } else {
            togDarkMode.setSelected(false);
            handleOnClickedtogDarkMode();
        }


        this.parentStage = root;
        handleOnClickedbtnTorrents();
        initializecboxSelectLanguage();

        initializeSPane(sPaneAboutStream);
        root.setOnShown(e ->
                sPaneAboutStream.lookup(".scroll-pane").lookup(".viewport").setStyle("-fx-background-color:  #transparent; -fx-blend-mode: src-over"));

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

            if ((Integer.parseInt(days) <= 0) && (Integer.parseInt(hours) <= 0) && (Integer.parseInt(minutes) <= 0) && (Integer.parseInt(seconds) <= 0)){
                lblDaysUntilRelease.setText("00");
                lblHoursUntilRelease.setText("00");
                lblMinutesUntilRelease.setText("00");
                lblSecondsUntilRelease.setText("00");
            } else if (days.equals("00") && Integer.parseInt(hours) <= 24) {
                if (timerVar) {
                    rectDays.setStyle("-fx-fill: #e84855");
                    rectHours.setStyle("-fx-fill: #e84855");
                    rectMinutes.setStyle("-fx-fill: #e84855");
                    rectSeconds.setStyle("-fx-fill: #e84855");
                    lblDaysUntilRelease.setStyle(lblDaysUntilRelease.getStyle() + "-fx-font-size: 75px;");
                    lblHoursUntilRelease.setStyle(lblHoursUntilRelease.getStyle() + "-fx-font-size: 75px;");
                    lblMinutesUntilRelease.setStyle(lblMinutesUntilRelease.getStyle() + "-fx-font-size: 75px;");
                    lblSecondsUntilRelease.setStyle(lblSecondsUntilRelease.getStyle() + "-fx-font-size: 75px;");
                    timerVar = false;
                } else {
                    rectDays.setStyle("-fx-fill: #1b3957");
                    rectHours.setStyle("-fx-fill: #1b3957");
                    rectMinutes.setStyle("-fx-fill: #1b3957");
                    rectSeconds.setStyle("-fx-fill: #1b3957");
                    lblDaysUntilRelease.setStyle(lblDaysUntilRelease.getStyle() + "-fx-font-size: 72px;");
                    lblHoursUntilRelease.setStyle(lblHoursUntilRelease.getStyle() + "-fx-font-size: 72px;");
                    lblMinutesUntilRelease.setStyle(lblMinutesUntilRelease.getStyle() + "-fx-font-size: 72px;");
                    lblSecondsUntilRelease.setStyle(lblSecondsUntilRelease.getStyle() + "-fx-font-size: 72px;");
                    timerVar = true;
                }
            }
        }));
        timerUntilRelease.setCycleCount(Timeline.INDEFINITE);
        timerUntilRelease.play();
    }

    /**
     * function, which initializes the given ScrollPane
     * @param sPane: pane, which is initialized
     */
    private void initializeSPane(ScrollPane sPane) {
        sPane.setPrefSize(650, 380);
        AnchorPane.setTopAnchor(sPane, 0.);
        AnchorPane.setRightAnchor(sPane, 0.);
        AnchorPane.setBottomAnchor(sPane, 0.);
        AnchorPane.setLeftAnchor(sPane, 0.);
    }

    /**
     * This method is called by menu buttons, which have been entered
     * It changes the style and the colour of the text in the caller button
     * @param event: is used to retrieve the source of the event
     */
    @FXML
    public void handleOnMenuEntryEntered(MouseEvent event) {
        Button btnEnteredBtn = (Button) event.getSource();
        if (togDarkMode.isSelected()) {
            btnEnteredBtn.setStyle(btnEnteredBtn.getStyle() + "; -fx-underline: true; -fx-text-fill:  #606060");
        } else {
            btnEnteredBtn.setStyle(btnEnteredBtn.getStyle() + "; -fx-underline: true; -fx-text-fill:  #1b3957");
        }

    }

    /**
     * This method changes the style and colour of the caller button back to the original state
     * If the mouse leaves one of the menu buttons, this method is called
     * @param event: is used to retrieve the source of the event
     */
    @FXML
    public void handleOnMenuEntryLeft(MouseEvent event) {
        Button btnLeftbtn = (Button) event.getSource();
        if (togDarkMode.isSelected()) {
            btnLeftbtn.setStyle(btnLeftbtn.getStyle() + "; -fx-underline: false; -fx-text-fill: white");
        } else {
            btnLeftbtn.setStyle(btnLeftbtn.getStyle() + "; -fx-underline: false; -fx-text-fill: white");
        }
    }

    /**
     * This method is called by select file buttons, which have been entered
     * It changes the style and the colour of the text in the caller button
     * @param event: is used to retrieve the source of the event
     */
    @FXML
    public void handleOnSelectFileEntered (MouseEvent event) {
        Button btnEnteredBtn = (Button)event.getSource();
        if (togDarkMode.isSelected()) {
            btnEnteredBtn.setStyle(btnEnteredBtn.getStyle() + "; -fx-background-color:  #606060; -fx-text-fill: white");
        } else {
            btnEnteredBtn.setStyle(btnEnteredBtn.getStyle() + "; -fx-background-color:  #1b3957; -fx-text-fill: white");
        }

    }

    /**
     * This method changes the style and colour of the caller button back to the original state
     * If the mouse leaves one of the select file buttons, this method is called
     * @param event: is used to retrieve the source of the event
     */
    @FXML
    public void handleOnSelectFileLeft (MouseEvent event) {
        Button btnLeftbtn = (Button)event.getSource();
        if (togDarkMode.isSelected()) {
            btnLeftbtn.setStyle(btnLeftbtn.getStyle() + "; -fx-background-color: transparent; -fx-text-fill: white");
        } else {
            btnLeftbtn.setStyle(btnLeftbtn.getStyle() + "; -fx-background-color: transparent; -fx-text-fill: black");
        }
    }


    /**
     * changes the language of all objects to the given language using ResourceBundles
     * @param loc: the given ResourceBundle, which contains the text for the depending object
     */
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

    /**
     * function is called, if user clicks on the button 'Select default directory' below the menu entry 'Settings'
     * opens a file dialog, checks if the selected path is valid and saves this path in the preferences
     * if the selected path is invalid, it calls the function showWarning() and shows a warning
     */
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

    /**
     * <p>this method serves as a validator, it shows a warning window pop-up if it detects a fault</p>
     * @author PichlerMartin
     */
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

    /**
     * <p>does similar things as {@link UI_Controller_main_page##handleOnAddSelectedParts()}</p>
     * @author PichlerMartin
     */
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

    /**
     * <p>does similar things as {@link UI_Controller_main_page##handleOnAddSelectedParts()}</p>
     * @author PichlerMartin
     */
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

    /**
     * <p>does similar things as {@link UI_Controller_main_page##handleOnAddSelectedParts()}</p>
     * @author PichlerMartin
     */
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

    /**
     * <p>does similar things as {@link UI_Controller_main_page##handleOnAddSelectedParts()}</p>
     * @author PichlerMartin
     */
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

    /**
     * <p>FXML method which is called when everythings prepared to start a download, hence
     * its only call comes from {@link UI_Controller_main_page#prepareDownload()}</p>
     */
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

    /**
     * <p>download-handler which invokes the initial torrent-client inside a new thread. this
     * happens to keep the ui useable even when in the background the torrent-download is being
     * invoked and handled. it gives all preset global values over to the main method of the
     * torrent-client in form of a string array and leaves it over to the client to decode the
     * string.</p>
     *
     * <p>below there is a commented-out part which is marked with a to-do, meaning there could
     * be another implementation which handles the display of the single torrent parts inside
     * another ui window, but this part will be implemented in a future update</p>
     *
     * @since 18.01.2020
     * @author PichlerMartin
     */
    @FXML
    private void handleOnStartDownload() {
        new Thread(() -> StreamClient.main(new String[]{MAGNET_LINK,
                DOWNLOAD_DIRECTORY,
                TORRENT_FILE,
                DOWNLOAD_ALL.toString(),
                USE_DEFAULT_PORT.toString(),
                PORT.toString(),
                USE_MAGNET_LINK.toString(),
                USE_TORRENT_FILE.toString(),
                SEED_AFTER_DOWNLOAD.toString()})).start();

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

    /**
     * <p>due to unforeseeable challenges this part of the program where a new java fx window is being
     * called is marked as deprecated. its initial purpose was to call a fx window where the single
     * torrent parts should be displayed</p>
     *
     * @see UI_Controller_main_page#handleOnStartDownload()
     * @see UI_Controller_parts_page
     * @deprecated this method is not exactly deprecated but rather froze in development
     * @throws IOException for errors when reading the fxml file
     */
    @Deprecated
    public void showTorrentPartsStage() throws IOException {

        Stage secondStage = new Stage();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/UI_stream_parts_page.fxml"));
        Parent root = loader.load();

        root.setStyle("-fx-background-image: url('/images/stream_LightModeUI_background.png'); -fx-background-repeat: no-repeat; -fx-background-size: 1215 765");
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
     * example method provided by github user atomashpolskiy, creator of bt-library,
     * used to demonstrate the basic capabilities of the java torrent implementation
     *
     * @see "https://github.com/atomashpolskiy/bittorrent"
     * @deprecated it is outversioned by {@link UI_Controller_main_page#handleOnStartDownload()}
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

    /**
     * <p>this method is called when the radio button which stands beside the magnet-uri/link label in
     * the ui is clicked. it tells the program to derive the torrent-information from the provided magnet
     * uri/link</p>
     *
     * @author PichlerMartin
     */
    @FXML
    public void handleOnUseMagnetURI() {

        txtTorrentFile.setDisable(true);
        btnSelectTorrentFile.setDisable(true);
        USE_MAGNET_LINK = true;

        USE_TORRENT_FILE = false;
        txtMagnetURI.setDisable(false);

        //  if all perquisites are met, enable download button
        if (checkIfDownloadCanBeInitialized()) prepareDownload();
    }

    /**
     * <p>same as {@link UI_Controller_main_page#handleOnUseMagnetURI()}</p>
     */
    @FXML
    public void handleOnUseTorrentFile() {

        txtMagnetURI.setDisable(true);
        USE_TORRENT_FILE = true;

        USE_MAGNET_LINK = true;
        btnSelectTorrentFile.setDisable(false);
        txtTorrentFile.setDisable(false);

        if (checkIfDownloadCanBeInitialized()) prepareDownload();
    }

    /**
     * <p>opens a new file dialog. there are two types, one in which a directory (download-directory) can be selected,
     * and another whereas a torrent file must be chosen.</p>
     * @param header text displayed as header, depending on requested file-type
     * @param type either DIRECTORY or TORRENT
     * @return chosen file/directory or NULL (will result in error window)
     * @author PichlerMartin
     */
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

    /**
     * <p>similar to {@link UI_Controller_main_page#rdoUseMagnetURI}, but here it enables or disables
     * the text box for the custom port (for incoming connections)</p>
     */
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

    private boolean isDirectoryValid(File directory) {
        return directory != null && !directory.getPath().equals("") && directory.isDirectory() && exists(directory.toPath()) && exists(directory.toPath());
    }

    private boolean isTorrentFileValid(File torrentfile) {
        return torrentfile != null && torrentfile.exists() && (FilenameUtils.getExtension(torrentfile.getPath()).equals("torrent"));
    }

    private boolean isMagnetLinkValid(String magnetlink) {
        return magnetlink.contains("magnet:?xt=urn:btih:");
    }

    /**
     * <p>this method checks whether the input text-fields met the requirements to start a download. it
     * calls the class-private method to check if e.g. the download directory is valid (does it exist, ...)</p>
     *
     * @author PichlerMartin
     * @return boolean value whether the magnet link/uri is valid
     */
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

    /**
     * <p>this is the last method which is called befor a torrent-client is initialised, it assigns the
     * global fields containing the information about optional parameters such as whether a download should
     * continue to its seeding stage after it is finished.</p>
     *
     * <p>this method is called after every insertion of data into the ui</p>
     *
     * @author PichlerMartin
     */
    private void prepareDownload() {
        MAGNET_LINK = txtMagnetURI.getText();
        TORRENT_FILE = txtTorrentFile.getText();
        DOWNLOAD_DIRECTORY = txtDownloadLocation.getText();
        DIRECTORY_SELECTED = true;

        DOWNLOAD_ALL = chbDownloadAll.isSelected();
        USE_DEFAULT_PORT = chbDefaultPort.isSelected();
        SEED_AFTER_DOWNLOAD = chbSeedAfterDownload.isSelected();

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

    /**
     * <p>simple enum, for usage see {@link UI_Controller_main_page#openFileDialog(String, FileDialogType)}</p>
     */
    enum FileDialogType {
        DIRECTORY,
        TORRENT
    }
    //endregion Pichler part
}