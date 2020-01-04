package client;

import bt.metainfo.TorrentFile;
import bt.torrent.fileselector.SelectionResult;
import bt.torrent.fileselector.TorrentFileSelector;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import streamUI.UI_Controller_singlepart_page;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.String.format;
import static java.lang.String.join;
import static meta.Globals.FORMAT_DOWNLOAD_PART;

public class StreamFileSelector extends TorrentFileSelector {

    private AtomicReference<Thread> currentThread;
    private AtomicBoolean shutdown;

    StreamFileSelector() {
        this.currentThread = new AtomicReference<>(null);
        this.shutdown = new AtomicBoolean(false);
    }

    private static String getPromptMessage(TorrentFile file) {
        return format(FORMAT_DOWNLOAD_PART, join("/", file.getPathElements()));
    }

    private SelectionResult selectSinglePart(TorrentFile file) {
        try {
            this.showSinglePartStage(file);
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }

        boolean selectionResult = new Random().nextBoolean();

        if (selectionResult){
            return SelectionResult.select().build();
        } else {
            return SelectionResult.skip();
        }
    }

    private void showSinglePartStage(TorrentFile file) throws IOException {

        Stage secondStage = new Stage();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/UI_stream_singlepart_page.fxml"));
        Parent root = loader.load();

        root.setStyle("-fx-background-image: url('/images/stream_UI_background.png'); -fx-background-repeat: no-repeat; -fx-background-size: 1215 765");
        UI_Controller_singlepart_page c = loader.getController();

        c.initData(file);

        c.setStage(secondStage);
        Scene s = new Scene(root);
        secondStage.setTitle("stream");
        secondStage.getIcons().add(new Image("/images/streamAppIcon_blue.PNG"));
        secondStage.setScene(s);
        secondStage.setResizable(false);

        c.setParentStage(secondStage);
        secondStage.show();
    }


    @Override
    protected SelectionResult select(TorrentFile file) {
        return this.selectSinglePart(file);

        /*
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

         */
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
}
