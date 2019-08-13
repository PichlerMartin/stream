package testui;

import bt.Bt;
import bt.BtClientBuilder;
import bt.cli.CliFileSelector;
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
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import joptsimple.OptionException;
import support.SupportMethods;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.Security;
import java.util.Optional;

import static org.apache.logging.log4j.Level.TRACE;

public class TestStreamClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestStreamClient.class);

    private final Options options;
    private final SessionStatePrinter printer;
    private final BtClient client;

    /**
     * Description:
     * Konstruktor der TestStreamClient Klasse, erstellt alle wichtigen Metadaten wie die Konfiguration,
     * den Storage und den ClientBuilder
     *
     * @param options ist ein Objekt der Options Klasse welche die Kommandozeilenargumente (argsv)
     *                enthält, dem Client die nötigen Daten gibt, z.B.: Torrent-File, Download Ort, ...
     * @throws MalformedURLException wird geworfen wenn z.B.: die URL des Torrent-Files nicht existiert
     */
    public TestStreamClient(Options options) throws MalformedURLException {
        this.options = options;
        this.printer = new SessionStatePrinter();

        Config config = buildConfig(this.options);

        BtRuntime runtime = BtRuntime.builder(config).module(buildDHTModule(options)).autoLoadModules().build();

        Storage storage = new FileSystemStorage(options.getTargetDirectory().toPath());
        PieceSelector selector = options.downloadSequentially() ?
        SequentialSelector.sequential() : RarestFirstSelector.randomizedRarest();

        BtClientBuilder clientBuilder = Bt.client(runtime).storage(storage).selector(selector);

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

    /**
     * Description
     * Erstellt das DHT Modul (distributed hash table) für den Port über den der
     * Download erfolgt
     *
     * @param options das Options Objekt
     * @return
     */
    private static DHTModule buildDHTModule(Options options){
        Optional<Integer> dhtPortOverride = tryGetPort(options.getDhtPort());

        return new DHTModule(new DHTConfig(){
            @Override
            public int getListeningPort(){
                return dhtPortOverride.orElseGet(super::getListeningPort);
            }

            @Override
            public boolean shouldUseRouterBootstrap(){
                return true;
            }
        });
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

    public static void main(String[] args) throws IOException {
        Options options;

        args = new String[] {"-d", "C:\\", "-m", "magnet:?xt=urn:btih:d1eb2b5cf80e286a7f848ab0c31638856db102d4&dn=Beethoven+-+The+Very+Best+Of+Beethoven+%282005%29+%5BFLAC%5D+dussin"};
        try{
            options = Options.parse (args);
        }catch (OptionException e){
            Options.printHelp(System.out);
            return;
        }

        configureLogging(options.getLogLevel());
        configureSecurity();
        SupportMethods.registerLog4jShutdownHook();

        TestStreamClient client = new TestStreamClient(options);
        client.start();
    }

    /**
     * Description
     * Startet den state (lambda expression) für eine bestimmte Zeit (1000)
     * Ausführung wird in einem neuen Thread gestartet
     */
    private void start(){
        printer.start();
        client.startAsync (state -> {
            boolean complete = (state.getPiecesRemaining()==0);
            if (complete){
                if(options.shouldSeedAfterDownloaded()){
                    printer.onDownloadComplete();
                } else{
                    printer.stop();
                    client.stop();
                }
            }

            printer.updateState(state);
        }, 1000).join();
    }

    /**
     * Description
     * Private Methode, die die Security-Policies bei der Klassenvariable "Security"
     * setzt um sie später auf den Client zuzuschreiben
     */
    private static void configureSecurity(){
        String key = "crypto.policy";
        String value = "unlimited";
        try{
            Security.setProperty(key, value);

        } catch (Exception e){
            LOGGER.error("Security Property konnte nicht von '" + key + "' auf '" + value + "' gesetzt werden", e);

        }
    }

    /**
     *
     * @param logLevel: log level, beschreibt den Grad des Logging abhängig vom Verwendungszweck
     *                d.h.: NORMAL im gewöhnlichen Betrieb, VERBOSE beim Debuggen und TRACE
     */
    private static void configureLogging(Options.LogLevel logLevel) {
        Level log4jLogLevel;
        switch (logLevel) {
            case NORMAL: {
                //  NORMAL
                log4jLogLevel = Level.INFO;
                break;
            }
            case VERBOSE: {
                //  VERBOSE
                log4jLogLevel = Level.DEBUG;
                break;
            }
            case TRACE: {
                //  TRACE
                log4jLogLevel = TRACE;
                break;
            }
            default: {
                throw new IllegalArgumentException("Unbekanntes log level: " + logLevel);
            }
        }
        Configurator.setLevel("bt", log4jLogLevel);
    }
}
