package streamUI;

import bt.metainfo.TorrentFile;
import io.grpc.netty.shaded.io.netty.util.internal.UnstableApi;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import javax.annotation.Untainted;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * <p>this controller is similar structured like {@link UI_Controller_parts_page} but with the
 * noticeable difference that it only show one torrent part and two buttons. these buttons
 * can be used to choose if the torrent part should be downloaded or not</p>
 *
 * <p>this class is somewhat more advanced than {@link UI_Controller_parts_page} but it
 * could not be invoked correctly during the testing phase. please consider to search the
 * project directory for the usage of this class and test the invocation with a breakpoint</p>
 *
 * @see Initializable
 * @author Pichler Martin
 * @since january 2020
 */
@SuppressWarnings("unused")
@Untainted
@UnstableApi
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


    /**
     * <p>this method initializes the textfield of the fxml window with a preset torrent
     * file by taking the torrent name and cutting out only the name of the torrent</p>
     * @param location the directory from where the controller is invoked
     * @param resources eventually need resource bundles
     * @author Pichler Martin
     * @since january 2020
     */
    @SuppressWarnings("Duplicates")
    @Override
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        //txtTorrentPartName.setText(join("/", file.getPathElements()));
    }

    /**
     * <p>through this method the torrent file of the controller is set before the
     * fxml window is loaded</p>
     * @param file a torrent file
     * @see TorrentFile
     * @see bt.metainfo.Torrent
     */
    public void initData(TorrentFile file) {
    }

    /**
     * <p>sets a new active stage instead of the current stage</p>
     * @param CurrentStage the current stage
     * @author Pichler Martin
     * @since january 2020
     */
    void setStage(Stage CurrentStage) {
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
     * <p>handler for when the selected parts of the torrent should be added
     * to the download queue</p>
     *
     * @see Button
     * @author Pichler Martin
     * @since january 2020
     */
    public void handleOnAddSelectedParts() {
    }

    /**
     * <p>handler for the button, which accepts the displayed torrent part</p>
     *
     * @see Button
     * @author Pichler Martin
     * @since january 2020
     */
    public void handleOnAccept() {
    }

    /**
     * <p>handler for the button, which rejects the displayed torrent part</p>
     *
     * @see Button
     * @author Pichler Martin
     * @since january 2020
     */
    public void handleOnReject() {
    }
}