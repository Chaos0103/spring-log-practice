package practice.log.lombok.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LombokService {

    public void save() {
        log.trace("Trace Level");
        log.debug("Debug Level");
        log.info("Info Level");
        log.warn("Warn Level");
        log.error("Error Level");
    }
}
