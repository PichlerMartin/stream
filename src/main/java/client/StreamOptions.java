package client;

import java.io.File;

/**
 * Desc:
 *
 */
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

    /**
     * Desc:
     *
     * This is tje "master"-constructor method of the StreamOptions class. It takes all possible parameters
     * which can be used by the StreamOptions-class. It is marked as deprecated because there is no actual
     * use of all possible parameters in the current version of stream yet. If you want to use one of the
     * additional parameters you will need to write a new constructor with the parameters you need.
     *
     * @param metainfoFile: a .torrent-file which can be used to provide meta data of a torrent
     * @param magnetUri: magnet uri/link which can be used as an alternative to metainfoFile
     * @param targetDirectory: the directory in which the torrent is stored
     * @param seedAfterDownloaded: boolean, whether the client should seed the torrent after downloading has finished
     * @param sequential: boolean, defines whether torrent-parts should be downloaded one after another or randomly
     * @param enforceEncryption: boolean, says whether encrypted connections should be used
     * @param verboseLogging: defines verbose logging level
     * @param traceLogging: defines trace logging level
     * @param inetAddress: a specific internet-address to which the client should connect
     * @param port: specific port for incoming connections, if empty default port is used
     * @param dhtPort: specific port for the dht connection
     * @param downloadAllFiles: boolean, specifies whether all or only some files should be downloaded
     */
    @Deprecated
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

    /**
     * Desc:
     *
     * This overloaded constructor of StreamOptions serves as the main constructor it is called in StreamClient
     * and accepts a subdivision of all possible arguments. It is tailored for the capabilities of stream and
     * features also some logical operations such as an evaluation on whether the default port is used or not
     * and if the meta information is provided via a magnet uri/link or a .torrent-file
     *
     * {@link StreamClient}
     *
     * @param metainfoFile: a .torrent-file which can be used to provide meta data of a torrent
     * @param magnetUri: magnet uri/link which can be used as an alternative to metainfoFile
     * @param targetDirectory: the directory in which the torrent is stored
     * @param seedAfterDownloaded: boolean, whether the client should seed the torrent after downloading has finished
     * @param port: specific port for incoming connections, if empty default port is used
     * @param downloadAllFiles: boolean, specifies whether all or only some files should be downloaded
     */
    StreamOptions(String magnetUri, File metainfoFile, File targetDirectory, boolean seedAfterDownloaded, boolean downloadAllFiles, boolean useDefaultPort, Integer port, boolean useMagnetLink, boolean useTorrentFile) {
        this.seedAfterDownloaded = seedAfterDownloaded;
        this.downloadAllFiles = downloadAllFiles;
        this.targetDirectory = targetDirectory;

        if (!useDefaultPort){
            this.port = port;
        }

        if (useMagnetLink && !useTorrentFile){
            this.magnetUri = magnetUri;
        } else if (useTorrentFile && !useMagnetLink){
            this.metainfoFile = metainfoFile;
        } else {
            Thread.currentThread().interrupt();
        }
    }

    public StreamOptions(String magnetUri, File targetDirectory) {
        this.magnetUri = magnetUri;
        this.targetDirectory = targetDirectory;
    }

    /**
     * Desc:
     *
     * <p>The following constructor exclusively contains a magnet link and
     * is not in productive use. it was created for testing purposes and is
     * no longer maintained.
     * </p>
     *
     * @param magnetUri: magnet uri/link which can be used as an alternative to metainfoFile
     * @see StreamOptions#StreamOptions(String, File, File, boolean, boolean, boolean, Integer, boolean, boolean)
     * {@link client.StreamOptions#StreamOptions(String, File, File, boolean, boolean, boolean, Integer, boolean, boolean) constructor with all possible parameters}
     * @since 0.00.4
     */
    @Deprecated
    public StreamOptions(String magnetUri) {
        this.magnetUri = magnetUri;
    }

    /**
     * Desc:
     *
     * <p>The following constructor exclusively contains a .torrent-file reference
     * and is not in productive use. it was created for testing purposes and is
     * no longer maintained.
     * </p>
     *
     * @param metainfoFile: a .torrent-file which can be used to provide meta data of a torrent
     * @see StreamOptions#StreamOptions(String, File, File, boolean, boolean, boolean, Integer, boolean, boolean)
     * {@link client.StreamOptions#StreamOptions(String, File, File, boolean, boolean, boolean, Integer, boolean, boolean) constructor with all possible parameters}
     * @since 0.00.4
     */
    @Deprecated
    public StreamOptions(File metainfoFile) {
        this.metainfoFile = metainfoFile;
    }

    /**
     * Desc
     *
     * @return
     */
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
        return this.downloadAllFiles;
    }

    /**
     *
     */
    public enum LogLevel {
        NORMAL,
        VERBOSE,
        TRACE;

        LogLevel() {
        }
    }
}