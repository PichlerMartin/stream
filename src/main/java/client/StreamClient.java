package client;

import bt.Bt;
import bt.BtClientBuilder;
import bt.cli.CliFileSelector;
import bt.cli.Options;
import bt.cli.SessionStatePrinter;
import bt.data.Storage;
import bt.data.file.FileSystemStorage;
import bt.runtime.BtClient;
import bt.runtime.BtRuntime;
import bt.runtime.Config;
import bt.service.IRuntimeLifecycleBinder;
import bt.torrent.selector.PieceSelector;
import bt.torrent.selector.RarestFirstSelector;
import bt.torrent.selector.SequentialSelector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import support.SupportMethods;
import testui.TestStreamClient;

import java.net.MalformedURLException;

public class StreamClient implements Client {
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
    public StreamClient(Options options) throws MalformedURLException {
        this.options = options;

        SupportMethods.configureLogging(options.getLogLevel());
        SupportMethods.configureSecurity(LOGGER);
        SupportMethods.registerLog4jShutdownHook();

        this.printer = new SessionStatePrinter();

        Config config = SupportMethods.buildConfig(this.options);

        BtRuntime runtime = BtRuntime.builder(config).module(SupportMethods.buildDHTModule(options)).autoLoadModules().build();

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

    private BtClient GetClient(BtRuntime runtime, Storage storage, PieceSelector selector) throws MalformedURLException{
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
            clientBuilder = clientBuilder.torrent(SupportMethods.toUrl(options.getMetainfoFile()));
        } else if (options.getMagnetUri() != null) {
            clientBuilder = clientBuilder.magnet (options.getMagnetUri ());
        } else {
            throw new IllegalStateException ("Torrent file or magnet URI is required");
        }

        return clientBuilder.build ();
    }
}
