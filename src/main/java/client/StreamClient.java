package client;

import bt.Bt;
import bt.BtClientBuilder;
import bt.data.Storage;
import bt.data.file.FileSystemStorage;
import bt.dht.DHTConfig;
import bt.dht.DHTModule;
import bt.protocol.crypto.EncryptionPolicy;
import bt.runtime.BtClient;
import bt.runtime.BtRuntime;
import bt.runtime.Config;
import bt.service.IRuntimeLifecycleBinder;
import bt.torrent.selector.PieceSelector;
import bt.torrent.selector.RarestFirstSelector;
import bt.torrent.selector.SequentialSelector;
import com.google.inject.Module;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import streamUI.UI_Controller_main_page;
import support.SupportMethods;

import java.io.File;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * <p>this class serves as a extensible client for torrent downloading. it contains a few private fields
 * used for various miscellaneous tasks such as {@link StreamOptions}, {@link StreamStatusProcessor} and
 * {@link BtClient}</p>
 */
public class StreamClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(StreamClient.class);

    private final StreamOptions options;
    private final StreamStatusProcessor processor;
    private final BtClient client;

    /**
     * <p>the main constructor of the stream torrent-client. it performs a number of various actions
     * in order to build a torrent client with the bt-api</p>
     *
     * <hr><blockquote><pre>
     *     Storage storage = new FileSystemStorage(options.getTargetDirectory().toPath());
     * </pre></blockquote></hr>
     * <p>initialises the download location, if given</p>
     *
     * <hr><blockquote><pre>
     *     PieceSelector selector = options.downloadSequentially() ? SequentialSelector.sequential() : RarestFirstSelector.randomizedRarest();
     *     BtClientBuilder clientBuilder = Bt.client(runtime).storage(storage).selector(selector);
     * </pre></blockquote></hr>
     * <p>initialises file selector, if download-all was disables and creates a {@link BtClientBuilder} object</p>
     *
     * <hr><blockquote><pre>
     *      if (options.getMetainfoFile() != null) {
     *             clientBuilder = clientBuilder.torrent(toUrl(options.getMetainfoFile()));
     *         } else {
     *             if (options.getMagnetUri() == null) {
     *                 throw new IllegalStateException("Torrent file or magnet URI is required");
     *             }
     *
     *             clientBuilder = clientBuilder.magnet(options.getMagnetUri());
     *         }
     * </pre></blockquote></hr>
     * <p>tests if a magnet uri/link or a .torrent-file is provided, if missing an exception is thrown</p>
     *
     * @see "https://github.com/atomashpolskiy/bt"
     * @param options object containing the different options for the client
     * @author PichlerMartin
     * @since january 2020
     */
    private StreamClient(StreamOptions options) {
        this.options = options;
        this.processor = new StreamStatusProcessor();

        Config config = buildConfig(options);
        BtRuntime runtime = BtRuntime.builder(config).module(buildDHTModule(options)).autoLoadModules().build();
        Storage storage = new FileSystemStorage(options.getTargetDirectory().toPath());
        PieceSelector selector = options.downloadSequentially() ? SequentialSelector.sequential() : RarestFirstSelector.randomizedRarest();
        BtClientBuilder clientBuilder = Bt.client(runtime).storage(storage).selector(selector);

        //  check if all files should be downloaded or not
        if (!options.shouldDownloadAllFiles()) {
            StreamFileSelector fileSelector = new StreamFileSelector();
            clientBuilder.fileSelector(fileSelector);
            runtime.service(IRuntimeLifecycleBinder.class).onShutdown(fileSelector::shutdown);
        }

        clientBuilder.afterTorrentFetched(this.processor::whenTorrentFetched);
        clientBuilder.afterFilesChosen(this.processor::onFilesChosen);

        if (options.getMetainfoFile() != null) {
            clientBuilder = clientBuilder.torrent(toUrl(options.getMetainfoFile()));
        } else {
            if (options.getMagnetUri() == null) {
                throw new IllegalStateException("Torrent file or magnet URI is required");
            }

            clientBuilder = clientBuilder.magnet(options.getMagnetUri());
        }

        this.client = clientBuilder.build();
    }

    /**
     * <p>main method, is called by private method in {@link UI_Controller_main_page}</p>
     * @param args String array containing the options, please use the debugger to examine its
     *             exact content
     * @author PichlerMartin
     * @since december 2019
     */
    public static void main(String[] args) {
        StreamOptions options = new StreamOptions(args[0], new File(args[2]), new File(args[1]), Boolean.valueOf(args[8]), Boolean.valueOf(args[3]), Boolean.valueOf(args[4]), Integer.valueOf(args[5]), Boolean.valueOf(args[6]), Boolean.valueOf(args[7]));

        SupportMethods.configureLogging(options.getLogLevel());
        SupportMethods.configureSecurity(LOGGER);
        SupportMethods.registerLog4jShutdownHook();

        StreamClient client = new StreamClient(options);
        client.start();
    }

    /**
     * <p>builds a new object of {@link Config}, whereas the network configuration is defined</p>
     * @param options options object
     * @return the new built config
     * @see SupportMethods#buildConfig(StreamOptions)
     */
    private static Config buildConfig(final StreamOptions options) {
        final Optional<InetAddress> acceptorAddressOverride = getAcceptorAddressOverride(options);
        final Optional<Integer> portOverride = tryGetPort(options.getPort());
        return new Config() {
            public InetAddress getAcceptorAddress() {
                return acceptorAddressOverride.orElseGet(super::getAcceptorAddress);
            }

            public int getAcceptorPort() {
                return portOverride.orElseGet(super::getAcceptorPort);
            }

            public int getNumOfHashingThreads() {
                return Runtime.getRuntime().availableProcessors();
            }

            public EncryptionPolicy getEncryptionPolicy() {
                return options.enforceEncryption() ? EncryptionPolicy.REQUIRE_ENCRYPTED : EncryptionPolicy.PREFER_PLAINTEXT;
            }
        };
    }

    /**
     * <p>attepts to retrieve the requested incoming connections port, if fail exception is thrown</p>
     * @param port port suggested by the user in the gui
     * @return returns either demanded port or empty port (-> standard port is used)
     */
    private static Optional<Integer> tryGetPort(Integer port) {
        if (port == null) {
            return Optional.empty();
        } else if (port >= 1024 && port <= 65535) {
            return Optional.of(port);
        } else {
            throw new IllegalArgumentException("Invalid port: " + port + "; expected 1024..65535");
        }
    }

    /**
     * <p>determines wheter the current default ip address for in and outgoing connections should
     * be tossed, and replaced by a new internet address</p>
     * @param options StreamOptions object, delivered by {@link StreamClient#StreamClient(StreamOptions)}
     * @return optional internet address or empty (default)
     */
    private static Optional<InetAddress> getAcceptorAddressOverride(StreamOptions options) {
        String inetAddress = options.getInetAddress();
        if (inetAddress == null) {
            return Optional.empty();
        } else {
            try {
                return Optional.of(InetAddress.getByName(inetAddress));
            } catch (UnknownHostException ex) {
                throw new IllegalArgumentException("Failed to parse the acceptor's internet address", ex);
            }
        }
    }

    /**
     * <p>Creates a new DHT-module (distributed hash table) over which the Torrent-download
     * being initialised</p>
     * <p>Erstellt das DHT Modul (distributed hash table) für den Port über den der
     * Download erfolgt</p>
     *
     * @param options das Options Objekt
     * @return the newly built DHT module
     * @see SupportMethods#buildDHTModule(StreamOptions)
     */
    private static Module buildDHTModule(StreamOptions options) {
        final Optional<Integer> dhtPortOverride = tryGetPort(options.getDhtPort());
        return new DHTModule(new DHTConfig() {
            public int getListeningPort() {
                return dhtPortOverride.orElseGet(super::getListeningPort);
            }

            public boolean shouldUseRouterBootstrap() {
                return true;
            }
        });
    }
    /**
     * <p>converts a file to URL</p>
     * @param file input file
     * @return the new URL
     * @see SupportMethods#toUrl(File)
     */
    private static URL toUrl(File file) {
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException ex) {
            throw new IllegalArgumentException("Unexpected error", ex);
        }
    }

    /**
     * <p>invokes the torrent client built in the previous steps. it simultaneously invokes
     * the status processor process and checks periodically if the download is completed/seeding
     * is completed.</p>
     *
     * @author PichlerMartin
     * @since summer 2019
     * @see BtClient#startAsync(Consumer, long)
     */
    public void start() {
        this.processor.startStatusProcessor();
        this.client.startAsync((torrentStage) -> {
            boolean complete = torrentStage.getPiecesRemaining() == 0;
            if (complete) {
                if (this.options.shouldSeedAfterDownloaded()) {
                    this.processor.onDownloadComplete();
                } else {
                    this.processor.stop();
                    this.client.stop();
                }
            }

            this.processor.updateTorrentStage(torrentStage);
        }, 1000L).join();
    }
}
