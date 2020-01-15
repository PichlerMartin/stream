package client;

import java.io.File;

/**
 * <p>This class contains a sum of private fields which will be used to instantiate an object of
 * the StreamClient-class in order to create a torrent-client. More accurate descriptions of the
 * fields can be found in the corresponding constructors. The methods left serve as getter methods</p>
 *
 * @since 0.00.2
 * @author PichlerMartin
 */
public class StreamOptions {
    /**
     * pointer to .torrent-file for download
     *
     * @since 0.00.1
     */
    private File metainfoFile;

    /**
     * magnet uri/link with download information
     *
     * @since 0.00.1
     */
    private String magnetUri;

    /**
     * pointer to download directory for torrent contents
     *
     * @since 0.00.1
     */
    private File targetDirectory;

    /**
     * boolean whether should shut down or seed after download is finished
     *
     * @since 0.00.1
     */
    private boolean seedAfterDownloaded;

    /**
     * sequential or random download
     *
     * @since 0.00.1
     */
    private boolean sequential;

    /**
     * enforcing encryption or leaving blank
     *
     * @since 0.00.1
     */
    private boolean enforceEncryption;

    /**
     * enables verbose logging in {@link StreamClient}
     *
     * @since 0.00.1
     */
    private boolean verboseLogging;

    /**
     * enables trace logging in {@link StreamClient}
     *
     * @since 0.00.1
     */
    private boolean traceLogging;

    /**
     * custom ip-address for incoming connections
     *
     * @since 0.00.1
     */
    private String inetAddress;

    /**
     * custom port for incoming connections
     *
     * @since 0.00.1
     */
    private Integer port;

    /**
     * custom port for dht conections
     *
     * @since 0.00.1
     */
    private Integer dhtPort;

    /**
     * boolean value whether download all or single files
     *
     * @since 0.00.1
     */
    private boolean downloadAllFiles;

