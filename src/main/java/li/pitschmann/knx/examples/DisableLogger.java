package li.pitschmann.knx.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DisableLogger {

    /**
     * Disables the Logger
     */
    public static void disable() {
        ((ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)).setLevel(ch.qos.logback.classic.Level.OFF);
    }
}
