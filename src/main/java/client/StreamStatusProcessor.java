package client;

import bt.metainfo.Torrent;
import bt.torrent.TorrentSessionState;
import download.DownloadRate;
import download.DownloadStats;
import javafx.scene.control.Label;
import support.StreamContext;

import java.io.*;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static client.StreamStatusProcessor.ExecutionStage.DOWNLOADING;
import static java.lang.String.format;
import static support.Globals.*;

/**
 * <p>Class which serves as a status printer in order to display download stats and date while downloading.
 * It is invoked when a new StreamClient ist started and as soon as the torrent is fetched successfully from
 * the network it displays the statistics of the download with the help of formatting strings from {@link support.Globals}.</p>
 *
 * <p>It was initially intended to redirect the output of this class to the UI but this function is not
 * implemented yet. To acheive this you may need a singleton class or a different programming pattern
 * in order to populate the ui's labels with the download data, e.g. download-rate and peer count.
 * </p>
 *
 * @see support.Globals#FORMAT_DOWNLOADING_VERBOSE
 * @see support.Globals#FORMAT_DOWNLOAD_PART
 * @see support.Globals#FORMAT_ILLEGAL_KEYPRESS
 * @see support.Globals#FORMAT_DOWNLOADING_SILENT
 * @see support.Globals#FORMAT_SEEDING
 *
 * @author PichlerMartin
 * @since 0.01.2
 */
public class StreamStatusProcessor {
    /**
     * <p>Atomic reference of torrent file, can not be modified by different threads
     *
     * @see Torrent
     * @see AtomicReference
     *
     * @since 0.01.2
     */
    private AtomicReference<Torrent> torrent;

    /**
     * <p>Atomic reference of TorrentSessionState, can not be modified by different threads
     *
     * @see TorrentSessionState
     * @see AtomicReference
     *
     * @since 0.01.2
     */
    private AtomicReference<TorrentSessionState> sessionState;

    /**
     * <p>Atomic reference of ExecutionsStage, can not be modified by different threads
     *
     * @see ExecutionStage
     * @see AtomicReference
     *
     * @since 0.01.2
     */
    private AtomicReference<ExecutionStage> executionStage;

    /**
     * <p>Atomic reference of boolean shutdown, depicts whether current status processor should
     * shut down or not</p>
     *
     * @since 0.01.2
     */
    private AtomicBoolean shutdown;

    /**
     * <p>time of start</p>
     *
     * @since 0.01.2
     */
    private long started;

    /**
     * <p>local variable for downloaded amount</p>
     *
     * @since 0.01.2
     */
    private long downloaded;

    /**
     * <p>local variable for uploaded (seeded) amount</p>
     *
     * @since 0.01.2
     */
    private long uploaded;

    /**
     * <p>default constructor for StreamStatusProcessor which initializes the local fields and AtomicReferences
     * with default values. It is used in the package-private constructor of {@link StreamClient StreamClient(StreamOptions options)}</p>
     *
     * @author PichlerMartin
     * @since 0.01.2
     */
    StreamStatusProcessor() {
        this.torrent = new AtomicReference<>(null);
        this.sessionState = new AtomicReference<>(null);
        this.executionStage = new AtomicReference<>(ExecutionStage.FETCHING_METADATA);
        this.shutdown = new AtomicBoolean(false);
    }

    /**
     * <p>formatting method, which returns the remaining time in seconds or the unicode string for
     * "infinity" if this is the case (no download is happening)</p>
     *
     * @param remainingSeconds Integer containing the estimated remaining seconds dependent on the current download
     *                         rate
     * @return call to method {@link StreamStatusProcessor#getDuration(Duration)}
     * @author PichlerMartin
     * @since 0.01.2
     */
    private static String formatTime(int remainingSeconds) {
        if (remainingSeconds < 0) {
            return "\u221E"; // infinity
        } else {
            return getDuration(Duration.ofSeconds(remainingSeconds));
        }
    }

    /**
     *<p>calculates the part of the downloaded amount of data</p>
     * @param total total torrent size
     * @param completed completed bytes
     * @return completed percentage of current torrent file (e.g. 50.5%, if 4 kB of 8 kB is downloaded)
     * @author PichlerMartin
     * @since 0.01.2
     */
    private static double calculateCompletePercentage(double total, double completed) {
        return completed / total * 100.0D;
    }

    /**
     * <p>calculates the targeted percentage, which should be downloaded, by default 100%</p>
     * @param total the percentage which is aimed for, by default 100%
     * @param completed the already downloaded percentage
     * @param remaining the remaining percentage
     * @return normaly 100%
     */
    private static double calculateTargetPercentage(double total, double completed, double remaining) {
        return (completed + remaining) / total * 100.0D;
    }

    private static String formatDownloadRate(DownloadRate rate) {
        return String.format("%5.1f %2s/s", rate.getQuantity(), rate.getMeasureUnit());
    }

    //region getters and setters

    private static int getRemainingTime(long chunkSize, long downloaded, int piecesRemaining) {
        if (downloaded == 0) {
            return -1;
        }
        long remainingBytes = chunkSize * piecesRemaining;
        return (int) (remainingBytes / downloaded);
    }

    /**
     * <p>calculates and format the elapsed time of the current download</p>
     * @param elapsedTime elapsed time as object of Duration
     * @return the unsigned elapsed time as String
     */
    private static String getDuration(Duration elapsedTime) {
        long seconds = elapsedTime.getSeconds();
        long absSeconds = Math.abs(seconds);
        String positive = format("%d:%02d:%02d", absSeconds / 3600L, (absSeconds % 3600L) / 60L, absSeconds % 60L);
        return seconds < 0 ? "-" + positive : positive;
    }

