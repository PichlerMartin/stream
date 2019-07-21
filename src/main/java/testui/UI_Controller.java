package testui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.EventListener;

/**
 * Controller for testing class UI_Main
 */
public class UI_Controller {

    @FXML
    private Stage parentStage;

    @FXML
    private ListView lbl_filelib;
    @FXML
    private Button cmd_enter;
    @FXML
    private ProgressBar prog_m;

    /**
     * Progressbar-Test to overlook the functionality and ability of the use-case
     */
    @FXML
    private ProgressBar prog_n;

    public void setParentStage (Stage root) {
        this.parentStage = root;
    }

    public void Enter_Action(ActionEvent actionEvent) {
    }

    /**
     * Loads all files from the resources/torrents directory at startup, for testing purposes
     * @param mouseEvent
     * @throws FileNotFoundException
     */
    public void LoadStartConfiguration(MouseEvent mouseEvent) throws FileNotFoundException {
        FileReader fr = new FileReader("../torrents/RedStarOS.torrent");

        for (File f:
             ) {

        }
    }
}
