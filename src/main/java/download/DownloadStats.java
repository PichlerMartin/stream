package download;

import java.time.Duration;

/**
 * <p>this class serves as a provider for the download-stats, including the elapsed time,
 * download rate and upload rate. for reasons of simplification the will be no extra documentation
 * about the single field, instead they will be described upon the constructor header briefly</p>
 *
 * @author PichlerMartin
 * @since december 2019
 */
public class DownloadStats {
    private final Duration elapsedTime;
    private final DownloadRate downloadRate;
    private final DownloadRate uploadRate;

    /**
     * <p>constructor method for download stats</p>
     * @param elapsedTime the already elapsed time in seconds
     * @param downloadRate the download rate served as DownloadRate object
     * @param uploadRate the upload rate served as DownloadRate object
     * @see DownloadRate
     */
    public DownloadStats(Duration elapsedTime, DownloadRate downloadRate, DownloadRate uploadRate) {
        this.elapsedTime = elapsedTime;
        this.downloadRate = downloadRate;
        this.uploadRate = uploadRate;
    }

    public Duration getElapsedTime() {
        return this.elapsedTime;
    }

    public DownloadRate getDownloadRate() {
        return this.downloadRate;
    }

    @Deprecated
    public DownloadRate getUploadRate() {
        return this.uploadRate;
    }
}
