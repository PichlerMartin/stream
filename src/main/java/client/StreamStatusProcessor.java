package client;

import bt.metainfo.Torrent;
import bt.torrent.TorrentSessionState;
import download.DownloadRate;
import download.DownloadStats;
import javafx.scene.control.Label;
import support.StreamContext;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static client.StreamStatusProcessor.ExecutionStage.DOWNLOADING;
import static java.lang.String.format;
import static support.Globals.*;

public class StreamStatusProcessor {
    private AtomicReference<Torrent> torrent;
    private AtomicReference<TorrentSessionState> sessionState;

    //endregion getters and setter
    private AtomicReference<ExecutionStage> processingStage;
    private AtomicBoolean shutdown;
    private long started;
    private long downloaded;
    private long uploaded;

    StreamStatusProcessor() {
        this.torrent = new AtomicReference<>(null);
        this.sessionState = new AtomicReference<>(null);
        this.processingStage = new AtomicReference<>(ExecutionStage.FETCHING_METADATA);
        this.shutdown = new AtomicBoolean(false);
    }

    private static String formatTime(int remainingSeconds) {
        if (remainingSeconds < 0) {
            return "\u221E"; // infinity
        } else {
            return getDuration(Duration.ofSeconds(remainingSeconds));
        }
    }

    private static double calculateCompletePercentage(double total, double completed) {
        return completed / total * 100.0D;
    }

    private static double calculateTargetPercentage(double total, double completed, double remaining) {
        return (completed + remaining) / total * 100.0D;
    }

    private static String formatDownloadRate(DownloadRate rate) {
        return String.format("%5.1f %2s/s", rate.getQuantity(), rate.getMeasureUnit());
    }

    private static int getRemainingTime(long chunkSize, long downloaded, int piecesRemaining) {
        if (downloaded == 0) {
            return -1;
        }
        long remainingBytes = chunkSize * piecesRemaining;
        return (int) (remainingBytes / downloaded);
    }

    private static String getDuration(Duration elapsedTime) {
        long seconds = elapsedTime.getSeconds();
        long absSeconds = Math.abs(seconds);
        String positive = format("%d:%02d:%02d", absSeconds / 3600L, (absSeconds % 3600L) / 60L, absSeconds % 60L);
        return seconds < 0 ? "-" + positive : positive;
    }

    //region    getters and setters
    public AtomicReference<Torrent> getTorrent() {
        return torrent;
    }

    public void setTorrent(AtomicReference<Torrent> torrent) {
        this.torrent = torrent;
    }

    void updateTorrentStage(TorrentSessionState sessionState) {
        this.sessionState.set(sessionState);
    }

    void whenTorrentFetched(Torrent torrent) {
        System.out.println("Downloading now");
        System.out.println(format(FORMAT_DOWNLOADING_SILENT, torrent.getName(), torrent.getSize()));
        StreamContext.getInstance().currentController().setLabel(new Label("Torrent fetched"));
        this.torrent.set(torrent);
        this.processingStage.set(ExecutionStage.CHOOSING_FILES);
    }

    void onFilesChosen() {
        this.processingStage.set(DOWNLOADING);
    }

    void onDownloadComplete() {
        this.processingStage.set(ExecutionStage.SEEDING);
    }

    void startStatusProcessor() {
        this.started = System.currentTimeMillis();
        System.out.println("Metadata is being fetched... Please be patient...");

        Thread logPrintThread = new Thread(() -> {
            do {
                TorrentSessionState sessionState = this.sessionState.get();
                if (sessionState != null) {
                    Duration elapsed = this.returnTimeElapsed();
                    DownloadRate down = new DownloadRate(sessionState.getDownloaded() - this.downloaded);
                    DownloadRate up = new DownloadRate(sessionState.getDownloaded() - this.uploaded);

                    processStatus(torrent.get(), sessionState, processingStage.get(), new DownloadStats(elapsed, down, up));

                    this.downloaded = sessionState.getDownloaded();
                    this.uploaded = sessionState.getUploaded();
                }

                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                    return;
                }
            } while (!this.shutdown.get());
        });

        logPrintThread.setDaemon(true);
        logPrintThread.start();
    }

    private void processStatus(Torrent torrent, TorrentSessionState sessionState, ExecutionStage executionStage, DownloadStats downloadStats) {
        int peerCount = sessionState.getConnectedPeers().size();
        String down = formatDownloadRate(downloadStats.getDownloadRate());
        String up = formatDownloadRate(downloadStats.getDownloadRate());

        switch (executionStage) {
            case DOWNLOADING: {
                double completePercents = calculateCompletePercentage(sessionState.getPiecesTotal(), sessionState.getPiecesComplete());
                double requiredPercents = calculateTargetPercentage(sessionState.getPiecesTotal(), sessionState.getPiecesComplete(), sessionState.getPiecesRemaining());

                String elapsedTime = getDuration(downloadStats.getElapsedTime());
                String remainingTime = formatTime(getRemainingTime(torrent.getChunkSize(),
                        downloadStats.getDownloadRate().getBytes(), sessionState.getPiecesRemaining()));

                System.out.println(format(FORMAT_DOWNLOADING_VERBOSE, peerCount, completePercents, requiredPercents, down, up, elapsedTime, remainingTime));
                break;
            }
            case SEEDING: {
                System.out.println(format(FORMAT_SEEDING, peerCount, up));
                break;
            }
            default: {
                break;
            }
        }
    }

    private Duration returnTimeElapsed() {
        return Duration.ofMillis(System.currentTimeMillis() - this.started);
    }

    void stop() {
        this.shutdown.set(true);
    }

    public enum ExecutionStage {
        FETCHING_METADATA,
        CHOOSING_FILES,
        DOWNLOADING,
        SEEDING;

        ExecutionStage() {
        }
    }
}
