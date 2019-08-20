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
import bt.torrent.fileselector.TorrentFileSelector;
import bt.torrent.selector.PieceSelector;
import bt.torrent.selector.RarestFirstSelector;
import bt.torrent.selector.SequentialSelector;
import com.google.inject.Module;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import support.SupportMethods;
import testui.Controller;
import testui.Select_Controller;
import testui.UI_Controller;
//import testui.TestStreamClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.Optional;

public class StreamClient implements Client {
    private static final Logger LOGGER = LoggerFactory.getLogger(StreamClient.class);

    private final StreamOptions options;
    private final StreamLogPrinter printer;
    private final BtClient client;

    //  ToDo:   implement self-written StreamClient class

    /**
     * Description:
     * Konstruktor der TestStreamClient Klasse, erstellt alle wichtigen Metadaten wie die Konfiguration,
     * den Storage und den ClientBuilder
     *
     * @param options ist ein Objekt der Options Klasse welche die Kommandozeilenargumente (argsv)
     *                enthält, dem Client die nötigen Daten gibt, z.B.: Torrent-File, Download Ort, ...
     * @throws MalformedURLException wird geworfen wenn z.B.: die URL des Torrent-Files nicht existiert
     */
    public StreamClient(StreamOptions options) throws MalformedURLException {
        this.options = options;

        SupportMethods.configureLogging(options.getLogLevel());
        SupportMethods.configureSecurity(LOGGER);
        SupportMethods.registerLog4jShutdownHook();

        this.printer = new StreamLogPrinter();

        Config config = buildConfig(this.options);

        BtRuntime runtime = BtRuntime.builder(config).module(buildDHTModule(options)).autoLoadModules().build();

        Storage storage = new FileSystemStorage(options.getTargetDirectory().toPath());
        PieceSelector selector = options.downloadSequentially() ?
                SequentialSelector.sequential() : RarestFirstSelector.randomizedRarest();

        this.client = GetClient(runtime, storage, selector);
    }

    /**
     * Description
     * Startet den state (lambda expression) für eine bestimmte Zeit (1000)
     * Ausführung wird in einem neuen Thread gestartet
     */
    public void start(){

        printer.startLogPrinter();
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

            printer.updateTorrentStage(state);
        }, 1000);
    }

    private BtClient GetClient(BtRuntime runtime, Storage storage, PieceSelector selector) throws MalformedURLException{
        BtClientBuilder clientBuilder = Bt.client(runtime).storage(storage).selector(selector);

        options.setDownloadAllFiles(true);

        StreamFileSelector fileSelector = new StreamFileSelector();
        clientBuilder.fileSelector(fileSelector);
        runtime.service(IRuntimeLifecycleBinder.class).onShutdown(fileSelector::shutdown);

        clientBuilder.afterTorrentFetched (printer :: whenTorrentFetched);

        if (options.getMetainfoFile() != null) {
            clientBuilder = clientBuilder.torrent(SupportMethods.toUrl(options.getMetainfoFile()));
        } else if (options.getMagnetUri() != null) {
            clientBuilder = clientBuilder.magnet (options.getMagnetUri ());
        } else {
            throw new IllegalStateException ("Torrent file or magnet URI is required");
        }

        return clientBuilder.build ();
    }

    /**
     * Description
     * Erstellt das DHT Modul (distributed hash table) für den Port über den der
     * Download erfolgt
     *
     * @param options das Options Objekt
     * @return
     */
    public static DHTModule buildDHTModule(StreamOptions options){
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

    private static Optional <Integer> tryGetPort (Integer port) {
        if (port == null) {
            return Optional.empty ();
        } else if (port <1024 || port> 65535) {
            throw new IllegalArgumentException ("Invalid port:" + port +
                    "; expected 1024..65535");
        }
        return Optional.of (port);
    }

    public static Config buildConfig (StreamOptions options) {
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

    private static Optional <InetAddress> getAcceptorAddressOverride (StreamOptions options) {
        String inetAddress = options.getInetAddress();
        if (inetAddress == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(InetAddress.getByName(inetAddress));
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException("The acceptor inet adress could not be parsed!", e);
        }
    }
}
