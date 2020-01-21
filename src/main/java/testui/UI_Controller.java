package testui;

import filelibrary.Library;
import filelibrary.PublicLibrary;
import filelibrary.TorrentInFileSystem;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import support.StreamContext;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * <p>this is the main controller of the fxml window from the testui package. as every other class
 * in this package this class solely served the purpose of testing the capabilities of the bittorrent
 * protocol and its frameworks.</p>
 * Controller for testing class UI_Main
 * @author PichlerMartin
 * @since august 2019
 */
@SuppressWarnings("unused")
public class UI_Controller implements Controller {

    @FXML
    private Stage parentStage;
    @FXML
    private ProgressBar prog_m;
    @FXML
    private Label lbl_status;

    private Library torrents = new PublicLibrary();

    /**
     * <p>helper method, which assists the ui in loading its parent stage</p>
     * <p>Hilfsmethode, welche dazu dient, dass das UI richtig lädt</p>
     *
     * @param root: the root stage of the ui
     * @author PichlerMartin
     * @since august 2019
     */
    @SuppressWarnings("WeakerAccess")
    public void setParentStage(Stage root) {
        this.parentStage = root;
    }

    /**
     * <p>laods the window, which is used to select single parts of a torrent file</p>
     * <p>Lädt das Fenster, welches dazu verwendet wird die einzelnen Dateien des Torrents
     * zu wählen</p>
     * @see Select_Controller
     * @author PichlerMartin
     * @since august 2019
     */
    private void loadChooseFileWindow() {
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/choosen_files.fxml"));
            root = loader.load();

            Stage stage = new Stage();

            stage.setTitle("Choose Files");
            stage.setScene(new Scene(root, 450, 450));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>Loads all files from the resources/torrents directory at startup, for testing purposes</p>
     *
     * @throws IOException: In case the files won't load properly
     * @author PichlerMartin
     * @since august 2019
     */
    public void LoadStartConfiguration() throws IOException {
        //  try to retrieve torrents from the resources directory
        try (Stream<Path> paths = Files.walk(Paths.get("target\\classes\\torrents"))) {


            Object[] directoryContent = paths.toArray();

            for (Object s : directoryContent
            ) {
                //  get names from torrent paths
                String content = s.toString();
                String contentname = content.substring(content.lastIndexOf('\\') + 1);

                if (!contentname.equals("torrents")) {
                    //  add torrent to library object
                    torrents.addTorrent(new TorrentInFileSystem(content, contentname.substring(0, contentname.length() - 8)));
                }
            }

            StreamContext.getInstance().setController(this);
            loadChooseFileWindow();
        }
    }

    /**
     * <p>this method converts a torrent file into a magnet link. this method is used for specific torrent
     * downloading functions which do not support .torrent-files. after the new {@link streamUI.UI_Controller_main_page}
     * was implmented this method is no longer in use, because both magnet links\uris and .torrent-files are now
     * supported.</p>
     * @see "https://stackoverflow.com/questions/3436823/how-to-calculate-the-hash-value-of-a-torrent-using-java"
     * @param torrentPath: path to torrent file
     * @return returns hash value
     * @author PichlerMartin
     * @since august 2019
     * @deprecated .torrent-files are now supported
     */
    @Deprecated
    private String convertTorrentToMagnet(File torrentPath) {
        String text = "";

        try {
            BufferedReader br = new BufferedReader(new FileReader(torrentPath));
            text = br.readLine();

            text = text.substring(0, text.lastIndexOf(':') + 1);

            //  creates a substring with the relevant information for creation of a magnet link
            String s1 = text.substring(0, 2);
            String s2 = text.substring(text.indexOf("pieces") - 1, text.lastIndexOf(':') - 1);
            String s3 = text.substring(text.indexOf(":name") - 1, text.lastIndexOf(':'));
            //s3 = s3.substring(0, s3.lastIndexOf(':'));
            text = s1 + s2 + ":...................." + s3.substring(0, s3.lastIndexOf(':'));

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return text;
    }

    /**
     * <p>testing method for the progress bar which should display the torrent download progress in the future</p>
     * @param mouseEvent: mouse event get thrown when clicked
     * @see "https://stackoverflow.com/questions/32757069/updating-progressbar-value-within-a-for-loop"
     * @author PichlerMartin
     * @since august 2019
     */
    public void click_makeProgress(MouseEvent mouseEvent) {
        new Thread(() -> {
            for (int i = 0; i <= 100; i++) {

                //  assign the initial position
                final int position = i;
                Platform.runLater(() -> {
                    //  set the new progress
                    prog_m.setProgress(position / 100.0);
                });
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    //noinspection ThrowablePrintedToSystemOut
                    System.err.println(e);
                }
            }
        }).start();
    }

    /**
     * {@inheritDoc}
     * @author PichlerMartin
     * @since august 2019
     */
    @Override
    public Label getLabel() {
        return this.lbl_status;
    }

    /**
     * {@inheritDoc}
     * @param status a label field for the status of the controller, or the displayed content
     * @author PichlerMartin
     * @since august 2019
     */
    @Override
    public void setLabel(Label status) {
        this.lbl_status = status;
    }
}
