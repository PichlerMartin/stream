package client;

import java.io.File;

public class StreamOptions {
    private File metainfoFile;
    private String magnetUri;
    private File targetDirectory;
    private boolean seedAfterDownloaded;
    private boolean sequential;
    private boolean enforceEncryption;
    private boolean verboseLogging;
    private boolean traceLogging;
    private String inetAddress;
    private Integer port;
    private Integer dhtPort;
    private boolean downloadAllFiles;

    public StreamOptions(File metainfoFile, String magnetUri, File targetDirectory, boolean seedAfterDownloaded, boolean sequential, boolean enforceEncryption, boolean verboseLogging, boolean traceLogging, String inetAddress, Integer port, Integer dhtPort, boolean downloadAllFiles) {
        this.metainfoFile = metainfoFile;
        this.magnetUri = magnetUri;
        this.targetDirectory = targetDirectory;
        this.seedAfterDownloaded = seedAfterDownloaded;
        this.sequential = sequential;
        this.enforceEncryption = enforceEncryption;
        this.verboseLogging = verboseLogging;
        this.traceLogging = traceLogging;
        this.inetAddress = inetAddress;
        this.port = port;
        this.dhtPort = dhtPort;
        this.downloadAllFiles = downloadAllFiles;
    }

    public StreamOptions(String magnetUri, File targetDirectory){
        this.magnetUri = magnetUri;
        this.targetDirectory = targetDirectory;
    }

    File getMetainfoFile() {
        return this.metainfoFile;
    }

    String getMagnetUri() {
        return this.magnetUri;
    }

    File getTargetDirectory() {
        return this.targetDirectory;
    }

    boolean shouldSeedAfterDownloaded() {
        return this.seedAfterDownloaded;
    }

    boolean downloadSequentially() {
        return this.sequential;
    }

    public boolean enforceEncryption() {
        return this.enforceEncryption;
    }

    public LogLevel getLogLevel() {
        return this.traceLogging ? LogLevel.TRACE :
                (this.verboseLogging ? LogLevel.VERBOSE :
                        LogLevel.NORMAL);
    }

    public String getInetAddress() {
        return this.inetAddress;
    }

    public Integer getPort() {
        return this.port;
    }

    public Integer getDhtPort() {
        return this.dhtPort;
    }

    boolean shouldDownloadAllFiles() {
        return !this.downloadAllFiles;
    }

    public enum LogLevel {
        NORMAL,
        VERBOSE,
        TRACE;

        LogLevel() {
        }
    }
}
