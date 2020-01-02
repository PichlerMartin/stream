package streamUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import tasks.UpdateTorrentPartsTask;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class UI_Controller_parts_page implements Initializable {
    @FXML
    private Stage parentStage;
    @FXML
    private Stage stage;
    @FXML
    private ListView<String> livFiles;

    private Map<String, Boolean> Controls = new HashMap<>();

    @SuppressWarnings("Duplicates")
    @Override
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        Controls.put(livFiles.getId(), livFiles.isDisabled());

        ObservableList<String> torrents = FXCollections.observableArrayList();

        torrents

        UpdateTorrentPartsTask torrentUpdateTask = UpdateTorrentPartsTask(friends);

        livFiles.itemsProperty().bind(torrentUpdateTask);

        try {
            torrentUpdateTask.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void setStage(Stage CurrentStage) {
        this.stage = CurrentStage;
    }

    void setParentStage(Stage root) {
        this.parentStage = root;
    }

    public void handleOnAddSelectedParts() {
    }
}