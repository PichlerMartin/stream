package support;

import bt.dht.DHTConfig;
import bt.dht.DHTModule;
import bt.protocol.crypto.EncryptionPolicy;
import bt.runtime.Config;
import client.StreamOptions;
import client.StreamOptions.LogLevel;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configurator;
import org.slf4j.Logger;

import java.io.File;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.Security;
import java.util.Objects;
import java.util.Optional;

/**
 * A collection of abstract Methods which can be used for configuration of the Torrent-Client,
 * they serve to assign specific environmental values to the variables. Most of the methods are
 * called within the StreamClient.java class, and are tailored for its needs.
 */
public abstract class SupportMethods {

    /**
     * <p>converts a file to URL</p>
     * @param file input file
     * @return the new URL
     */
    public static URL toUrl(File file) {
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException var2) {
            throw new IllegalArgumentException("Unexpected error", var2);
        }
    }

    /**
     * Private Methode, die die Security-Policies bei der Klassenvariable "Security"
     * setzt um sie später auf den Client zuzuschreiben
     */
    public static void configureSecurity(Logger LOGGER) {
        String key = "crypto.policy";
        String value = "unlimited";
        try {
            Security.setProperty(key, value);

        } catch (Exception e) {
            LOGGER.error("Security Property konnte nicht von '" + key + "' auf '" + value + "' gesetzt werden", e);

        }
    }

    /**
     * https://stackoverflow.com/questions/31416784/thread-with-lambda-expression
     */
    public static void registerLog4jShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (LogManager.getContext() instanceof LoggerContext) {
                Configurator.shutdown((LoggerContext) LogManager.getContext());
            }
        }));
    }

    /**
     * @param logLevel: log level, beschreibt den Grad des Logging abhängig vom Verwendungszweck
     *                  d.h.: NORMAL im gewöhnlichen Betrieb, VERBOSE beim Debuggen und TRACE
     */
    public static void configureLogging(LogLevel logLevel) {
        Level log4jLogLevel;
        switch (Objects.requireNonNull(logLevel)) {
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
                log4jLogLevel = Level.TRACE;
                break;
            }
            default: {
                throw new IllegalArgumentException("Unbekanntes log level: " + logLevel.name());
            }
        }
        Configurator.setLevel("bt", log4jLogLevel);
    }

    /**
     * <p>builds a new DHT module</p>
     *
     * @param options das Options Objekt
     * @return the newly built DHT module
     */
    public static DHTModule buildDHTModule(StreamOptions options) {
        Optional<Integer> dhtPortOverride = tryGetPort(options.getDhtPort());

        return new DHTModule(new DHTConfig() {
            @Override
            public int getListeningPort() {
                return dhtPortOverride.orElseGet(super::getListeningPort);
            }

            @Override
            public boolean shouldUseRouterBootstrap() {
                return true;
            }
        });
    }

    /**
     * <p>attepts to retrieve the requested incoming connections port, if fail exception is thrown</p>
     * @param port port suggested by the user in the gui
     * @return returns either demanded port or empty port (-> standard port is used)
     * @see SupportMethods#buildDHTModule
     */
    private static Optional<Integer> tryGetPort(Integer port) {
        if (port == null) {
            return Optional.empty();
        } else if (port < 1024 || port > 65535) {
            throw new IllegalArgumentException("Invalid port:" + port +
                    "; expected 1024..65535");
        }
        return Optional.of(port);
    }

    /**
     * <p>builds a new object of {@link Config}, whereas the network configuration is defined</p>
     * @param options options object
     * @return the new built config
     */
    public static Config buildConfig(StreamOptions options) {
        Optional<InetAddress> acceptorAddressOverride = getAcceptorAddressOverride(options);
        Optional<Integer> portOverride = tryGetPort(options.getPort());
        return new Config() {
            @Override
            public InetAddress getAcceptorAddress() {
                return acceptorAddressOverride.orElseGet(super::getAcceptorAddress);
            }

            @Override
            public int getAcceptorPort() {
                return portOverride.orElseGet(super::getAcceptorPort);
            }

            @Override
            public int getNumOfHashingThreads() {
                return Runtime.getRuntime().availableProcessors();
            }

            @Override
            public EncryptionPolicy getEncryptionPolicy() {
                return options.enforceEncryption() ?
                        EncryptionPolicy.REQUIRE_ENCRYPTED
                        : EncryptionPolicy.PREFER_PLAINTEXT;
            }
        };
    }

    /**
     * <p>determines wheter the current default ip address for in and outgoing connections should
     * be tossed, and replaced by a new internet address</p>
     * @param options StreamOptions object, delivered by {@link SupportMethods#buildConfig}
     * @return optional internet address or empty (default)
     */
    private static Optional<InetAddress> getAcceptorAddressOverride(StreamOptions options) {
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
