package practice.log.loggerfactory.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LoggerFactoryService {

    private static final Logger log = LoggerFactory.getLogger(LoggerFactoryService.class);

    public void save() {
        log.trace("Trace Level");
        log.debug("Debug Level");
        log.info("Info Level");
        log.warn("Warn Level");
        log.error("Error Level");
    }
}
