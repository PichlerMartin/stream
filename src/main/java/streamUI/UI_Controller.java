package streamUI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

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
    private VBox VBoxTorrents;

    @FXML
    private TableView TVTorrentsList;

    @FXML
    private GridPane GPAddTorrent;

    @FXML
    private Stage stage;

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
    public void handleOnClickedbtnStorageLocation (ActionEvent event) {

        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Speichern unter");
        dirChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File directory = dirChooser.showDialog(stage);

        txtDownloadLocation.setText(directory.toString());
    }

    private void resetVisibility() {
        VBoxTorrents.setVisible(false);
        GPAddTorrent.setVisible(false);
    }

    public void setStage (Stage CurrentStage) {
        this.stage = CurrentStage;
    }

    public void setParentStage (Stage root) {
        this.parentStage = root;
        resetVisibility();
        VBoxTorrents.setVisible(true);
        TVTorrentsList.setPlaceholder(new Label("No Torrents found!"));
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

}
