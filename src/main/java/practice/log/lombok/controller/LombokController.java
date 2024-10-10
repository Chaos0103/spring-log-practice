package practice.log.lombok.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import practice.log.lombok.service.LombokService;

@Slf4j
@RestController
@RequestMapping("/v2")
public class LombokController {

    private final LombokService lombokService;

    public LombokController(LombokService lombokService) {
        this.lombokService = lombokService;
    }

    @GetMapping
    public String save() {
        log.trace("Trace Level");
        log.debug("Debug Level");
        log.info("Info Level");
        log.warn("Warn Level");
        log.error("Error Level");

        lombokService.save();

        return "ok";
    }
}
