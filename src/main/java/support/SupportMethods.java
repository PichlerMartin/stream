package support;

import bt.cli.Options;
import bt.dht.DHTConfig;
import bt.dht.DHTModule;
import bt.protocol.crypto.EncryptionPolicy;
import bt.runtime.Config;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configurator;
import org.slf4j.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.Security;
import java.util.Optional;

import static org.apache.logging.log4j.Level.TRACE;

public abstract class SupportMethods {

    public static void printHello(){
        System.out.println("Hello");
    }

    /**
     * Description
     * Erstellt das DHT Modul (distributed hash table) für den Port über den der
     * Download erfolgt
     *
     * @param options das Options Objekt
     * @return
     */
    public static DHTModule buildDHTModule(Options options){
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

    public static Config buildConfig (Options options) {
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
    /**
     * Description
     * Private Methode, die die Security-Policies bei der Klassenvariable "Security"
     * setzt um sie später auf den Client zuzuschreiben
     */
    public static void configureSecurity(Logger LOGGER){
        String key = "crypto.policy";
        String value = "unlimited";
        try{
            Security.setProperty(key, value);

        } catch (Exception e){
            LOGGER.error("Security Property konnte nicht von '" + key + "' auf '" + value + "' gesetzt werden", e);

        }
    }

    /**
     * https://stackoverflow.com/questions/31416784/thread-with-lambda-expression
     */
    public static void registerLog4jShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                if( LogManager.getContext() instanceof LoggerContext) {
                    Configurator.shutdown((LoggerContext)LogManager.getContext());
                }
            }
        });
    }

    /**
     *
     * @param logLevel: log level, beschreibt den Grad des Logging abhängig vom Verwendungszweck
     *                d.h.: NORMAL im gewöhnlichen Betrieb, VERBOSE beim Debuggen und TRACE
     */
    public static void configureLogging(Options.LogLevel logLevel) {
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
