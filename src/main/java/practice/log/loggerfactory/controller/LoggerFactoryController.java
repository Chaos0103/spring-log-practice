package practice.log.loggerfactory.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import practice.log.loggerfactory.service.LoggerFactoryService;

@RestController
@RequestMapping("/v1")
public class LoggerFactoryController {

    private static final Logger log = LoggerFactory.getLogger(LoggerFactoryController.class);

    private final LoggerFactoryService loggerFactoryService;

    public LoggerFactoryController(LoggerFactoryService loggerFactoryService) {
        this.loggerFactoryService = loggerFactoryService;
    }

    @GetMapping
    public String save() {
        log.trace("Trace Level");
        log.debug("Debug Level");
        log.info("Info Level");
        log.warn("Warn Level");
        log.error("Error Level");

        loggerFactoryService.save();

        return "ok";
    }
}
