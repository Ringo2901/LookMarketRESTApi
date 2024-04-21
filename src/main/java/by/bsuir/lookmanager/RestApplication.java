package by.bsuir.lookmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@SpringBootApplication
@EnableJpaRepositories
public class RestApplication {
    private static final Logger LOGGER = LogManager.getLogger(RestApplication.class);
    static {
        System.setProperty("Log4jContextSelector", "org.apache.logging.log4j.core.async.AsyncLoggerContextSelector");
    }
    public static void main(String[] args) {
        LOGGER.debug("Debugging log");
        LOGGER.info("Info log");
        LOGGER.warn("Warning log");
        LOGGER.error("Error log");
        LOGGER.fatal("Fatal log");
        SpringApplication.run(RestApplication.class);
    }
}

