package client;

import bt.metainfo.Torrent;
import bt.torrent.TorrentSessionState;
import download.DownloadRate;
import download.DownloadStats;
import javafx.scene.control.Label;
import support.StreamContext;
import testui.Controller;
import testui.UI_Controller;

import javax.naming.Context;
import javax.naming.ldap.Control;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class StreamLogPrinter {
    public AtomicReference<Torrent> getTorrent() {
        return torrent;
    }

    public void setTorrent(AtomicReference<Torrent> torrent) {
        this.torrent = torrent;
    }

    private AtomicReference<Torrent> torrent;
    private Controller controller;
    private AtomicReference<TorrentSessionState> sessionState;
    private AtomicReference<ProcessingStage> processingStage;
    private AtomicBoolean shutdown;

    private long started;
    private long downloaded;
    private long uploaded;

    public StreamLogPrinter(Controller controller){
        this.torrent = new AtomicReference<>(null);
        this.sessionState = new AtomicReference<>(null);
        this.processingStage = new AtomicReference<>(ProcessingStage.FETCHING_METADATA);
        this.shutdown = new AtomicBoolean(false);
        this.controller = controller;
    }

    public void updateTorrentStage(TorrentSessionState sessionState){this.sessionState.set(sessionState);}

    public void whenTorrentFetched(Torrent torrent){
        this.controller.setLabel(new Label("Torrent fetched"));
        StreamContext.getInstance().currentController().setLabel(new Label("Torrent fetched"));
        this.torrent.set(torrent);
        this.processingStage.set(ProcessingStage.CHOOSING_FILES);
    }



    public void onFilesChosen() {
        this.processingStage.set(ProcessingStage.DOWNLOADING);
    }

    public void onDownloadComplete() {

        this.processingStage.set(StreamLogPrinter.ProcessingStage.SEEDING);
    }

    public void startLogPrinter(){
        this.started = System.currentTimeMillis();
        System.out.println("Metadata is being fetched... Please be pacient...");

        new Thread(() ->{
            do{
                TorrentSessionState sessionState = this.sessionState.get();
                if(sessionState != null){
                    Duration elapsedtime = this.returnTimeElapsed();
                    DownloadRate downloadRate = new DownloadRate(sessionState.getDownloaded() - this.downloaded);
                    DownloadRate uploadRate = new DownloadRate(sessionState.getDownloaded() - this.uploaded);

                    printLog(torrent.get(), sessionState, processingStage.get(), new DownloadStats(elapsedtime, downloadRate, uploadRate));

                    this.downloaded = sessionState.getDownloaded();
                    this.uploaded = sessionState.getUploaded();
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex){
                    return;
                }
            } while (!this.shutdown.get());
        }).start();
    }

    private void printLog(Torrent torrent, TorrentSessionState sessionState, ProcessingStage downloadingStage, DownloadStats downloadStats) {
        
    }

    private Duration returnTimeElapsed() {
        return Duration.ofMillis(System.currentTimeMillis() - this.started);
    }

    public void stop() {
        this.shutdown.set(true);
    }

    public static enum ProcessingStage {
        FETCHING_METADATA,
        CHOOSING_FILES,
        DOWNLOADING,
        SEEDING;

        private ProcessingStage() {
        }
    }
}
