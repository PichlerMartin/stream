package streamUI;

import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.awt.*;

public class UI_Controller {

    @FXML
    private Stage parentStage;

    @FXML
    private Label lblTorrents;

    @FXML
    private Label lblDownloading;

    @FXML
    private Label lblUploading;

    @FXML
    private Label lblFinished;

    @FXML
    private Label lblSettings;

    @FXML
    private Label lblHelp;

    @FXML
    public void handleOnClickedlblTorrents (ActiveEvent event) {

    }

    public void setParentStage (Stage root) {
        this.parentStage = root;
    }
}
