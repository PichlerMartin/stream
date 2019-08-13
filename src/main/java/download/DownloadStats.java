package download;

import java.time.Duration;

public class DownloadStats {
    private final Duration elapsedTime;
    private final DownloadRate downloadRate;
    private final DownloadRate uploadRate;

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

    public DownloadRate getUploadRate() {
        return this.uploadRate;
    }
}
