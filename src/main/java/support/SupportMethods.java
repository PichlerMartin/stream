package support;

import bt.dht.DHTConfig;
import bt.dht.DHTModule;
import bt.protocol.crypto.EncryptionPolicy;
import bt.runtime.Config;
import client.StreamOptions;
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
import java.util.Optional;

public abstract class SupportMethods {

    public static void printHello(){
        System.out.println("Hello");
    }

    public static URL toUrl(File file) {
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException var2) {
            throw new IllegalArgumentException("Unexpected error", var2);
        }
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
    public static void configureLogging(StreamOptions.LogLevel logLevel) {
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
                log4jLogLevel = Level.TRACE;
                break;
            }
            default: {
                throw new IllegalArgumentException("Unbekanntes log level: " + logLevel);
            }
        }
        Configurator.setLevel("bt", log4jLogLevel);
    }
}
