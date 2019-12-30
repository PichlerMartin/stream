package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Controller {

    @FXML
    private Label lbl1;

    @FXML
    private Stage parentStage;

    @FXML
    private TextField txt1;

    @FXML
    private Button btn1;

    @FXML
    private Button btn2;

    @FXML
    private void handleBT1action(ActionEvent e) {
        lbl1.setText(txt1.getText());
    }

    @FXML
    private void handleBT2action(ActionEvent e) {
        parentStage.setHeight(parentStage.getHeight() + 10);
        parentStage.setWidth(parentStage.getWidth() + 10);
    }

    @FXML
    private void handleTXT1action(ActionEvent e) {
        btn1.setText(txt1.getText());
    }

    public void setParentStage (Stage root) {
        this.parentStage = root;
    }
}
