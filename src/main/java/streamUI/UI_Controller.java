package streamUI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
    private VBox VBoxDownloading;

    @FXML
    private TableView TVDownloading;

    @FXML
    private GridPane GPAddTorrent;

    @FXML
    public void handleOnClickedbtnTorrents (ActionEvent event) {

    }

    @FXML
    public void handleOnClickedbtnDownloading (ActionEvent event) {
        VBoxDownloading.setVisible(true);
    }

    @FXML
    public void handleOnClickedbtnAddTorrents (ActionEvent event) {

        GPAddTorrent.setVisible(true);
    }

    public void setParentStage (Stage root) {
        this.parentStage = root;
        VBoxDownloading.setVisible(false);
        GPAddTorrent.setVisible(false);
        TVDownloading.setPlaceholder(new Label("No downloaded Torrents found!"));
    }
}
