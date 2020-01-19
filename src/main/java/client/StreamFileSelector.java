package client;

import bt.metainfo.TorrentFile;
import bt.torrent.fileselector.SelectionResult;
import bt.torrent.fileselector.TorrentFileSelector;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import streamUI.UI_Controller_singlepart_page;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.String.format;
import static java.lang.String.join;
import static support.Globals.FORMAT_DOWNLOAD_PART;
import static support.Globals.FORMAT_ILLEGAL_KEYPRESS;

/**
 * <p>this class serves the purpose of handling the selection of single torrent parts which make up
 * the total torrent-package. it is used when the use selects to download not all files contained in
 * the torrent meta-info.</p>
 *
 * <p>it contains two private field, atomic references, which can not be parallel accessed. first being called currentThread
 * representing the current thread. thw second is called shutdown which is a boolean which indicates whether the download-process
 * is already shut down.</p>
 *
 * @see TorrentFileSelector
 * @see Initializable
 *
 * @author Pichler Martin
 * @since december 2019
 */
public class StreamFileSelector extends TorrentFileSelector implements Initializable {

    private AtomicReference<Thread> currentThread;
    private AtomicBoolean shutdown;

    /**
     * <p>initialisation of the private atomic reference fields</p>
     *
     * @see AtomicReference
     *
     * @author Pichler Martin
     * @since december 2019
     */
    StreamFileSelector() {
        this.currentThread = new AtomicReference<>(null);
        this.shutdown = new AtomicBoolean(false);
    }

    /**
     * <p>formats the correct output for the command line display of the torrent file selection. it shows a single
     * line in which the user is asked  whether they want to download a specific file. this can be acquitted by either
     * "y" for yes or "n" for no.</p>
     *
     * @see TorrentFile
     * @param file a torrent file object
     * @return the formatted string
     */
    private static String getPromptMessage(TorrentFile file) {
        return format(FORMAT_DOWNLOAD_PART, join("/", file.getPathElements()));
    }

    /**
     * https://stackoverflow.com/questions/31416784/thread-with-lambda-expression
     * @param file: Torrent file containing information name, etc.
     * @return whether file is selected or nah
     *
     * @deprecated makes a call to the private method {@link StreamFileSelector#showSinglePartStage(TorrentFile)} but
     * this feature does not work properly yet. its purpose is to be called uniquely for every single part
     * of the torrent-package, this function should be implemented in this method, until then it will stay deprecated
     */
    @Deprecated
    public SelectionResult selectSinglePart(TorrentFile file) {
        new Thread(() -> {
            try {
                //  call to class private method which shows a new java fxml window for single part selection
                this.showSinglePartStage(file);
            } catch (IOException e) {
                // handle: log or throw in a wrapped RuntimeException
                throw new RuntimeException("IOException caught in lambda", e);
            }
        }).start();

        //  placeholder, returns a random selection for torrent part, needs to be removed URGENTLY when single part selection is implemented
        boolean selectionResult = new Random().nextBoolean();

        if (selectionResult){
            return SelectionResult.select().build();
        } else {
            return SelectionResult.skip();
        }
    }

    /**
     * <p>this java fxml windows display a single part of a torrent file in a text-box with two buttons, "yes" and "no"
     * which allow the user to select whether to download it or not.</p>
     *
     * <p>due to unknown reasons the fxml file is not correctly displayed after .show() is called, this error should be
     * looked after if the ui selection of the torrent file should be realised, but until then this fxml window will not be
     * used.</p>
     * <hr><blockquote><pre>
     *     secondStage.get().show();
     * </pre></blockquote></hr>
     *
     * @param file a torrent file object
     * @throws IOException exceptions is thrown if fxml file could not be found
     * @see TorrentFile
     * @see FXMLLoader
     * @see UI_Controller_singlepart_page
     *
     * @author Pichler Martin
     * @since january 2020
     */
    private void showSinglePartStage(TorrentFile file) throws IOException {
        /*
         https://stackoverflow.com/questions/17850191/why-am-i-getting-java-lang-illegalstateexception-not-on-fx-application-thread
         Avoid throwing IllegalStateException by running from a non-JavaFX thread.
        */
        AtomicReference<Stage> secondStage = new AtomicReference<>();
        Platform.runLater(
                () -> secondStage.set(new Stage())
        );

        //  loads the fxml file for this window
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/UI_stream_singlepart_page.fxml"));
        Parent root = loader.load();

        //  sets the default stream background
        root.setStyle("-fx-background-image: url('/images/stream_LightModeUI_background.png'); -fx-background-repeat: no-repeat; -fx-background-size: 1215 765");
        UI_Controller_singlepart_page c = loader.getController();

        //  initialises fxml controller with Torrent-file
        c.initData(file);

        c.setStage(secondStage.get());
        secondStage.get().setTitle("stream");
        secondStage.get().getIcons().add(new Image("/images/streamAppIcon_blue.PNG"));
        secondStage.get().setScene(new Scene(root, 800, 300));
        secondStage.get().setResizable(false);

        c.setParentStage(secondStage.get());

        /*
            https://stackoverflow.com/questions/43647368/how-can-i-get-make-my-program-wait-until-javafx-window-has-been-closed-before-co
        */
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        alert.setContentText("Would you like to enter another number?");
        alert.setOnHidden(e -> {
            if (alert.getResult() == ButtonType.YES) {
                secondStage.get().show();
            }
        });
        secondStage.get().setOnHidden(e -> alert.show());

        secondStage.get().show();
    }

