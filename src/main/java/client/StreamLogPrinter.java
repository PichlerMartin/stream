package client;

import bt.metainfo.Torrent;
import bt.torrent.TorrentSessionState;
import download.DownloadRate;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class StreamLogPrinter {
    private AtomicReference<Torrent> torrent = new AtomicReference((Object)null);
    private AtomicReference<TorrentSessionState> sessionState = new AtomicReference((Object)null);
    private AtomicReference<DownloadingStage> processingStage;
    private AtomicBoolean shutdown;
    private long started;
    private long downloaded;
    private long uploaded;

    public StreamLogPrinter(){
        this.processingStage = new AtomicReference<>(DownloadingStage.FETCHING_METADATA);
        this.shutdown = new AtomicBoolean(false);
    }

    public void updateTorrentStage(TorrentSessionState sessionState){this.sessionState.set(sessionState);}

    public void whenTorrentFetched(Torrent torrent){
        this.torrent.set(torrent);
        this.processingStage.set(DownloadingStage.DOWNLOADING);
    }

    public void onDownloadComplete() {

        this.processingStage.set(StreamLogPrinter.DownloadingStage.SEEDING);
    }

    public void startLogPrinter(){
        this.started = System.currentTimeMillis();
        System.out.println("Metadata is being fetched... Please be pacient...");

        Thread t = new Thread(() ->{
            do{
                TorrentSessionState sessionState = (TorrentSessionState)this.sessionState.get();
                if(sessionState != null){
                    Duration elapsedtime = this.returnTimeElapsed();
                    DownloadRate downloadRate = new DownloadRate(sessionState.getDownloaded() - this.downloaded);
                    DownloadRate uploadRate = new DownloadRate(sessionState.getDownloaded() - this.uploaded);
                    this.downloaded = sessionState.getDownloaded();
                    this.uploaded = sessionState.getUploaded();
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex){
                    return;
                }
            } while (this.shutdown.get());
        });
    }

    private Duration returnTimeElapsed() {
        return Duration.ofMillis(System.currentTimeMillis() - this.started);
    }

    public void stop() {
        this.shutdown.set(true);
    }

    public static enum DownloadingStage {
        FETCHING_METADATA,
        DOWNLOADING,
        SEEDING;

        private DownloadingStage() {
        }
    }
}
