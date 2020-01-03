package streamUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class UI_Controller_singlepart_page implements Initializable {
    @FXML
    public GridPane GPAddTorrent;
    @FXML
    private Stage parentStage;
    @FXML
    private Stage stage;
    @FXML
    private TextField txtTorrentPartName;
    @FXML
    private Button btnAccept;
    @FXML
    private Button btnReject;

    @SuppressWarnings("Duplicates")
    @Override
    @FXML
    public void initialize(URL location, ResourceBundle resources) {

    }

    void initData(Customer customer) {
        customerName.setText(customer.getName());
    }

    public void setStage(Stage CurrentStage) {
        this.stage = CurrentStage;
    }

    public void setParentStage(Stage root) {
        this.parentStage = root;
    }

    public void handleOnAddSelectedParts() {
    }

    public void handleOnAccept(ActionEvent actionEvent) {
    }

    public void handleOnReject(ActionEvent actionEvent) {
    }
}