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

public class StreamFileSelector extends TorrentFileSelector implements Initializable {

    private AtomicReference<Thread> currentThread;
    private AtomicBoolean shutdown;

    StreamFileSelector() {
        this.currentThread = new AtomicReference<>(null);
        this.shutdown = new AtomicBoolean(false);
    }

    private static String getPromptMessage(TorrentFile file) {
        return format(FORMAT_DOWNLOAD_PART, join("/", file.getPathElements()));
    }

    /**
     * https://stackoverflow.com/questions/31416784/thread-with-lambda-expression
     * @param file: Torrent file containing information name, etc.
     * @return whether file is selected or nah
     * FIXME:   try to implement display of unique parts on one site fxml window
     */
    @Deprecated
    private SelectionResult selectSinglePart(TorrentFile file) {
        new Thread(() -> {
            try {
                this.showSinglePartStage(file);
            } catch (IOException e) {
                // handle: log or throw in a wrapped RuntimeException
                throw new RuntimeException("IOException caught in lambda", e);
            }
        }).start();

        boolean selectionResult = new Random().nextBoolean();

        if (selectionResult){
            return SelectionResult.select().build();
        } else {
            return SelectionResult.skip();
        }
    }

    private void showSinglePartStage(TorrentFile file) throws IOException {
        /*
         https://stackoverflow.com/questions/17850191/why-am-i-getting-java-lang-illegalstateexception-not-on-fx-application-thread
         Avoid throwing IllegalStateException by running from a non-JavaFX thread.
        */
        AtomicReference<Stage> secondStage = new AtomicReference<>();
        Platform.runLater(
                () -> secondStage.set(new Stage())
        );

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/UI_stream_singlepart_page.fxml"));
        Parent root = loader.load();

        root.setStyle("-fx-background-image: url('/images/stream_UI_background.png'); -fx-background-repeat: no-repeat; -fx-background-size: 1215 765");
        UI_Controller_singlepart_page c = loader.getController();

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


    @Override
    protected SelectionResult select(TorrentFile file) {
        //  Method below is for the invocation of "UI_stream_singlepart_page.fxml"
        //  does not work yet
        //  return this.selectSinglePart(file);

        while (!this.shutdown.get()) {
            System.out.println(getPromptMessage(file));

            String nextCommand = this.readNextCommand(new Scanner(System.in));

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

        throw new IllegalStateException("Shutdown");
    }

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

    private String readNextCommand(Scanner scanner) {
        this.currentThread.set(Thread.currentThread());

        String nextCommand;
        try {
            nextCommand = scanner.nextLine().trim();
        } finally {
            this.currentThread.set(null);
        }

        return nextCommand;
    }

    void shutdown() {
        this.shutdown.set(true);
        Thread currentThread = this.currentThread.get();
        if (currentThread != null) {
            currentThread.interrupt();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
