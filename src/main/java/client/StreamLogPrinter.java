package client;

import bt.metainfo.Torrent;
import bt.torrent.TorrentSessionState;
import download.DownloadRate;
import download.DownloadStats;
import javafx.scene.control.Label;
import support.StreamContext;
import testui.Controller;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static client.StreamLogPrinter.ProcessingStage.DOWNLOADING;

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
        this.processingStage.set(DOWNLOADING);
    }

    public void onDownloadComplete() {

        this.processingStage.set(StreamLogPrinter.ProcessingStage.SEEDING);
    }

    public void startLogPrinter(){
        this.started = System.currentTimeMillis();
        System.out.println("Metadata is being fetched... Please be pacient...");

        Thread logPrintThread = new Thread(() ->{
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
        });

        logPrintThread.setDaemon(true);
        logPrintThread.start();
    }

    private void printLog(Torrent torrent, TorrentSessionState sessionState, ProcessingStage downloadingStage, DownloadStats downloadStats) {
        int peerCount = sessionState.getConnectedPeers().size();
        String up = String.format("%5.1f %2s/s", downloadStats.getUploadRate().getQuantity(), downloadStats.getUploadRate().getMeasureUnit());

        switch (downloadingStage) {
            case DOWNLOADING: {
                double completePercents = ((double) sessionState.getPiecesComplete()) / ((double) sessionState.getPiecesTotal()) * 100;
                double requiredPercents = (((double) sessionState.getPiecesComplete()) + ((double) sessionState.getPiecesRemaining())) / ((double) sessionState.getPiecesTotal()) * 100;
                String down = String.format("%5.1f %2s/s", downloadStats.getDownloadRate().getQuantity(), downloadStats.getDownloadRate().getMeasureUnit());
                String elapsedTime = getDuration(downloadStats.getElapsedTime());
                String remainingTime = formatTime(getRemainingTime(torrent.getChunkSize(),
                        downloadStats.getDownloadRate().getBytes(), sessionState.getPiecesRemaining()));

                System.out.println("Dwonloading from " + peerCount + " peers... Ready: " + completePercents + ", Target: " + requiredPercents + ", Down: " + down + ", Up: " + up + ", Elapsed: " + elapsedTime +", Remaining: " + remainingTime);
                break;
            }
            case SEEDING: {
                System.out.println("Download was completed, seeding to " + peerCount + " peers... Up: " + up);
                break;
            }
            default: {
                break;
            }
        }
    }

    private static String formatTime(int remainingSeconds) {
        if (remainingSeconds < 0) {
            return "\u221E"; // infinity
        } else {
            return getDuration(Duration.ofSeconds(remainingSeconds));
        }
    }

    private static String getDuration(Duration elapsedTime) {
        long seconds = elapsedTime.getSeconds();
        long absSeconds = Math.abs(seconds);
        String positive = String.format("%d:%02d:%02d", absSeconds / 3600, (absSeconds % 3600) / 60, absSeconds % 60);
        return seconds < 0 ? "-" + positive : positive;
    }

    private Duration returnTimeElapsed() {
        return Duration.ofMillis(System.currentTimeMillis() - this.started);
    }

    public void stop() {
        this.shutdown.set(true);
    }

    private static int getRemainingTime(long chunkSize, long downloaded, int piecesRemaining) {
        if (downloaded == 0) {
            return -1;
        }
        long remainingBytes = chunkSize * piecesRemaining;
        return (int) (remainingBytes / downloaded);
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