    /**
     * This is tje "master"-constructor method of the StreamOptions class. It takes all possible parameters
     * which can be used by the StreamOptions-class. It is marked as deprecated because there is no actual
     * use of all possible parameters in the current version of stream yet. If you want to use one of the
     * additional parameters you will need to write a new constructor with the parameters you need.
     *
     * @param metainfoFile a .torrent-file which can be used to provide meta data of a torrent
     * @param magnetUri magnet uri/link which can be used as an alternative to metainfoFile
     * @param targetDirectory the directory in which the torrent is stored
     * @param seedAfterDownloaded boolean, whether the client should seed the torrent after downloading has finished
     * @param sequential boolean, defines whether torrent-parts should be downloaded one after another or randomly
     * @param enforceEncryption boolean, says whether encrypted connections should be used
     * @param verboseLogging defines verbose logging level
     * @param traceLogging defines trace logging level
     * @param inetAddress a specific internet-address to which the client should connect
     * @param port specific port for incoming connections, if empty default port is used
     * @param dhtPort specific port for the dht connection
     * @param downloadAllFiles boolean, specifies whether all or only some files should be downloaded
     * @author PichlerMartin
     * @since 0.00.2
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
     * <p>This overloaded constructor of StreamOptions serves as the main constructor it is called in StreamClient
     * and accepts a subdivision of all possible arguments. It is tailored for the capabilities of stream and
     * features also some logical operations such as an evaluation on whether the default port is used or not
     * and if the meta information is provided via a magnet uri/link or a .torrent-file
     * </p>
     *
     * {@link client.StreamClient#main(String[]) usage of this constructor}
     *
     * @param metainfoFile a .torrent-file which can be used to provide meta data of a torrent
     * @param magnetUri magnet uri/link which can be used as an alternative to metainfoFile
     * @param targetDirectory the directory in which the torrent is stored
     * @param seedAfterDownloaded boolean, whether the client should seed the torrent after downloading has finished
     * @param port specific port for incoming connections, if empty default port is used
     * @param downloadAllFiles boolean, specifies whether all or only some files should be downloaded
     * @author PichlerMartin
     * @since 0.00.6
     */
    @SuppressWarnings("all")
    public StreamOptions(String magnetUri, File metainfoFile, File targetDirectory, boolean seedAfterDownloaded, boolean downloadAllFiles, boolean useDefaultPort, Integer port, boolean useMagnetLink, boolean useTorrentFile) {
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

    /**
     * <p>The following constructor contains the most basic data needed to instantiate a version
     * of the StreamOptions-class. It contains a magnet uri/link and a download location and no
     * other data. The magnetUri could be replaced by a .torrent-file.</p>
     *
     * @see StreamOptions#StreamOptions(String, File, File, boolean, boolean, boolean, Integer, boolean, boolean)
     * @since 0.00.2
     * @author PichlerMartin
     * @param magnetUri magnet uri/link which can be used as an alternative to metainfoFile
     * @param targetDirectory the directory in which the torrent is stored
     */
    public StreamOptions(String magnetUri, File targetDirectory) {
        this.magnetUri = magnetUri;
        this.targetDirectory = targetDirectory;
    }

    /**
     * <p>The following constructor exclusively contains a magnet link and
     * is not in productive use. it was created for testing purposes and is
     * no longer maintained.
     * </p>
     *
     * @param magnetUri magnet uri/link which can be used as an alternative to metainfoFile
     * @see StreamOptions#StreamOptions(String, File, File, boolean, boolean, boolean, Integer, boolean, boolean)
     * @since 0.00.4
     * @author PichlerMartin
     */
    @Deprecated
    public StreamOptions(String magnetUri) {
        this.magnetUri = magnetUri;
    }

    /**
     * <p>The following constructor exclusively contains a .torrent-file reference
     * and is not in productive use. it was created for testing purposes and is
     * no longer maintained.
     * </p>
     *
     * @param metainfoFile a .torrent-file which can be used to provide meta data of a torrent
     * @see StreamOptions#StreamOptions(String, File, File, boolean, boolean, boolean, Integer, boolean, boolean)
     * @since 0.00.4
     * @author PichlerMartin
     */
    @Deprecated
    public StreamOptions(File metainfoFile) {
        this.metainfoFile = metainfoFile;
    }

    /**
     * A simple getter method for metainfoFile
     *
     * @return returns path to .torrent-file
     * @since 0.00.1
     * @author PichlerMartin
     */
    File getMetainfoFile() {
        return this.metainfoFile;
    }

    /**
     * A simple getter method for magnetURI
     *
     * @return returns magnet uri/link-String
     * @since 0.00.1
     * @author PichlerMartin
     */
    String getMagnetUri() {
        return this.magnetUri;
    }

    /**
     * A simple getter method for {@link StreamOptions#targetDirectory}
     *
     * @return returns the target directory
     * @since 0.00.1
     * @author PichlerMartin
     */
    File getTargetDirectory() {
        return this.targetDirectory;
    }

    /**
     * A simple getter method which for {@link StreamOptions#seedAfterDownloaded}
     *
     * @return returns boolean seedAfterDownload
     * @since 0.00.1
     * @author PichlerMartin
     */
    boolean shouldSeedAfterDownloaded() {
        return this.seedAfterDownloaded;
    }

    /**
     * A simple getter method for field {@link StreamOptions#sequential}
     *
     * @return returns boolean sequential
     * @since 0.00.1
     * @author PichlerMartin
     */
    boolean downloadSequentially() {
        return this.sequential;
    }

    /**
     * A simple getter method for field {@link StreamOptions#enforceEncryption}
     *
     * @return returns boolean enforceEncryption
     * @since 0.00.1
     * @author PichlerMartin
     */
    public boolean enforceEncryption() {
        return this.enforceEncryption;
    }

    /**
     * getter method for which evaluates whether {@link StreamOptions#traceLogging} or
     * {@link StreamOptions#verboseLogging} is enabled
     *
     * @return returns set log level to configure logging in {@link StreamClient#main(String[]) StreamClient}
     * @since 0.00.1
     * @author PichlerMartin
     */
    public LogLevel getLogLevel() {
        return this.traceLogging ? LogLevel.TRACE :
                (this.verboseLogging ? LogLevel.VERBOSE :
                        LogLevel.NORMAL);
    }

    /**
     * getter method for {@link StreamOptions#inetAddress}
     *
     * @return returns inetAdress
     * @since 0.00.1
     * @author PichlerMartin
     */
    public String getInetAddress() {
        return this.inetAddress;
    }

    /**
     * getter method for {@link StreamOptions#port}
     *
     * @return returns custom download port set by {@link StreamOptions} constructor
     * @since 0.00.1
     * @author PichlerMartin
     */
    public Integer getPort() {
        return this.port;
    }

    /**
     * getter method for {@link StreamOptions#dhtPort}
     *
     * @return returns dhtPort, variable which port is in use for dht-connections
     * @since 0.00.1
     * @author PichlerMartin
     */
    public Integer getDhtPort() {
        return this.dhtPort;
    }

    /**
     * getter method for {@link StreamOptions#downloadAllFiles}
     *
     * @return returns boolean downloadAllFiles
     * @since 0.00.1
     * @author PichlerMartin
     */
    boolean shouldDownloadAllFiles() {
        return this.downloadAllFiles;
    }

    /**
     * Logging level for the return value of getLogLevel within this class
     *
     * @see StreamOptions#getLogLevel()
     * @since 0.00.1
     * @author PichlerMartin
     */
    public enum LogLevel {
        NORMAL,
        VERBOSE,
        TRACE;

        LogLevel() {
        }
    }
}