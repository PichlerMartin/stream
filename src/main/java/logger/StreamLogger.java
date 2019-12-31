package logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class StreamLogger {

    @Deprecated
    public static org.slf4j.Logger getLogger(Object baseclass) {
        Logger logger = LoggerFactory.getLogger(baseclass.getClass());

        logger.error("severe message");

        logger.warn("warning message");

        logger.info("info message");

        return logger;
    }
}