    public AtomicReference<Torrent> getTorrent() {
        return torrent;
    }

    /**
     * <p>sets the atomic reference on the torrent file which should be initialised</p>
     *
     * @see StreamStatusProcessor#torrent
     *
     * @author PichlerMartin
     * @since 0.01.4
     */
    public void setTorrent(AtomicReference<Torrent> torrent) {
        this.torrent = torrent;
    }

    //endregion    getters and setters

    /**
     * <p>updates the current torrent session state</p>
     *
     * @see TorrentSessionState
     *
     * @author PichlerMartin
     * @since 0.01.4
     */
    void updateTorrentStage(TorrentSessionState sessionState) {
        this.sessionState.set(sessionState);
    }

    /**
     * <p>after the torrent was found in the tracker-network its name is displayed in the
     * command line and afterwards the downloading starts, except file selection was selected
     * in the user interface.</p>
     *
     * @author PichlerMartin
     * @since 0.01.4
     */
    void whenTorrentFetched(Torrent torrent) {
        System.out.println("Downloading now");
        System.out.println(format(FORMAT_DOWNLOADING_SILENT, torrent.getName(), torrent.getSize()));
        StreamContext.getInstance().currentController().setLabel(new Label("Torrent fetched"));
        this.torrent.set(torrent);
        this.executionStage.set(ExecutionStage.CHOOSING_FILES);
    }

    /**
     * <p>sets execution stage to {@link ExecutionStage#DOWNLOADING} after files have
     * been chosen</p>
     *
     * @author PichlerMartin
     * @since 0.01.4
     */
    void onFilesChosen() {
        this.executionStage.set(DOWNLOADING);
    }

    /**
     * <p>sets execution stage to {@link ExecutionStage#SEEDING} after downloading
     * is finished</p>
     *
     * @author PichlerMartin
     * @since 0.01.4
     */
    void onDownloadComplete() {
        this.executionStage.set(ExecutionStage.SEEDING);
    }

    /**
     * <p>this method takes the current time as a start time, for later display of duration time and other statistics.
     * it uses a new thread in which different locales like session state and down- and upload-rates ({@link DownloadRate}
     * are given to the method {@link StreamStatusProcessor#processStatus(Torrent, TorrentSessionState, ExecutionStage, DownloadStats)}
     * , until the download is finished (no seeding) and then terminated.</p>
     *
     * @author PichlerMartin
     * @since 0.01.5
     */
    void startStatusProcessor() {
        this.started = System.currentTimeMillis();

        /*
        A command-line interface is being invoked here, because the output of the download stats does not work
        yet via the graphical user interface

        18.01.2020
        PichlerMartin
         */
        try {
            Process p = Runtime.getRuntime().exec("cmd.exe /c start dir ");
            BufferedWriter writeer = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
            writeer.write("dir");
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Metadata is being fetched... Please be patient...");

        Thread logPrintThread = new Thread(() -> {
            do {
                TorrentSessionState sessionState = this.sessionState.get();
                if (sessionState != null) {
                    Duration elapsed = this.returnTimeElapsed();
                    DownloadRate down = new DownloadRate(sessionState.getDownloaded() - this.downloaded);
                    DownloadRate up = new DownloadRate(sessionState.getDownloaded() - this.uploaded);

                    processStatus(torrent.get(), sessionState, executionStage.get(), new DownloadStats(elapsed, down, up));

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

    /**
     * <p>this method evaluates the current execution stage, choosing from either {@link ExecutionStage#DOWNLOADING} or
     * {@link ExecutionStage#SEEDING}. it uses the formatting methods found in this class to correctly display the output
     * on the command line interface. this method is called by {@link StreamStatusProcessor#startStatusProcessor()}</p>
     *
     * @param torrent the currently active torrent
     * @param sessionState object of current downloading session
     * @param executionStage current execution stage, see {@link ExecutionStage}
     * @param downloadStats object of {@link DownloadStats}
     * @author PichlerMartin
     * @since 0.01.5
     */
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

    /**
     * <p>this method returns a object of Duration describing the already elapsed time for a
     * upload/download</p>
     *
     * @return total elapsed time since start of download
     *
     * @author PichlerMartin
     * @since 0.01.2
     */
    private Duration returnTimeElapsed() {
        return Duration.ofMillis(System.currentTimeMillis() - this.started);
    }

    /**
     * <p>stops the status processor, for example when ExecutionStage in
     * {@link StreamClient#start()} is invalid.</p>
     *
     * @see ExecutionStage
     *
     * @author PichlerMartin
     * @since 0.01.2
     */
    void stop() {
        this.shutdown.set(true);
    }

    /**
     * <p>an enum field containing all possible execution-stages</p>
     *
     * <p>FETCHING_METADATA is active before the download starts</p>
     * <p>CHOOSING_FILES is active if not all files should be downloaded</p>
     * <p>DOWNLOADING indicates the downloading phase</p>
     * <p>SEEDING indicates the seeding phase</p>
     *
     * @see ExecutionStage
     *
     * @author PichlerMartin
     * @since 0.01.2
     */
    public enum ExecutionStage {
        FETCHING_METADATA,
        CHOOSING_FILES,
        DOWNLOADING,
        SEEDING;

        ExecutionStage() {
        }
    }
}