package client;

import bt.Bt;
import bt.BtClientBuilder;
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
import testui.Controller;
import testui.UI_Controller;

import java.net.MalformedURLException;

import static support.SupportMethods.buildConfig;
import static support.SupportMethods.buildDHTModule;

//import testui.TestStreamClient;

/**
 * Description
 * Klasse StreamClientDeprecated in der ein modifiziertes Objekt der Client-Klasse aus der Bt-Library
 * erzeugt wird, welches dazu dient den Download cer Dateien zu überwachen
 */
public class StreamClientDeprecated implements Client {
    private static final Logger LOGGER = LoggerFactory.getLogger(StreamClientDeprecated.class);

    private final StreamOptions options;
    private final StreamLogPrinter printer;
    private final BtClient client;

    //  ToDo:   implement self-written StreamClientDeprecated class

    /**
     * Description:
     * Konstruktor der TestStreamClient Klasse, erstellt alle wichtigen Metadaten wie die Konfiguration,
     * den Storage und den ClientBuilder
     *
     * @param options ist ein Objekt der Options Klasse welche die Kommandozeilenargumente (argsv)
     *                enthält, dem Client die nötigen Daten gibt, z.B.: Torrent-File, Download Ort, ...
     * @throws MalformedURLException wird geworfen wenn z.B.: die URL des Torrent-Files nicht existiert
     */
    public StreamClientDeprecated(StreamOptions options, Controller controller) {
        this.options = options;

        controller = controller != null ? controller : new UI_Controller();

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
        }, 1000).join();
    }

    /**
     * Description
     * Methode zur Vereinfachung der Client-Beschaffung, das Objekt clientBuilder wird mit den Parametern
     * der Funktion gespeist, und danach werden einige Optionen festgelegt, u.a. "Sollten alle
     * Dateien heruntergeladen werden?", usw.
     *
     * @param runtime: ein generisches Runtime-Objekt der Bt-Library
     * @param storage: der Speicherplatz auf der Festplatte
     * @param selector: Hilfsobjekt zur auswahl der einzelnen Torrent-Dateien
     * @return: gibt den fertigen Client zurück
     */
    private BtClient GetClient(BtRuntime runtime, Storage storage, PieceSelector selector){
        BtClientBuilder clientBuilder = Bt.client(runtime).storage(storage).selector(selector);

        //options.setDownloadAllFiles(true);

        //  ToDo:   Continue here somewhere, idk
        //  ToDo:   Test with own hotspot

        if(options.shouldDownloadAllFiles()){
            StreamFileSelector fileSelector = new StreamFileSelector();
            clientBuilder.fileSelector(fileSelector);
            runtime.service(IRuntimeLifecycleBinder.class).onShutdown(fileSelector::shutdown);
        }

        clientBuilder.afterTorrentFetched (printer :: whenTorrentFetched);
        clientBuilder.afterFilesChosen(printer::onFilesChosen);

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