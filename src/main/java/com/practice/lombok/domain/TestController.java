package com.practice.lombok.domain;

import com.practice.lombok.global.annotation.logger.Slf4j;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Slf4j
    Logger log = null;


    @GetMapping("/logger")
    public String loggerTest(){
        log.info("test");
        return "ok";
    }
}
