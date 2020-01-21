package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * <p>controller for {@link sample.Main}. please see the javadoc of this Main class. this controller
 * serves as a simple controller for the elements of this class. it simply serves as a template for a
 * basic java fx application, in order to help the developers get a quick start into coding.</p>
 *
 * @see sample.Main#main(String[])
 * @author PichlerMartin
 * @since summer 2019
 */
public class Controller {

    @FXML
    private Label lbl1;

    @FXML
    private Stage parentStage;

    @FXML
    private TextField txt1;

    @FXML
    private Button btn1;

    /**
     * <p>handler for button btn1. sets the text from lbl1 to the text which was set in textfield
     * txt1.</p>
     */
    @FXML
    private void handleBT1action() {
        lbl1.setText(txt1.getText());
    }

    /**
     * <p>handler for button btn2. this button sets expands the window in width and height by 10
     * pixels when pressed</p>
     */
    @FXML
    private void handleBT2action() {
        parentStage.setHeight(parentStage.getHeight() + 10);
        parentStage.setWidth(parentStage.getWidth() + 10);
    }

    /**
     * <p>this is a handler for the enter-action of textfield txt1. it sets the text of btn1 to
     * the input text when pressed enter in txt1.</p>
     */
    @FXML
    private void handleTXT1action() {
        btn1.setText(txt1.getText());
    }

    @FXML
    void setParentStage(Stage root) {
        this.parentStage = root;
    }
}
