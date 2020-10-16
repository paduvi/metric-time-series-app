package io.dogy.metrictimeseriesapp.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadLocalRandom;

@RequestMapping("/ping")
@RestController
public class PingController {

    @GetMapping(produces = {MediaType.TEXT_PLAIN_VALUE})
    public String ping() throws InterruptedException {
        Thread.sleep(ThreadLocalRandom.current().nextInt(50));
        return "ping";
    }

}

