package be.glever.util.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

/**
 * Logger util to allow for slf4j lambda params until slf4j 2.0.0 is released.
 * It was easier to write this util than to switch logging frameworks which do support lambdas.
 */
public class Log {

    private Logger logger;

    private Log(Logger logger) {
        this.logger = logger;
    }

    public static Log getLogger(Class<?> clazz) {
        return new Log(LoggerFactory.getLogger(clazz));
    }

    public void debug(Supplier<String> msgSupplier) {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug(msgSupplier.get());
        }
    }

    public void info(Supplier<String> msgSupplier) {
        if (this.logger.isInfoEnabled()) {
            this.logger.info(msgSupplier.get());
        }
    }

    public void warn(Supplier<String> msgSupplier) {
        if (this.logger.isWarnEnabled()) {
            this.logger.warn(msgSupplier.get());
        }
    }

    public void error(Supplier<String> msgSupplier) {
        if (this.logger.isErrorEnabled()) {
            this.logger.error(msgSupplier.get());
        }
    }

    public void error(Supplier<String> msgSupplier, Throwable t) {
        if (this.logger.isErrorEnabled()) {
            this.logger.error(msgSupplier.get(), t);
        }
    }
}
