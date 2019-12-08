package client;

import bt.Bt;
import bt.BtClientBuilder;
import bt.cli.Options;
import bt.cli.SessionStatePrinter;
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
import meta.Globals;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.Security;
import java.util.Objects;
import java.util.Optional;

public class StreamClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(StreamClient.class);
    private final Options options;
    private final SessionStatePrinter printer;
    private final BtClient client;

    public static void main(String[] args) throws IOException {
        args = new String[]{"-d", Globals.DOWNLOAD_DIRECTORY, "-m", Globals.MAGNET_LINK};
        Options options = Options.parse(args);//new Options(MagnetLink, new File(DownloadDirectory));

        //configureLogging(options.getLogLevel());
        //configureSecurity();
        //registerLog4jShutdownHook();
        StreamClient client = new StreamClient(options);
        client.start();
    }

    private static void configureLogging(Options.LogLevel logLevel) {
        Level log4jLogLevel;
        switch((Options.LogLevel) Objects.requireNonNull(logLevel)) {
            case NORMAL:
                log4jLogLevel = Level.INFO;
                break;
            case VERBOSE:
                log4jLogLevel = Level.DEBUG;
                break;
            case TRACE:
                log4jLogLevel = Level.TRACE;
                break;
            default:
                throw new IllegalArgumentException("Unknown log level: " + logLevel.name());
        }

        Configurator.setLevel("bt", log4jLogLevel);
    }

    private static void configureSecurity() {
        String key = "crypto.policy";
        String value = "unlimited";

        try {
            Security.setProperty(key, value);
        } catch (Exception var3) {
            LOGGER.error(String.format("Failed to set security property '%s' to '%s'", key, value), var3);
        }

    }

    private static void registerLog4jShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                if (LogManager.getContext() instanceof LoggerContext) {
                    Configurator.shutdown((LoggerContext)LogManager.getContext());
                }

            }
        });
    }

    public StreamClient(Options options) {
        this.options = options;
        this.printer = new SessionStatePrinter(/*new UI_Controller()*/);
        Config config = buildConfig(options);
        BtRuntime runtime = BtRuntime.builder(config).module(buildDHTModule(options)).autoLoadModules().build();
        Storage storage = new FileSystemStorage(options.getTargetDirectory().toPath());
        PieceSelector selector = options.downloadSequentially() ? SequentialSelector.sequential() : RarestFirstSelector.randomizedRarest();
        BtClientBuilder clientBuilder = (BtClientBuilder)((BtClientBuilder)Bt.client(runtime).storage(storage)).selector((PieceSelector)selector);
        if (!options.shouldDownloadAllFiles()) {
            StreamFileSelector fileSelector = new StreamFileSelector();
            clientBuilder.fileSelector(fileSelector);
            ((IRuntimeLifecycleBinder)runtime.service(IRuntimeLifecycleBinder.class)).onShutdown(fileSelector::shutdown);
        }

        SessionStatePrinter var10001 = this.printer;
        //clientBuilder.afterTorrentFetched(var10001::onTorrentFetched);
        var10001 = this.printer;
        clientBuilder.afterFilesChosen(var10001::onFilesChosen);
        if (options.getMetainfoFile() != null) {
            clientBuilder = (BtClientBuilder)clientBuilder.torrent(toUrl(options.getMetainfoFile()));
        } else {
            if (options.getMagnetUri() == null) {
                throw new IllegalStateException("Torrent file or magnet URI is required");
            }

            clientBuilder = (BtClientBuilder)clientBuilder.magnet(options.getMagnetUri());
        }

        this.client = clientBuilder.build();
    }

    private static Config buildConfig(final Options options) {
        final Optional<InetAddress> acceptorAddressOverride = getAcceptorAddressOverride(options);
        final Optional<Integer> portOverride = tryGetPort(options.getPort());
        return new Config() {
            public InetAddress getAcceptorAddress() {
                return (InetAddress)acceptorAddressOverride.orElseGet(() -> {
                    return super.getAcceptorAddress();
                });
            }

            public int getAcceptorPort() {
                return (Integer)portOverride.orElseGet(() -> {
                    return super.getAcceptorPort();
                });
            }

            public int getNumOfHashingThreads() {
                return Runtime.getRuntime().availableProcessors();
            }

            public EncryptionPolicy getEncryptionPolicy() {
                return options.enforceEncryption() ? EncryptionPolicy.REQUIRE_ENCRYPTED : EncryptionPolicy.PREFER_PLAINTEXT;
            }
        };
    }

    private static Optional<Integer> tryGetPort(Integer port) {
        if (port == null) {
            return Optional.empty();
        } else if (port >= 1024 && port <= 65535) {
            return Optional.of(port);
        } else {
            throw new IllegalArgumentException("Invalid port: " + port + "; expected 1024..65535");
        }
    }

    private static Optional<InetAddress> getAcceptorAddressOverride(Options options) {
        String inetAddress = options.getInetAddress();
        if (inetAddress == null) {
            return Optional.empty();
        } else {
            try {
                return Optional.of(InetAddress.getByName(inetAddress));
            } catch (UnknownHostException var3) {
                throw new IllegalArgumentException("Failed to parse the acceptor's internet address", var3);
            }
        }
    }

    private static Module buildDHTModule(Options options) {
        final Optional<Integer> dhtPortOverride = tryGetPort(options.getDhtPort());
        return new DHTModule(new DHTConfig() {
            public int getListeningPort() {
                return (Integer)dhtPortOverride.orElseGet(() -> {
                    return super.getListeningPort();
                });
            }

            public boolean shouldUseRouterBootstrap() {
                return true;
            }
        });
    }

    private static URL toUrl(File file) {
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException var2) {
            throw new IllegalArgumentException("Unexpected error", var2);
        }
    }

    private void start() {
        this.printer.start();
        this.client.startAsync((state) -> {
            boolean complete = state.getPiecesRemaining() == 0;
            if (complete) {
                if (this.options.shouldSeedAfterDownloaded()) {
                    this.printer.onDownloadComplete();
                } else {
                    this.printer.stop();
                    this.client.stop();
                }
            }

            this.printer.updateState(state);
        }, 1000L).join();
    }
}
