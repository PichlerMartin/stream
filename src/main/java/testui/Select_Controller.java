package testui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import javax.annotation.Resources;
import java.net.URL;
import java.util.ResourceBundle;

public class Select_Controller implements Initializable {
    @FXML
    private ListView<String> lv_files;
    @FXML
    private Button btn_confirm;

    public void Click_ConfirmFileList(ActionEvent ms){
        Stage stage = (Stage) btn_confirm.getScene().getWindow();

        stage.close();
    }

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     */
    @Override
    @FXML
    public void initialize(URL location, ResourceBundle resources) {

    }
}
