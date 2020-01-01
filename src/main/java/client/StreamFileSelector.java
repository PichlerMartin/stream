package client;

import bt.metainfo.TorrentFile;
import bt.torrent.fileselector.SelectionResult;
import bt.torrent.fileselector.TorrentFileSelector;
import javafx.scene.control.ListView;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.String.format;
import static java.lang.String.join;
import static meta.Globals.FORMAT_DOWNLOAD_PART;
import static meta.Globals.FORMAT_ILLEGAL_KEYPRESS;

public class StreamFileSelector extends TorrentFileSelector {

    private AtomicReference<Thread> currentThread;
    private AtomicBoolean shutdown;
    private ListView<String> listView = null;

    StreamFileSelector() {
        this.currentThread = new AtomicReference<>(null);
        this.shutdown = new AtomicBoolean(false);
    }

    StreamFileSelector(ListView<String> listView) {
        this.currentThread = new AtomicReference<>(null);
        this.shutdown = new AtomicBoolean(false);
        this.listView = listView;
    }

    private static String getPromptMessage(TorrentFile file) {
        return format(FORMAT_DOWNLOAD_PART, join("/", file.getPathElements()));
    }

    protected SelectionResult selectToListView(TorrentFile file) {
        while (!this.shutdown.get()) {
            System.out.println(getPromptMessage(file));

            String nextCommand = this.readNextCommand(new Scanner(System.in));
            byte result = -1;
            StreamClient.putFileNameAndChoice(file);
            this.listView.getItems().add(format("%s", join("/", file.getPathElements())));
        }

        /*
          ToDo:   This method is called first to load all torrent-parts into the list view
          ToDo:   but origin of select needs to be identified, continue later
        */

        return SelectionResult.select().build();
    }

    @Override
    protected SelectionResult select(TorrentFile file) {

        while (!this.shutdown.get()) {
            System.out.println(getPromptMessage(file));

            String nextCommand = this.readNextCommand(new Scanner(System.in));
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

            switch (result) {
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
