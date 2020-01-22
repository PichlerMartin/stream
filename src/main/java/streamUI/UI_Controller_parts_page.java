package streamUI;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * <p>ui controller page which reflects another fxml page which dislays a list-view of torrent
 * parts from which the user can choose single parts which they want to download.</p>
 *
 * <p>this controller class and the accompanying fxml page are already developed but not in
 * active usage. in order to make it work correctly it would need some reviewing and
 * testing, but until then it stays on hold</p>
 *
 * @see Initializable
 * @author Pichler Martin
 * @since january 2020
 */
@SuppressWarnings("unused")
public class UI_Controller_parts_page implements Initializable {
    @FXML
    private Stage parentStage;
    @FXML
    private Stage stage;
    @FXML
    private ListView<String> livFiles;

    /**
     * <p>this initializer prepares the values and fields of this controller class, but
     * it is not implemented yet.</p>
     * @param location the directory from where the controller is invoked
     * @param resources eventually need resource bundles
     * @author Pichler Martin
     * @since january 2020
     */
    @SuppressWarnings("Duplicates")
    @Override
    @FXML
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * <p>sets a new active stage instead of the current stage</p>
     * @param CurrentStage the current stage
     * @author Pichler Martin
     * @since january 2020
     */
    public void setStage(Stage CurrentStage) {
        this.stage = CurrentStage;
    }

    /**
     * <p>sets a new parent stage to the active stage</p>
     * @param root the current parent stage
     * @author Pichler Martin
     * @since january 2020
     */
    void setParentStage(Stage root) {
        this.parentStage = root;
    }

    /**
     * <p>to implement: this button adds the selected parts of the list view
     * which is displaed on the user interface of this fxml window</p>
     * @author Pichler Martin
     * @since january 2020
     */
    public void handleOnAddSelectedParts() {
    }
}