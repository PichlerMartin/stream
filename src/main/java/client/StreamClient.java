package client;

import bt.Bt;
import bt.BtClientBuilder;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import support.SupportMethods;

import java.io.File;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Optional;

public class StreamClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(StreamClient.class);
    private final StreamOptions options;
    private final SessionStatePrinter printer;
    private final BtClient client;

    public static void main(String[] args) {
        StreamOptions options = new StreamOptions(args[3], new File(args[1]));

        SupportMethods.configureLogging(options.getLogLevel());
        SupportMethods.configureSecurity(LOGGER);
        SupportMethods.registerLog4jShutdownHook();

        StreamClient client = new StreamClient(options);
        client.start();
    }

    private StreamClient(StreamOptions options) {
        this.options = options;
        this.printer = new SessionStatePrinter();

        Config config = buildConfig(options);
        BtRuntime runtime = BtRuntime.builder(config).module(buildDHTModule(options)).autoLoadModules().build();
        Storage storage = new FileSystemStorage(options.getTargetDirectory().toPath());
        PieceSelector selector = options.downloadSequentially() ? SequentialSelector.sequential() : RarestFirstSelector.randomizedRarest();
        BtClientBuilder clientBuilder = Bt.client(runtime).storage(storage).selector(selector);

        if (options.shouldDownloadAllFiles()) {
            StreamFileSelector fileSelector = new StreamFileSelector();
            clientBuilder.fileSelector(fileSelector);
            runtime.service(IRuntimeLifecycleBinder.class).onShutdown(fileSelector::shutdown);
        }

        clientBuilder.afterFilesChosen(this.printer::onFilesChosen);
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

    private static Optional<Integer> tryGetPort(Integer port) {
        if (port == null) {
            return Optional.empty();
        } else if (port >= 1024 && port <= 65535) {
            return Optional.of(port);
        } else {
            throw new IllegalArgumentException("Invalid port: " + port + "; expected 1024..65535");
        }
    }

    private static Optional<InetAddress> getAcceptorAddressOverride(StreamOptions options) {
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

    private static URL toUrl(File file) {
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException var2) {
            throw new IllegalArgumentException("Unexpected error", var2);
        }
    }

    public void start() {
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
