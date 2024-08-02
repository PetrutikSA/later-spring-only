package ru.practicum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    public static final Logger logger= LoggerFactory.getLogger(TestController.class);

    @GetMapping("/test")
    public String test() {
        logger.trace("get request");
        return "OK!";
    }
}
