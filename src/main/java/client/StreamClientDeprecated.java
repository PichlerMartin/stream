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

import static support.SupportMethods.buildConfig;
import static support.SupportMethods.buildDHTModule;

/**
 * <p>StreamClientDeprecated is a modified version of the class {@link StreamClient} from the
 * client package, which was created to overview the current downloads</p>
 *
 * @author PichlerMartin
 * @deprecated use {@link StreamClient} instead
 */
@Deprecated
public class StreamClientDeprecated implements Client {
    private static final Logger LOGGER = LoggerFactory.getLogger(StreamClientDeprecated.class);

    private final StreamOptions options;
    private final StreamStatusProcessor printer;
    private final BtClient client;

    /**
     * <p>constructor of the class StreamClientDeprecated with all sorts of stuff, such as configuration,
     * storage and {@link BtClientBuilder}</p>
     *
     * @param options is an object which supplies the builder with the needed metadata such as download
     *                location and magnet-uri/link
     * @deprecated use {@link StreamClient} instead
     */
    @Deprecated
    public StreamClientDeprecated(StreamOptions options) {
        this.options = options;

        SupportMethods.configureLogging(options.getLogLevel());
        SupportMethods.configureSecurity(LOGGER);
        SupportMethods.registerLog4jShutdownHook();

        this.printer = new StreamStatusProcessor();

        Config config = buildConfig(this.options);

        BtRuntime runtime = BtRuntime.builder(config).module(buildDHTModule(options)).autoLoadModules().build();

        Storage storage = new FileSystemStorage(options.getTargetDirectory().toPath());
        PieceSelector selector = options.downloadSequentially() ?
                SequentialSelector.sequential() : RarestFirstSelector.randomizedRarest();

        this.client = GetClient(runtime, storage, selector);
    }

    /**
     * Startet den state (lambda expression) für eine bestimmte Zeit (1000)
     * Ausführung wird in einem neuen Thread gestartet
     */
    @Deprecated
    public void start() {

        printer.startStatusProcessor();
        client.startAsync(state -> {
            boolean complete = (state.getPiecesRemaining() == 0);
            if (complete) {
                if (options.shouldSeedAfterDownloaded()) {
                    printer.onDownloadComplete();
                } else {
                    printer.stop();
                    client.stop();
                }
            }

            printer.updateTorrentStage(state);
        }, 1000).join();
    }

    /**
     * Methode zur Vereinfachung der Client-Beschaffung, das Objekt clientBuilder wird mit den Parametern
     * der Funktion gespeist, und danach werden einige Optionen festgelegt, u.a. "Sollten alle
     * Dateien heruntergeladen werden?", usw.
     *
     * <p>method which simplifies the client builder process from {@link StreamClientDeprecated#StreamClientDeprecated(StreamOptions)},
     * from above. some options are already defined here such as "should all data be downloaded?"</p>
     *
     * @param runtime:  a generic object of the bt-library
     * @param storage:  a storage place on the hard drive
     * @param selector: helper object for selection of torrent files
     * @return: returns the runable client
     * @deprecated is no longer in development
     */
    @Deprecated
    private BtClient GetClient(BtRuntime runtime, Storage storage, PieceSelector selector) {
        BtClientBuilder clientBuilder = Bt.client(runtime).storage(storage).selector(selector);

        if (options.shouldDownloadAllFiles()) {
            StreamFileSelector fileSelector = new StreamFileSelector();
            clientBuilder.fileSelector(fileSelector);
            runtime.service(IRuntimeLifecycleBinder.class).onShutdown(fileSelector::shutdown);
        }

        clientBuilder.afterTorrentFetched(printer::whenTorrentFetched);
        clientBuilder.afterFilesChosen(printer::onFilesChosen);

        if (options.getMetainfoFile() != null) {
            clientBuilder = clientBuilder.torrent(SupportMethods.toUrl(options.getMetainfoFile()));
        } else if (options.getMagnetUri() != null) {
            clientBuilder = clientBuilder.magnet(options.getMagnetUri());
        } else {
            throw new IllegalStateException("Torrent file or magnet URI is required");
        }

        return clientBuilder.build();
    }
}