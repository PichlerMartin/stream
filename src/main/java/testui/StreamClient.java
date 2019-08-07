package testui;

import bt.Bt;
import bt.BtClientBuilder;
import bt.cli.CliFileSelector;
import bt.cli.Options;
import bt.cli.SessionStatePrinter;
import bt.data.Storage;
import bt.data.file.FileSystemStorage;
import bt.module.ProtocolModule;
import bt.protocol.crypto.EncryptionPolicy;
import bt.runtime.BtClient;
import bt.runtime.BtRuntime;
import bt.runtime.Config;
import bt.service.IRuntimeLifecycleBinder;
import bt.torrent.selector.PieceSelector;
import bt.torrent.selector.RarestFirstSelector;
import bt.torrent.selector.SequentialSelector;
import filelibrary.StreamTorrent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import joptsimple.OptionException;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.Security;
import java.util.Objects;
import java.util.Optional;

import static org.apache.logging.log4j.Level.TRACE;

public class StreamClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(StreamClient.class);

    private final Options options;
    private final SessionStatePrinter printer;
    private final BtClient client;


    public StreamClient(Options options) throws MalformedURLException {
        this.options = options;
        this.printer = new SessionStatePrinter();

        Config config = new Config();

        BtRuntime runtime = BtRuntime.builder(config).module(new ProtocolModule()).autoLoadModules().build();

        Storage storage = new FileSystemStorage(options.getTargetDirectory());
        //  PieceSelector selector = options.downloadSequentially();
        SequentialSelector.sequential();
        RarestFirstSelector.randomizedRarest();

        BtClientBuilder clientBuilder = Bt.client(runtime).storage(storage);

        if (! options.shouldDownloadAllFiles ()) {
            CliFileSelector fileSelector = new CliFileSelector();
            clientBuilder.fileSelector (fileSelector);
            runtime.service (IRuntimeLifecycleBinder.class)
                    .onShutdown (fileSelector :: shutdown);
        }

        clientBuilder.afterTorrentFetched (printer :: onTorrentFetched);
        clientBuilder.afterFilesChosen (printer :: onFilesChosen);

        if (options.getMetainfoFile() != null) {
            clientBuilder = clientBuilder.torrent (new URL(options.getMetainfoFile().toString()));
        } else if (options.getMagnetUri() != null) {
            clientBuilder = clientBuilder.magnet (options.getMagnetUri ());
        } else {
            throw new IllegalStateException ("Torrent file or magnet URI is required");
        }

        this.client = clientBuilder.build ();
    }

    private static Config buildConfig (Options options) {
        Optional <InetAddress> acceptorAddressOverride = getAcceptorAddressOverride (options);
        Optional<Integer> portOverride = tryGetPort (options.getPort ());
        return new Config () {
            @Override
            public InetAddress getAcceptorAddress () {
                return acceptorAddressOverride.orElseGet (super :: getAcceptorAddress); }
            @Override
            public int getAcceptorPort () {
                return portOverride.orElseGet (super :: getAcceptorPort);
            }
            @Override
            public int getNumOfHashingThreads () {
                return Runtime.getRuntime (). availableProcessors ();
            }
            @Override
            public EncryptionPolicy getEncryptionPolicy () {
                return options.enforceEncryption ()?
                        EncryptionPolicy.REQUIRE_ENCRYPTED
                        : EncryptionPolicy.PREFER_PLAINTEXT;
            }
        };
    }

    private static Optional <InetAddress> getAcceptorAddressOverride (Options options) {
        String inetAddress = options.getInetAddress();
        if (inetAddress == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(InetAddress.getByName(inetAddress));
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException("Failed to parse the acceptor's internet address", e);
        }
    }

    private static Optional <Integer> tryGetPort (Integer port) {
        if (port == null) {
            return Optional.empty ();
        } else if (port <1024 || port> 65535) {
            throw new IllegalArgumentException ("Invalid port:" + port +
                    "; expected 1024..65535");
        }
        return Optional.of (port);
    }

    public static void main(String[] args) {
        Options options;

        args = new String[] {"-d", "C:\\"};
        try{
            options = Options.parse (args);
        }catch (OptionException e){
            Options.printHelp(System.out);
        }

        configureLogging(1);
        configureSecurity();
        registerLog4jShutdownHook();
    }

    private static void configureSecurity(){
        String key = "crypto.policy";
        String value = "unlimited";
        try{
            Security.setProperty(key, value);

        } catch (Exception e){
            LOGGER.error("Failed to set security property '" + key + "' to '" + value + "'", e);

        }
    }

    /**
     * https://stackoverflow.com/questions/31416784/thread-with-lambda-expression
     */
    private static void registerLog4jShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                if( LogManager.getContext() instanceof LoggerContext) {
                    Configurator.shutdown((LoggerContext)LogManager.getContext());
                }
            }
        });
    }

    private static void configureLogging(int logLevel) {
        Level log4jLogLevel;
        switch (logLevel) {
            case 1: {
                //  NORMAL
                log4jLogLevel = Level.INFO;
                break;
            }
            case 2: {
                //  VERBOSE
                log4jLogLevel = Level.DEBUG;
                break;
            }
            case 3: {
                //  TRACE
                log4jLogLevel = TRACE;
                break;
            }
            default: {
                throw new IllegalArgumentException("Unknown log level: " + logLevel);
            }
        }
        Configurator.setLevel("bt", log4jLogLevel);
    }
}