    /**
     * <p>displays a query to ask the user if they want to download a specific file and accepts a single char as an answer.
     * if they choose "y" it will be added in the switch-tree, if they answer "n" it will not be included in the build. every
     * other input will trigger a error message in the command prompt and will result in an repetition of the query.</p>
     * {@inheritDoc}
     * @param file
     * @return
     * @see TorrentFileSelector#select(TorrentFile)
     *
     * @author Pichler Martin
     * @since january 2020
     */
    @Override
    protected SelectionResult select(TorrentFile file) {
        //  Method below is for the invocation of "UI_stream_singlepart_page.fxml"
        //  does not work yet
        //  return this.selectSinglePart(file);

        while (!this.shutdown.get()) {
            //  ask user whether to download a file
            System.out.println(getPromptMessage(file));

            //  read input from user
            String nextCommand = this.readNextCommand(new Scanner(System.in));

            //  check whether the users input was valid
            switch (this.readKey(nextCommand)) {
                case 0:
                case 1:
                case 2:
                    return SelectionResult.select().build();
                case 3:
                case 4:
                    return SelectionResult.skip();
                default:
                    System.out.println(FORMAT_ILLEGAL_KEYPRESS);
            }
        }

        //  program should not reach this point, otherwise throw a exception
        throw new IllegalStateException("Shutdown");
    }

    /**
     * <p>this class private method evaluates the users input and check whether it is one of the allowed
     * ones ("n", "N", "y", "Y") or if the input is invalid the calling method {@link StreamFileSelector#select(TorrentFile)}
     * decides what to do depending on the input</p>
     * @param nextCommand a string containing the input
     * @return a byte (single digit number) which indicates the input result
     *
     * @see case
     * @author Pichler Martin
     * @since january 2020
     */
    private byte readKey(String nextCommand) {

        byte result = -1;
        switch (nextCommand.hashCode()) {
            case 0:
                if (nextCommand.equals("")) {
                    result = 0;
                }
                break;
            case 78:
                if (nextCommand.equals("N")) {
                    result = 4;
                }
                break;
            case 89:
                if (nextCommand.equals("Y")) {
                    result = 2;
                }
                break;
            case 110:
                if (nextCommand.equals("n")) {
                    result = 3;
                }
                break;
            case 121:
                if (nextCommand.equals("y")) {
                    result = 1;
                }
        }

        return result;
    }

    /**
     * <p>this class private method is used to read input from the command line</p>
     * @param scanner a scanner object for reading the input from the command line
     * @return a string containing the next command
     * @see Scanner
     *
     * @author Pichler Martin
     * @since january 2020
     */
    private String readNextCommand(Scanner scanner) {
        //  set the current thread
        this.currentThread.set(Thread.currentThread());

        String nextCommand;
        try {
            //  read next line
            nextCommand = scanner.nextLine().trim();
        } finally {
            //  terminate this thread
            this.currentThread.set(null);
        }

        return nextCommand;
    }

    /**
     * <p>shut down the torrent file selection, in case an invalid operation has been performed</p>
     *
     * @author Pichler Martin
     * @since january 2020
     */
    void shutdown() {
        this.shutdown.set(true);
        Thread currentThread = this.currentThread.get();
        if (currentThread != null) {
            currentThread.interrupt();
        }

    }

    /**
     * {@inheritDoc}
     * @param location
     * @param resources
     *
     * @author Pichler Martin
     * @since january 2020
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
